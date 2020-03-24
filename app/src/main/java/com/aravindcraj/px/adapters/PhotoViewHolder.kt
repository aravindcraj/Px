package com.aravindcraj.px.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.aravindcraj.px.R
import com.aravindcraj.px.data.models.Photo
import com.aravindcraj.px.data.repository.PxRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view), KoinComponent {

    private val image: ImageView = view.findViewById(R.id.image)
    private val title: AppCompatTextView = view.findViewById(R.id.title)
    private val isFavourite: AppCompatImageView = view.findViewById(R.id.is_favourite)
    private var photo: Photo? = null
    private val pxRepository: PxRepository by inject()

    init {
        isFavourite.setOnClickListener {
            val photo = it.tag as Photo
            photo.isSaved = !photo.isSaved
            pxRepository.markPhotoAsSaved(photo) {
                setSavedImage(photo.isSaved)
            }
        }
    }

    fun bind(photo: Photo?) {
        if (photo != null) {
            image.load(photoUrl(photo)) {
                crossfade(true)
            }
            title.text = photo.title
            setSavedImage(photo.isSaved)
            isFavourite.tag = photo
        }
    }

    private fun setSavedImage(isSaved: Boolean) {
        if (isSaved)
            isFavourite.setImageResource(R.drawable.ic_favorite_24dp)
        else
            isFavourite.setImageResource(
                R.drawable.ic_not_favorite_border_24dp
            )
    }

    private fun photoUrl(photo: Photo): String {
        return "https://farm${photo.farm}.staticflickr.com/" +
                "${photo.server}/${photo.id}_${photo.secret}_m.jpg"
    }

    companion object {
        fun create(parent: ViewGroup): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_explore_item, parent, false)
            return PhotoViewHolder(view)
        }
    }
}