package uz.asos.feature.auth.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.asos.feature.auth.data.DefaultEmailAuthRepository
import uz.asos.feature.auth.data.EmailAuthRepository
import uz.asos.feature.auth.data.network.SignInApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthDataModule {

    @Binds
    @Singleton
    abstract fun bindsEmailAuthRepository(impl: DefaultEmailAuthRepository): EmailAuthRepository

    companion object {
        @Provides
        @Singleton
        fun providesSignInApi(retrofit: Retrofit): SignInApi = retrofit.create(SignInApi::class.java)
    }
}
