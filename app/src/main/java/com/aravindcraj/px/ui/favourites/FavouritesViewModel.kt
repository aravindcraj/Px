package com.aravindcraj.px.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.aravindcraj.px.data.models.Photo
import com.aravindcraj.px.data.repository.PxRepository

class FavouritesViewModel(
    private val pxRepository: PxRepository
) : ViewModel() {

    fun getSavedPhotos(): LiveData<PagedList<Photo>> {
        return pxRepository.getAllSavedPhotos()
    }
}