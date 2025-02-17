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
package mihon.core.preference.internal

import com.russhwolf.settings.ObservableSettings
import mihon.core.preference.Preference
import mihon.core.preference.PreferenceStore

class PreferenceStoreImpl(private val settings: ObservableSettings) : PreferenceStore {
    override fun getString(key: String, defaultValue: String): Preference<String> {
        return StringPreferences(settings, key, defaultValue)
    }

    override fun getLong(key: String, defaultValue: Long): Preference<Long> {
        return LongPreferences(settings, key, defaultValue)
    }

    override fun getInt(key: String, defaultValue: Int): Preference<Int> {
        return IntPreferences(settings, key, defaultValue)
    }

    override fun getFloat(key: String, defaultValue: Float): Preference<Float> {
        return FloatPreferences(settings, key, defaultValue)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> {
        return BooleanPreferences(settings, key, defaultValue)
    }

    override fun getStringSet(key: String, defaultValue: Set<String>): Preference<Set<String>> {
        TODO("Not yet implemented")
    }

    override fun <T> getObject(
        key: String,
        defaultValue: T,
        serializer: (T) -> String,
        deserializer: (String) -> T,
    ): Preference<T> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Map<String, Any> {
        TODO("Not yet implemented")
    }
}
