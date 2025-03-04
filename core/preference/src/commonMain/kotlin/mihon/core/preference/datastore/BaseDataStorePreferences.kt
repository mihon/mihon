/*
 * Copyright (C) 2025 AntsyLich and The Mihon Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mihon.core.preference.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import mihon.core.preference.Preference

internal abstract class BaseDataStorePreferences : mihon.core.preference.Preferences {

    protected abstract val dataStateFlow: StateFlow<Preferences>

    private val data: Preferences inline get() = dataStateFlow.value

    override fun getString(key: String, defaultValue: String): Preference<String> =
        getBuiltin(stringPreferencesKey(key), defaultValue)

    override fun getInt(key: String, defaultValue: Int): Preference<Int> =
        getBuiltin(intPreferencesKey(key), defaultValue)

    override fun getLong(key: String, defaultValue: Long): Preference<Long> =
        getBuiltin(longPreferencesKey(key), defaultValue)

    override fun getFloat(key: String, defaultValue: Float): Preference<Float> =
        getBuiltin(floatPreferencesKey(key), defaultValue)

    override fun getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> =
        getBuiltin(booleanPreferencesKey(key), defaultValue)

    override fun getStringSet(key: String, defaultValue: Set<String>): Preference<Set<String>> =
        getBuiltin(stringSetPreferencesKey(key), defaultValue)

    private fun <T> getBuiltin(key: Preferences.Key<T>, defaultValue: T): Preference<T> {
        return DataStorePreference(
            key = key.name,
            defaultValue = defaultValue,
            getValue = { data[key] ?: defaultValue },
            getValueFlow = { dataStateFlow.map { it[key] ?: defaultValue } },
            setValue = { setValue(key, it) },
            isValueSet = { data.contains(key) },
        )
    }

    override fun <T> getObject(
        key: String,
        defaultValue: T,
        serializer: (T) -> String,
        deserializer: (String) -> T,
    ): Preference<T> {
        val delegateKey = stringPreferencesKey(key)
        return DataStorePreference(
            key = key,
            defaultValue = defaultValue,
            getValue = { data[delegateKey]?.let(deserializer) ?: defaultValue },
            getValueFlow = { dataStateFlow.map { it[delegateKey]?.let(deserializer) ?: defaultValue } },
            setValue = { setValue(delegateKey, it?.let(serializer)) },
            isValueSet = { data.contains(delegateKey) },
        )
    }

    override fun <T> getObjectSet(
        key: String,
        defaultValue: Set<T>,
        serializer: (T) -> String,
        deserializer: (String) -> T,
    ): Preference<Set<T>> {
        val delegateKey = stringSetPreferencesKey(key)
        return DataStorePreference(
            key = key,
            defaultValue = defaultValue,
            getValue = { data[delegateKey]?.mapToSet(deserializer) ?: defaultValue },
            getValueFlow = { dataStateFlow.map { it[delegateKey]?.mapToSet(deserializer) ?: defaultValue } },
            setValue = { setValue(delegateKey, it?.mapToSet(serializer)) },
            isValueSet = { data.contains(delegateKey) },
        )
    }

    protected abstract fun <T> setValue(key: Preferences.Key<T>, value: T?)

    private inline fun <T, R> Set<T>.mapToSet(transform: (T) -> R): Set<R> {
        return buildSet { this@mapToSet.mapTo(this, transform) }
    }

    override fun getAll(): Map<String, Any> {
        return dataStateFlow.value.asMap().mapKeys { (key, _) -> key.name }
    }
}
