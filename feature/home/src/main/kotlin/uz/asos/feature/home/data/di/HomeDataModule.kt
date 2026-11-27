package uz.asos.feature.home.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.asos.feature.home.data.DefaultHomeRepository
import uz.asos.feature.home.data.HomeRepository
import uz.asos.feature.home.data.network.HomeFeedApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HomeDataModule {

    @Binds
    @Singleton
    abstract fun bindsHomeRepository(impl: DefaultHomeRepository): HomeRepository

    companion object {
        @Provides
        @Singleton
        fun providesHomeFeedApi(retrofit: Retrofit): HomeFeedApi = retrofit.create(HomeFeedApi::class.java)
    }
}
