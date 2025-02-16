package mihon.core.preference.di

import mihon.core.preference.PreferenceStore
import mihon.core.preference.PreferenceStoreFactory
import mihon.core.preference.internal.Constants
import org.koin.dsl.module

val corePreferenceModule = module {
    preferenceStoreFactory()
    single<PreferenceStore> { get<PreferenceStoreFactory>().get(Constants.DEFAULT_PREFERENCE_STORE) }
}
