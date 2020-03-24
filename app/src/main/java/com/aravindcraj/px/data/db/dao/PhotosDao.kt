package com.aravindcraj.px.data.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.aravindcraj.px.data.models.Photo

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photo: List<Photo>)

    @Query("SELECT * FROM photos WHERE id LIKE :id")
    fun photoById(id: String): LiveData<Photo>

    @Query("SELECT * from photos WHERE title LIKE :query")
    fun getPhotosByTitle(query: String): DataSource.Factory<Int, Photo>

    @Query("SELECT * from photos WHERE isSaved IS 1")
    fun getSavedPhotos(): DataSource.Factory<Int, Photo>

    @Query("SELECT * from photos")
    fun getAllPhotos(): DataSource.Factory<Int, Photo>

    @Delete
    fun deletePhoto(photo: Photo)

    @Query("DELETE FROM photos")
    fun deleteAll()
}