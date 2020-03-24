package com.aravindcraj.px.data.models

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class SearchPhotosResult(
    val data: LiveData<PagedList<Photo>>,
    val errors: LiveData<String>
)