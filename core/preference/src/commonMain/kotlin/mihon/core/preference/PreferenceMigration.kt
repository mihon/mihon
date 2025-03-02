package mihon.core.preference

interface PreferenceMigration {
    suspend fun shouldMigrate(store: PreferenceStore) {

    }
}
