/*
 * FakeUserRepository.kt
 * Module: :core:testing   ·   Work package: WP-02 / WP-11
 *
 * In-memory fake of UserRepository. Sample content: ProfileSampleData.
 */
package uz.asos.core.testing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.UserProfile
import uz.asos.core.testing.data.ProfileSampleData

class FakeUserRepository : UserRepository {

    var profileResult: AsosResult<UserProfile> = AsosResult.Success(ProfileSampleData.profile)
    var updateResult: AsosResult<UserProfile?> = AsosResult.Success(null)

    val savedLocales = mutableListOf<String?>()
    val savedTextSizes = mutableListOf<ReaderTextSize?>()
    val savedNotifyDaily = mutableListOf<Boolean?>()
    var profileCalls = 0
        private set

    private val textSize = MutableStateFlow(ReaderTextSize.MEDIUM)

    override val readerTextSize: Flow<ReaderTextSize> = textSize

    override suspend fun setReaderTextSize(size: ReaderTextSize) {
        textSize.value = size
    }

    override suspend fun getProfile(): AsosResult<UserProfile> {
        profileCalls++
        return profileResult
    }

    override suspend fun updateSettings(
        locale: String?,
        textSize: ReaderTextSize?,
        notifyDaily: Boolean?,
    ): AsosResult<UserProfile?> {
        savedLocales += locale
        savedTextSizes += textSize
        savedNotifyDaily += notifyDaily
        textSize?.let { this.textSize.value = it }
        return updateResult
    }
}
