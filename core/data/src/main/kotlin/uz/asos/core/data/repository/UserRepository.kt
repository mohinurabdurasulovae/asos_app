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
import uz.asos.core.model.user.ReaderTextSize

interface UserRepository {

    /** Text size of the official text in the Modda reader. */
    val readerTextSize: Flow<ReaderTextSize>

    suspend fun setReaderTextSize(size: ReaderTextSize)
}
