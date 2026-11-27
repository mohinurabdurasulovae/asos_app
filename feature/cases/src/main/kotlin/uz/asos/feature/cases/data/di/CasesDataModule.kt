package uz.asos.feature.cases.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.asos.feature.cases.data.CasesRepository
import uz.asos.feature.cases.data.InMemoryCasesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CasesDataModule {

    @Binds
    @Singleton
    abstract fun bindsCasesRepository(impl: InMemoryCasesRepository): CasesRepository
}
