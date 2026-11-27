package uz.asos.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import uz.asos.core.datastore.di.SessionStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStore @Inject constructor(
    @SessionStore private val dataStore: DataStore<Preferences>,
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expiresAtKey = stringPreferencesKey("expires_at")

    @Volatile
    private var cached: String? = null

    @Volatile
    private var loaded = false

    val accessToken: Flow<String?> = dataStore.data.map { it[accessTokenKey] }

    val isSignedIn: Flow<Boolean> = accessToken.map { !it.isNullOrBlank() }

    suspend fun current(): String? {
        if (!loaded) {
            cached = dataStore.data.first()[accessTokenKey]
            loaded = true
        }
        return cached
    }

    suspend fun save(accessToken: String, expiresAt: String?) {
        dataStore.edit { prefs ->
            prefs[accessTokenKey] = accessToken
            if (expiresAt != null) prefs[expiresAtKey] = expiresAt else prefs.remove(expiresAtKey)
        }
        cached = accessToken
        loaded = true
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(expiresAtKey)
        }
        cached = null
        loaded = true
    }
}
