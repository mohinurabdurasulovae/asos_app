package uz.asos.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val isSignedIn: Flow<Boolean>
}
