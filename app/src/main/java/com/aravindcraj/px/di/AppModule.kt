package com.aravindcraj.px.di

import com.aravindcraj.px.data.db.PxDatabase
import com.aravindcraj.px.data.db.PxStore
import com.aravindcraj.px.data.repository.PxRepository
import com.aravindcraj.px.network.PxService
import com.aravindcraj.px.ui.explore.ExploreViewModel
import com.aravindcraj.px.ui.favourites.FavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        PxService.create()
    }

    single {
        PxDatabase.getInstance(get()).photosDao()
    }

    single {
        PxStore(get())
    }

    single {
        PxRepository(get(), get())
    }

    viewModel {
        ExploreViewModel(get())
    }

    viewModel {
        FavouritesViewModel(get())
    }
}