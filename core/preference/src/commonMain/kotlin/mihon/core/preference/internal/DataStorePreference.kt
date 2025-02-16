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
import kotlinx.coroutines.flow.StateFlow
import mihon.core.preference.Preference

internal class DataStorePreference<T>(
    private val delegate: StateFlow<Map<Preferences.Key<*>, Any>>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
) : Preference<T> {
    override fun key(): String {
        return key.name
    }

    override fun get(): T {
        TODO("Not yet implemented")
    }

    override fun isSet(): Boolean {
        return delegate.value.containsKey(key)
    }

    override fun delete() {
        TODO("Not yet implemented")
    }

    override fun defaultValue(): T {
        return defaultValue
    }

    override fun changes(): Flow<T> {
        TODO("Not yet implemented")
    }

    override fun stateIn(scope: CoroutineScope): StateFlow<T> {
        TODO("Not yet implemented")
    }

    override fun set(value: T) {
        TODO("Not yet implemented")
    }
}
