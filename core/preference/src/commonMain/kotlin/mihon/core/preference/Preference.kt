/*
 * Copyright (C) 2025 AntsyLich and The Mihon Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mihon.core.preference

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration.Companion.seconds

interface Preference<T> {

    val key: String

    fun get(): T

    fun set(value: T)

    fun getFlow(): Flow<T>

    fun getStateFlow(scope: CoroutineScope, started: SharingStarted = defaultStarted): StateFlow<T>

    fun isSet(): Boolean

    fun delete()
}

private val defaultStarted = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds)
