package mihon.core.preference.di

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import mihon.core.preference.PreferenceStore
import mihon.core.preference.PreferenceStoreFactory
import mihon.core.preference.internal.Constants
import mihon.core.preference.internal.DataStorePreferenceStore
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual fun Scope.preferenceStoreFactory(): PreferenceStoreFactory {
    return PreferenceStoreFactoryImpl()
}

private class PreferenceStoreFactoryImpl: PreferenceStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    val preferencesDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
        ?.path
        ?.toPath()
        ?.resolve("preferences")!!
        .also { FileSystem.SYSTEM.createDirectories(it, mustCreate = false) }

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
