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
package mihon.core.preference.di

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import ca.gosyer.appdirs.AppDirs
import mihon.core.preference.Preferences
import mihon.core.preference.datastore.DataStorePreferences
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope

internal actual fun Scope.preferenceStoreFactory(): mihon.core.preference.Preferences.Factory {
    return PreferencesFactoryImpl()
}

private class PreferencesFactoryImpl : mihon.core.preference.Preferences.Factory {
    val preferencesDir = AppDirs("Mihon", "Mihon")
        .getUserConfigDir(roaming = true)
        .toPath()

    override fun default(): mihon.core.preference.Preferences {
        return internalGet(Constants.PREFERENCES_FILE_NAME)
    }

    override fun get(name: String): mihon.core.preference.Preferences {
        require(name != Constants.PREFERENCES_FILE_NAME) {
            "Custom preference store name can't be '${Constants.PREFERENCES_FILE_NAME}'"
        }
        return internalGet(name)
    }

    private fun internalGet(
        name: String,
        migrations: List<DataMigration<Preferences>> = listOf(),
    ): mihon.core.preference.Preferences {
        return createPreferencesDataStore(migrations = migrations) {
            preferencesDir.resolve("$name.pb")
        }
            .let { DataStorePreferences(it) }
    }
}
