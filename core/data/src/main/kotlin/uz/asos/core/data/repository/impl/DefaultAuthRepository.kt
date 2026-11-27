package uz.asos.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.datastore.TokenStore
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    tokenStore: TokenStore,
) : AuthRepository {

    override val isSignedIn: Flow<Boolean> = tokenStore.isSignedIn.distinctUntilChanged()
}
