package com.aravindcraj.px.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.aravindcraj.px.data.models.Photo
import com.aravindcraj.px.data.models.SearchPhotosResult
import com.aravindcraj.px.data.repository.PxRepository

class ExploreViewModel(
    private val pxRepository: PxRepository
) : ViewModel() {
    private val _state = MutableLiveData<ExploreState>()
    val state: LiveData<ExploreState> = _state

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery
    private val searchResult: LiveData<SearchPhotosResult> = Transformations.map(searchQuery) {
        pxRepository.searchPhotos(it)
    }

    val photos: LiveData<PagedList<Photo>> =
        Transformations.switchMap(searchResult) {
            _state.postValue(ExploreState.HideLoading)
            it.data
        }
    val errors: LiveData<String> = Transformations.switchMap(searchResult) {
        _state.postValue(ExploreState.HideLoading)
        it.errors
    }

    fun searchPhotos(query: String) {
        _state.postValue(ExploreState.ShowLoading)
        _searchQuery.postValue(query)
    }
}