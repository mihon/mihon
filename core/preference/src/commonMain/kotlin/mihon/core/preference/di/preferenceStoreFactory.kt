package mihon.core.preference.di

import mihon.core.preference.PreferenceStoreFactory
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

internal expect fun Module.preferenceStoreFactory(): KoinDefinition<PreferenceStoreFactory>
