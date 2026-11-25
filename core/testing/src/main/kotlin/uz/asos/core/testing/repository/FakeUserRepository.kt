/*
 * FakeUserRepository.kt
 * Module: :core:testing   ·   Work package: WP-02
 *
 * In-memory fake of UserRepository.
 */
package uz.asos.core.testing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.model.user.ReaderTextSize

class FakeUserRepository : UserRepository {

    private val textSize = MutableStateFlow(ReaderTextSize.MEDIUM)

    override val readerTextSize: Flow<ReaderTextSize> = textSize

    override suspend fun setReaderTextSize(size: ReaderTextSize) {
        textSize.value = size
    }
}
