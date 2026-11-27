/*
 * NetworkModule.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Hilt module: Json, OkHttpClient, Retrofit and every *Api interface.
 */
package uz.asos.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import uz.asos.core.network.AsosApiConfig
import uz.asos.core.network.BuildConfig
import uz.asos.core.network.api.CommunityApi
import uz.asos.core.network.api.ConstitutionApi
import uz.asos.core.network.api.DictionaryApi
import uz.asos.core.network.api.LearningApi
import uz.asos.core.network.api.LegalHelpApi
import uz.asos.core.network.api.UserApi
import uz.asos.core.network.interceptor.AuthInterceptor
import uz.asos.core.network.interceptor.LanguageInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
        coerceInputValues = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        languageInterceptor: LanguageInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(AsosApiConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(AsosApiConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(languageInterceptor)
        .addInterceptor(authInterceptor)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                        redactHeader("Authorization")
                    },
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(AsosApiConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun providesConstitutionApi(retrofit: Retrofit): ConstitutionApi =
        retrofit.create(ConstitutionApi::class.java)

    @Provides
    @Singleton
    fun providesUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun providesLearningApi(retrofit: Retrofit): LearningApi =
        retrofit.create(LearningApi::class.java)

    @Provides
    @Singleton
    fun providesLegalHelpApi(retrofit: Retrofit): LegalHelpApi =
        retrofit.create(LegalHelpApi::class.java)

    @Provides
    @Singleton
    fun providesDictionaryApi(retrofit: Retrofit): DictionaryApi =
        retrofit.create(DictionaryApi::class.java)

    @Provides
    @Singleton
    fun providesCommunityApi(retrofit: Retrofit): CommunityApi =
        retrofit.create(CommunityApi::class.java)
}
