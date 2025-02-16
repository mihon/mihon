package mihon.core.preference.di

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import mihon.core.preference.PreferenceStore
import mihon.core.preference.PreferenceStoreFactory
import mihon.core.preference.internal.Constants
import mihon.core.preference.internal.DataStorePreferenceStore
import okio.Path.Companion.toOkioPath
import org.koin.core.scope.Scope

internal actual fun Scope.preferenceStoreFactory(): PreferenceStoreFactory {
    return PreferenceStoreFactoryImpl(get<Context>())
}

private class PreferenceStoreFactoryImpl(context: Context): PreferenceStoreFactory {
    val preferencesDir = context.filesDir
        .resolve("preferences")
        .apply { mkdirs() }
        .toOkioPath()

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
