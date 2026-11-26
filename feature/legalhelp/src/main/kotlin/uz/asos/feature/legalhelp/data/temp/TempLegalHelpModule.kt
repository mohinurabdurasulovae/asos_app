/*
 * TempLegalHelpModule.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * TODO(WP-02): TEMPORARY Hilt binding for the in-memory repository. Delete together with
 * the rest of data/temp/ when core:data binds the real LegalHelpRepository.
 */
package uz.asos.feature.legalhelp.data.temp

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TempLegalHelpModule {

    @Binds
    @Singleton
    abstract fun bindsTempLegalHelpRepository(impl: InMemoryLegalHelpRepository): TempLegalHelpRepository
}
