package mihon.core.preference.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mihon.core.preference.Preference
import mihon.core.preference.PreferenceStore

class DataStorePreferenceStore(private val store: DataStore<Preferences>)  : PreferenceStore {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dataStateFlow = store.data.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = runBlocking(scope.coroutineContext) { store.data.first() }
    )

    override fun getString(key: String, defaultValue: String): Preference<String> {
        val preferencesKey = stringPreferencesKey(key)
        return DataStorePreference(dataStateFlow, preferencesKey, defaultValue, edit(preferencesKey))
    }

    override fun getLong(key: String, defaultValue: Long): Preference<Long> {
        val preferencesKey = longPreferencesKey(key)
        return DataStorePreference(dataStateFlow, preferencesKey, defaultValue, edit(preferencesKey))
    }

    override fun getInt(key: String, defaultValue: Int): Preference<Int> {
        val preferencesKey = intPreferencesKey(key)
        return DataStorePreference(dataStateFlow, preferencesKey, defaultValue, edit(preferencesKey))
    }

    override fun getFloat(key: String, defaultValue: Float): Preference<Float> {
        val preferencesKey = floatPreferencesKey(key)
        return DataStorePreference(dataStateFlow, preferencesKey, defaultValue, edit(preferencesKey))
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> {
        val preferencesKey = booleanPreferencesKey(key)
        return DataStorePreference(dataStateFlow, preferencesKey, defaultValue, edit(preferencesKey))
    }

    override fun getStringSet(key: String, defaultValue: Set<String>): Preference<Set<String>> {
        val preferencesKey = stringSetPreferencesKey(key)
        return DataStorePreference(dataStateFlow, preferencesKey, defaultValue, edit(preferencesKey))
    }

    override fun <T> getObject(
        key: String,
        defaultValue: T,
        serializer: (T) -> String,
        deserializer: (String) -> T,
    ): Preference<T> {
        TODO("Not yet implemented")
    }

    private fun <T> edit(key: Preferences.Key<T>): (T?) -> Unit {
        return { value ->
            scope.launch {
                store.edit {
                    if (value == null) {
                        it.remove(key)
                    } else {
                        it[key] = value
                    }
                }
            }
        }

    }

    override fun getAll(): Map<String, Any> {
        return dataStateFlow.value.asMap().mapKeys { (key) -> key.name }
    }
}
