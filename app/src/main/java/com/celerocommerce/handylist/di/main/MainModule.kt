package com.celerocommerce.handylist.di.main

import com.celerocommerce.handylist.network.main.CeleroApiMainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {

    @ActivityRetainedScoped
    @Provides
    fun provideCeleroApiMainService(retrofit: Retrofit): CeleroApiMainService {
        return retrofit.create(CeleroApiMainService::class.java)
    }
}