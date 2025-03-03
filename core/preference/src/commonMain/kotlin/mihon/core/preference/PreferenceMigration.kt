package mihon.core.preference

interface PreferenceMigration {
    fun shouldMigrate(currentData: Preferences): Boolean

    fun migrate(currentData: Preferences): Preferences
}
