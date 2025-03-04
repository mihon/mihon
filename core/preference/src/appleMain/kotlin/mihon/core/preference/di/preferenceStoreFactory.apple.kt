package mihon.core.preference.di

import kotlinx.cinterop.ExperimentalForeignApi
import mihon.core.preference.Preferences
import mihon.core.preference.datastore.DataStorePreferencesFactory
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual fun Scope.preferenceDirectory(): Path {
    @OptIn(ExperimentalForeignApi::class)
    return NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!
        .path!!
        .toPath()
        .resolve("preferences")
}
