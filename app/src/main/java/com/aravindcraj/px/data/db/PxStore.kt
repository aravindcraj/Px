package com.aravindcraj.px.data.db

import androidx.paging.DataSource
import com.aravindcraj.px.data.db.dao.PhotosDao
import com.aravindcraj.px.data.models.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PxStore(
    private val photosDao: PhotosDao
) {

    fun insert(photos: List<Photo>, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            photosDao.insertAll(photos)
            onComplete()
        }
    }

    fun searchForPhotos(title: String): DataSource.Factory<Int, Photo> {
        val query = "%${title.replace(' ', '%')}%"
        return photosDao.getPhotosByTitle(query)
    }

    fun updatePhoto(photo: Photo, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            photosDao.insert(photo)
            onComplete()
        }
    }

    fun getAllSavedPhotos(): DataSource.Factory<Int, Photo> {
        return photosDao.getSavedPhotos()
    }
}