package com.aravindcraj.px.ui.explore

import androidx.paging.PagedList
import com.aravindcraj.px.data.models.Photo

sealed class ExploreState {
    object ShowLoading : ExploreState()
    object HideLoading : ExploreState()
    data class OnPhotosFetched(val photos: PagedList<Photo>) : ExploreState()
}