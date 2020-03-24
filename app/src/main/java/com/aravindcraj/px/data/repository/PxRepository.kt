package com.aravindcraj.px.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aravindcraj.px.data.db.PxStore
import com.aravindcraj.px.data.models.Photo
import com.aravindcraj.px.data.models.SearchPhotosResult
import com.aravindcraj.px.network.PxService

class PxRepository(
    private val pxService: PxService,
    private val store: PxStore
) {

    fun searchPhotos(
        query: String
    ): SearchPhotosResult {
        val dataSourceFactory = store.searchForPhotos(query)
        val boundaryCallback = PhotosBoundaryCallback(query, pxService, store)
        val networkExceptions = boundaryCallback.exceptions

        val config = PagedList.Config.Builder()
            .setPageSize(DB_PAGE_SIZE)
            .setInitialLoadSizeHint(2 * DB_PAGE_SIZE)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(true)
            .build()

        val data = LivePagedListBuilder(
            dataSourceFactory, config
        ).setBoundaryCallback(boundaryCallback).build()

        return SearchPhotosResult(data, networkExceptions)
    }

    fun markPhotoAsSaved(photo: Photo, onComplete: () -> Unit) {
        store.updatePhoto(photo) {
            onComplete()
        }
    }

    fun getAllSavedPhotos(): LiveData<PagedList<Photo>> {
        val dataSourceFactory = store.getAllSavedPhotos()
        val config = PagedList.Config.Builder()
            .setPageSize(DB_PAGE_SIZE)
            .setInitialLoadSizeHint(2 * DB_PAGE_SIZE)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(true)
            .build()

        return LivePagedListBuilder(
            dataSourceFactory, config
        ).build()
    }

    companion object {
        const val DB_PAGE_SIZE = 10
    }
}