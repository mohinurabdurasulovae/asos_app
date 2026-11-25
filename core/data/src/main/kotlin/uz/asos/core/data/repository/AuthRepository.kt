/*
 * AuthRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Interface. Login/logout, session state. Feature ViewModels depend on this interface only.
 *
 * Implemented: session state (isSignedIn). Still to add (WP-03): requestCode, verify,
 * oauth, logout (the POST /v1/auth endpoints), which save / clear the token in TokenStore.
 */
package uz.asos.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    /** true while an access token is stored. Emits again on sign-in / sign-out / expiry. */
    val isSignedIn: Flow<Boolean>
}
