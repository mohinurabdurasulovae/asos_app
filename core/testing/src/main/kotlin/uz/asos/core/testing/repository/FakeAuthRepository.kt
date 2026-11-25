/*
 * FakeAuthRepository.kt
 * Module: :core:testing   ·   Work package: WP-02
 *
 * In-memory fake of AuthRepository. Call setSignedIn() to simulate signing in or out.
 */
package uz.asos.core.testing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import uz.asos.core.data.repository.AuthRepository

class FakeAuthRepository(signedIn: Boolean = false) : AuthRepository {

    private val state = MutableStateFlow(signedIn)

    override val isSignedIn: Flow<Boolean> = state

    fun setSignedIn(signedIn: Boolean) {
        state.value = signedIn
    }
}
