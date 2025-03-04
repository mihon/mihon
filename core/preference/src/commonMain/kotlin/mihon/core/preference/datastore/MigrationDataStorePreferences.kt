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

import androidx.datastore.preferences.core.MutablePreferences as AndroidXMutablePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.datastore.preferences.core.Preferences as AndroidXPreferences

internal class MigrationDataStorePreferences(delegate: AndroidXPreferences) : BaseDataStorePreferences() {

    private val mutableDelegate: AndroidXMutablePreferences = delegate.toMutablePreferences()

    override val dataStateFlow: StateFlow<AndroidXPreferences> = MutableStateFlow(mutableDelegate).asStateFlow()

    override fun <T> setValue(key: AndroidXPreferences.Key<T>, value: T?) {
        mutableDelegate.apply {
            if (value == null) {
                remove(key)
            } else {
                set(key, value)
            }
        }
    }

    internal fun getAndroidXPreferences(): AndroidXPreferences = mutableDelegate.toPreferences()
}
