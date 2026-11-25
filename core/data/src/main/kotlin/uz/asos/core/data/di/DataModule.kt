/*
 * DataModule.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Hilt @Binds for every repository interface → implementation. Add the other repositories
 * here as they are implemented.
 */
package uz.asos.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.BookmarkRepository
import uz.asos.core.data.repository.ConstitutionRepository
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.data.repository.impl.DefaultAuthRepository
import uz.asos.core.data.repository.impl.DefaultBookmarkRepository
import uz.asos.core.data.repository.impl.DefaultConstitutionRepository
import uz.asos.core.data.repository.impl.DefaultUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsConstitutionRepository(impl: DefaultConstitutionRepository): ConstitutionRepository

    @Binds
    @Singleton
    abstract fun bindsBookmarkRepository(impl: DefaultBookmarkRepository): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(impl: DefaultUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(impl: DefaultAuthRepository): AuthRepository
}
