/*
 * UserPreferencesDataSource.kt
 * Module: :core:datastore   ·   Work package: WP-02
 *
 * Reads/writes user preferences stored on the device.
 *
 * Implemented: reader text size (used by the Modda reader's "Aa" button).
 * Still to add: daily reminders on/off, language, onboarding done.
 */
package uz.asos.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.asos.core.datastore.di.UserPreferencesStore
import uz.asos.core.model.user.ReaderTextSize
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @UserPreferencesStore private val dataStore: DataStore<Preferences>,
) {
    private val textSizeKey = stringPreferencesKey("reader_text_size")

    val readerTextSize: Flow<ReaderTextSize> =
        dataStore.data.map { ReaderTextSize.fromApi(it[textSizeKey]) }

    suspend fun setReaderTextSize(size: ReaderTextSize) {
        dataStore.edit { it[textSizeKey] = size.apiValue }
    }
}
