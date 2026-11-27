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
