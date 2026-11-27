/*
 * DefaultUserRepository.kt
 * Module: :core:data   ·   Work package: WP-02 / WP-11
 *
 * Implementation of UserRepository. The reader text size lives on the device (DataStore) and
 * is mirrored to the server with PATCH /v1/me when the user is signed in.
 */
package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.mapper.asModel
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.datastore.UserPreferencesDataSource
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.UserProfile
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.UserApi
import uz.asos.core.network.dto.UpdateMeRequestDto
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    private val preferences: UserPreferencesDataSource,
    private val userApi: UserApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

    override val readerTextSize: Flow<ReaderTextSize> = preferences.readerTextSize

    override suspend fun setReaderTextSize(size: ReaderTextSize) {
        preferences.setReaderTextSize(size)
    }

    override suspend fun getProfile(): AsosResult<UserProfile> = io {
        apiCaller.call { userApi.getProfile() }.mapSuccess { it.asModel() }
    }

    override suspend fun updateSettings(
        locale: String?,
        textSize: ReaderTextSize?,
        notifyDaily: Boolean?,
    ): AsosResult<UserProfile?> = io {
        // The reader size is a device setting first, so it applies even when the call fails.
        textSize?.let { preferences.setReaderTextSize(it) }

        apiCaller.call {
            userApi.updateMe(
                UpdateMeRequestDto(
                    locale = locale,
                    textSize = textSize?.apiValue,
                    notifyDaily = notifyDaily,
                ),
            )
        }.mapSuccess { null }
    }

    private suspend fun <T> io(block: suspend () -> T): T = withContext(ioDispatcher) { block() }
}
