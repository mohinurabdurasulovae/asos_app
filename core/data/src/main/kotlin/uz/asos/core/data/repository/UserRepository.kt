/*
 * UserRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Interface. Profile, stats, activity, settings. Feature ViewModels depend on this
 * interface only.
 *
 * Implemented: reader text size (device setting). Still to add (WP-11): profile, stats,
 * weekly activity, server-side settings (PATCH /v1/me).
 */
package uz.asos.core.data.repository

import kotlinx.coroutines.flow.Flow
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.UserProfile

interface UserRepository {

    /** Text size of the official text in the Modda reader. */
    val readerTextSize: Flow<ReaderTextSize>

    suspend fun setReaderTextSize(size: ReaderTextSize)

    /** GET /v1/me/profile — the whole Profil screen in one call. Needs a signed-in user. */
    suspend fun getProfile(): AsosResult<UserProfile>

    /** PATCH /v1/me — the settings rows on Profil. Only non-null fields are sent. */
    suspend fun updateSettings(
        locale: String? = null,
        textSize: ReaderTextSize? = null,
        notifyDaily: Boolean? = null,
    ): AsosResult<UserProfile?>
}
