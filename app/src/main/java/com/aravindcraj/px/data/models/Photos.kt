package com.aravindcraj.px.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: String,
    val photo: List<Photo>
)

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey @Json(name = "id")
    val id: String,
    @Json(name = "owner")
    val owner: String,
    @Json(name = "secret")
    val secret: String,
    @Json(name = "server")
    val server: String,
    @Json(name = "farm")
    val farm: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "ispublic")
    val isPublic: Int,
    @Json(name = "isfriend")
    val isFriend: Int,
    @Json(name = "isfamily")
    val isFamily: Int,
    @Json(name = "is_saved")
    var isSaved: Boolean = false
)