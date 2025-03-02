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
import androidx.datastore.preferences.core.Preferences
import mihon.core.preference.PreferenceStore
import mihon.core.preference.PreferenceStoreFactory
import okio.Path

class DataStorePreferenceStoreFactory(private val preferencesDirectory: Path): PreferenceStoreFactory {

    override fun default(): PreferenceStore {
        return internalGet(DEFAULT_NAME)
    }

    override fun get(name: String): PreferenceStore {
        return internalGet("_$name")
    }

    private fun internalGet(
        name: String,
        migrations: List<DataMigration<Preferences>> = listOf(),
    ): PreferenceStore {
        return PreferenceDataStoreFactory.createWithPath(
            migrations = migrations,
            produceFile = { preferencesDirectory.resolve("$name.preferences_pb") },
        )
            .let { DataStorePreferenceStore(it) }
    }

    companion object {
        private const val DEFAULT_NAME = "mihon"
    }
}
