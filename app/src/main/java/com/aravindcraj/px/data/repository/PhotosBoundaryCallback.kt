package com.aravindcraj.px.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.aravindcraj.px.data.db.PxStore
import com.aravindcraj.px.data.models.Photo
import com.aravindcraj.px.data.models.Result
import com.aravindcraj.px.data.models.SearchPhotos
import com.aravindcraj.px.network.PxService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class PhotosBoundaryCallback(
    private val query: String,
    private val pxService: PxService,
    private val store: PxStore
) : PagedList.BoundaryCallback<Photo>() {

    private var lastPageLoaded = 1
    private val _exceptions = MutableLiveData<String>()
    val exceptions: LiveData<String>
        get() = _exceptions
    private var isRequestInProgress = false

    private fun fetchDataAndSave(query: String) {
        if (isRequestInProgress) return

        val params = hashMapOf(
            "method" to "flickr.photos.search",
            "api_key" to "062a6c0c49e4de1d78497d13a7dbb360",
            "text" to query,
            "format" to "json",
            "nojsoncallback" to 1,
            "per_page" to NETWORK_PAGE_SIZE,
            "page" to lastPageLoaded
        )

        CoroutineScope(Dispatchers.IO).launch {
            val response = safeNetworkCalls(
                call = {
                    isRequestInProgress = true
                    pxService.searchPhotos(params)
                }
            )

            when (response) {
                is Result.Success<*> -> {
                    val result = response.data as SearchPhotos
                    store.insert(result.photos.photo) {
                        isRequestInProgress = false
                        lastPageLoaded++
                    }
                }

                is Result.Error -> {
                    _exceptions.postValue(response.exception.message)
                    isRequestInProgress = false
                }
            }
        }
    }

    override fun onZeroItemsLoaded() {
        fetchDataAndSave(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Photo) {
        fetchDataAndSave(query)
    }

    private suspend fun <T : Any> safeNetworkCalls(
        call: suspend () -> Response<T>
    ): Result {
        try {
            val apiResult = call.invoke()
            if (apiResult.isSuccessful) {
                apiResult.body()?.let { return Result.Success(it) }
                    ?: throw IllegalStateException("response body is empty")
            } else {
                throw HttpException(apiResult)
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}