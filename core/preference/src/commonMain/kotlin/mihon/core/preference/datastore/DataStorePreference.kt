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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import mihon.core.preference.Preference

internal class DataStorePreference<T>(
    override val key: String,
    private val defaultValue: T,
    private val getValue: () -> T,
    private val getValueFlow: () -> Flow<T>,
    private val isValueSet: () -> Boolean,
    private val setValue: (T?) -> Unit,
) : Preference<T> {

    override fun get(): T {
        return try {
            getValue()
        } catch (_: ClassCastException) {
            delete()
            defaultValue
        }
    }

    override fun getFlow(): Flow<T> {
        return getValueFlow().catch { e ->
            if (e !is ClassCastException) throw e

            delete()
            emit(defaultValue)
        }
    }

    override fun getStateFlow(scope: CoroutineScope, started: SharingStarted): StateFlow<T> {
        return getFlow().stateIn(scope, started, get())
    }

    override fun set(value: T) {
        setValue(value)
    }

    override fun isSet(): Boolean {
        return isValueSet()
    }

    override fun delete() {
        setValue(null)
    }
}
