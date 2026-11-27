package uz.asos.core.data.repository

import kotlinx.coroutines.flow.Flow
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.UserProfile

interface UserRepository {

    val readerTextSize: Flow<ReaderTextSize>

    suspend fun setReaderTextSize(size: ReaderTextSize)

    suspend fun getProfile(): AsosResult<UserProfile>

    suspend fun updateSettings(
        locale: String? = null,
        textSize: ReaderTextSize? = null,
        notifyDaily: Boolean? = null,
    ): AsosResult<UserProfile?>
}
