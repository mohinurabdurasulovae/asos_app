/*
 * DefaultUserRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Implementation of UserRepository. The reader text size lives on the device (DataStore).
 * TODO(WP-11): also send it with PATCH /v1/me { textSize } when signed in.
 */
package uz.asos.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.datastore.UserPreferencesDataSource
import uz.asos.core.model.user.ReaderTextSize
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    private val preferences: UserPreferencesDataSource,
) : UserRepository {

    override val readerTextSize: Flow<ReaderTextSize> = preferences.readerTextSize

    override suspend fun setReaderTextSize(size: ReaderTextSize) {
        preferences.setReaderTextSize(size)
    }
}
