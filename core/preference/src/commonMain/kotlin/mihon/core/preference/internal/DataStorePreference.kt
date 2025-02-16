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

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mihon.core.preference.Preference
import kotlin.time.Duration.Companion.seconds

internal class DataStorePreference<T>(
    private val dataStateFlow: StateFlow<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
    private val setValue: (T?) -> Unit,
) : Preference<T> {
    private val data inline get() = dataStateFlow.value

    override fun key(): String {
        return key.name
    }

    override fun get(): T {
        return try {
            data[key] ?: defaultValue
        } catch (_: ClassCastException) {
            delete()
            defaultValue
        }
    }

    override fun set(value: T) {
        setValue(value)
    }

    override fun isSet(): Boolean {
        return data.contains(key)
    }

    override fun delete() {
        setValue(null)
    }

    override fun defaultValue(): T {
        return defaultValue
    }

    override fun changes(): Flow<T> {
        return dataStateFlow.map {
            try {
                it[key] ?: defaultValue
            } catch (_: ClassCastException) {
                delete()
                defaultValue
            }
        }
    }

    override fun stateIn(scope: CoroutineScope): StateFlow<T> {
        return changes().stateIn(scope, SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds), get())
    }
}
