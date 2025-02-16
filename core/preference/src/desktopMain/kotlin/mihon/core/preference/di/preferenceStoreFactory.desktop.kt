package mihon.core.preference.di

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import ca.gosyer.appdirs.AppDirs
import mihon.core.preference.PreferenceStore
import mihon.core.preference.PreferenceStoreFactory
import mihon.core.preference.internal.Constants
import mihon.core.preference.internal.DataStorePreferenceStore
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope

internal actual fun Scope.preferenceStoreFactory(): PreferenceStoreFactory {
    return PreferenceStoreFactoryImpl()
}

private class PreferenceStoreFactoryImpl: PreferenceStoreFactory {
    val preferencesDir = AppDirs("Mihon", "Mihon")
        .getUserConfigDir(roaming = true)
        .toPath()

    override fun default(): PreferenceStore {
        return internalGet(Constants.PREFERENCES_FILE_NAME)
    }

    override fun get(name: String): PreferenceStore {
        require(name != Constants.PREFERENCES_FILE_NAME) {
            "Custom preference store name can't be '${Constants.PREFERENCES_FILE_NAME}'"
        }
        return internalGet(name)
    }

    private fun internalGet(
        name: String,
        migrations: List<DataMigration<Preferences>> = listOf(),
    ): PreferenceStore {
        return createPreferencesDataStore(migrations = migrations) {
            preferencesDir.resolve("$name.pb")
        }
            .let { DataStorePreferenceStore(it) }
    }
}
