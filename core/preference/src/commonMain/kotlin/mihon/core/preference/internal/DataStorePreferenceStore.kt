package mihon.core.preference.internal

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.MutableStateFlow
import mihon.core.preference.Preference
import mihon.core.preference.PreferenceStore

class DataStorePreferenceStore : PreferenceStore {
    override fun getString(key: String, defaultValue: String): Preference<String> {
        return DataStorePreference(
            MutableStateFlow(),
            stringPreferencesKey(key),
            defaultValue
        )
    }

    override fun getLong(key: String, defaultValue: Long): Preference<Long> {
        TODO("Not yet implemented")
    }

    override fun getInt(key: String, defaultValue: Int): Preference<Int> {
        TODO("Not yet implemented")
    }

    override fun getFloat(key: String, defaultValue: Float): Preference<Float> {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getStringSet(key: String, defaultValue: Set<String>): Preference<Set<String>> {
        TODO("Not yet implemented")
    }

    override fun <T> getObject(
        key: String,
        defaultValue: T,
        serializer: (T) -> String,
        deserializer: (String) -> T,
    ): Preference<T> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Map<String, *> {
        TODO("Not yet implemented")
    }
}
