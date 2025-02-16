package mihon.core.preference.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import mihon.core.preference.PreferenceStoreFactory
import okio.Path
import org.koin.core.scope.Scope

internal expect fun Scope.preferenceStoreFactory(): PreferenceStoreFactory

internal fun createPreferencesDataStore(
    migrations: List<DataMigration<Preferences>> = listOf(),
    produceFile: () -> Path,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        migrations = migrations,
        produceFile = produceFile
    )
}


