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

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import mihon.core.preference.PreferenceMigration
import androidx.datastore.preferences.core.Preferences as AndroidXPreferences
import mihon.core.preference.Preferences
import okio.Path

class DataStorePreferencesFactory(private val preferencesDirectory: Path): Preferences.Factory {

    override fun default(migrations: List<PreferenceMigration>): Preferences {
        return get(DEFAULT_NAME, migrations)
    }

    override fun named(name: String, migrations: List<PreferenceMigration>): Preferences {
        return get("$name.named", migrations)
    }

    private fun get(name: String, migrations: List<PreferenceMigration>): Preferences {
        return PreferenceDataStoreFactory.createWithPath(
            migrations = listOf(object : DataMigration<AndroidXPreferences> {
                override suspend fun cleanUp() {
                    TODO("Not yet implemented")
                }

                override suspend fun shouldMigrate(currentData: AndroidXPreferences): Boolean {
                    TODO("Not yet implemented")
                }

                override suspend fun migrate(currentData: AndroidXPreferences): AndroidXPreferences {
                    TODO("Not yet implemented")
                }

            }),
            produceFile = { preferencesDirectory.resolve("$name.preferences_pb") },
        )
            .let { DataStorePreferences(it) }
    }

    companion object {
        private const val DEFAULT_NAME = "_mihon"
    }
}
