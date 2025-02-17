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

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getBooleanStateFlow
import com.russhwolf.settings.coroutines.getFloatFlow
import com.russhwolf.settings.coroutines.getFloatStateFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getIntStateFlow
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getLongStateFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.coroutines.getStringStateFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import mihon.core.preference.Preference

internal abstract class AbstractPreference<T>(
    private val settings: ObservableSettings,
    private val key: String,
    private val default: T,
    private val internalGet: () -> T,
    private val internalGetFlow: () -> Flow<T>,
    private val internalGetStateFlow: (CoroutineScope, SharingStarted) -> StateFlow<T>,
    private val internalSet: (T) -> Unit,
) : Preference<T> {

    override fun key(): String {
        return key
    }

    override fun get(): T {
        return internalGet()
    }

    override fun getFlow(): Flow<T> {
        return internalGetFlow()
    }

    override fun getStateFlow(scope: CoroutineScope, started: SharingStarted): StateFlow<T> {
        return internalGetStateFlow(scope, started)
    }

    override fun default(): T {
        return default
    }

    override fun set(value: T) {
        internalSet(value)
    }

    override fun isSet(): Boolean {
        return settings.hasKey(key)
    }

    override fun delete() {
        settings.remove(key)
    }
}

@OptIn(ExperimentalSettingsApi::class)
internal class StringPreferences(
    private val settings: ObservableSettings,
    private val key: String,
    private val default: String,
) : AbstractPreference<String>(
    settings = settings,
    key = key,
    default = default,
    internalGet = { settings.getString(key, default) },
    internalGetFlow = { settings.getStringFlow(key, default) },
    internalGetStateFlow = { scope, started ->
        settings.getStringStateFlow(scope, key, default, started)
    },
    internalSet = { settings[key] = it },
)

@OptIn(ExperimentalSettingsApi::class)
internal class LongPreferences(
    private val settings: ObservableSettings,
    private val key: String,
    private val default: Long,
) : AbstractPreference<Long>(
    settings = settings,
    key = key,
    default = default,
    internalGet = { settings.getLong(key, default) },
    internalGetFlow = { settings.getLongFlow(key, default) },
    internalGetStateFlow = { scope, started ->
        settings.getLongStateFlow(scope, key, default, started)
    },
    internalSet = { settings[key] = it },
)

@OptIn(ExperimentalSettingsApi::class)
internal class IntPreferences(
    private val settings: ObservableSettings,
    private val key: String,
    private val default: Int,
) : AbstractPreference<Int>(
    settings = settings,
    key = key,
    default = default,
    internalGet = { settings.getInt(key, default) },
    internalGetFlow = { settings.getIntFlow(key, default) },
    internalGetStateFlow = { scope, started ->
        settings.getIntStateFlow(scope, key, default, started)
    },
    internalSet = { settings[key] = it },
)

@OptIn(ExperimentalSettingsApi::class)
internal class FloatPreferences(
    private val settings: ObservableSettings,
    private val key: String,
    private val default: Float,
) : AbstractPreference<Float>(
    settings = settings,
    key = key,
    default = default,
    internalGet = { settings.getFloat(key, default) },
    internalGetFlow = { settings.getFloatFlow(key, default) },
    internalGetStateFlow = { scope, started ->
        settings.getFloatStateFlow(scope, key, default, started)
    },
    internalSet = { settings[key] = it },
)

@OptIn(ExperimentalSettingsApi::class)
internal class BooleanPreferences(
    private val settings: ObservableSettings,
    private val key: String,
    private val default: Boolean,
) : AbstractPreference<Boolean>(
    settings = settings,
    key = key,
    default = default,
    internalGet = { settings.getBoolean(key, default) },
    internalGetFlow = { settings.getBooleanFlow(key, default) },
    internalGetStateFlow = { scope, started ->
        settings.getBooleanStateFlow(scope, key, default, started)
    },
    internalSet = { settings[key] = it },
)
