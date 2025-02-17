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
import com.russhwolf.settings.Settings
import mihon.core.preference.PreferenceStore
import mihon.core.preference.PreferenceStoreFactory

class PreferenceStoreFactoryImpl(private val delegate: Settings.Factory) : PreferenceStoreFactory {
    override fun default(): PreferenceStore {
        return create(DEFAULT_PREFERENCES_NAME)
    }

    override fun get(name: String): PreferenceStore {
        require(name != DEFAULT_PREFERENCES_NAME) {
            "Preference name needs to be something other than '${DEFAULT_PREFERENCES_NAME}'"
        }
        return create(name)
    }

    private fun create(name: String): PreferenceStore {
        val settings = delegate.create(name)
        return PreferenceStoreImpl(settings as ObservableSettings)
    }

    companion object {
        // For compatibility reasons, forks should not change the default preference name.
        private const val DEFAULT_PREFERENCES_NAME = "mihon"
    }
}
