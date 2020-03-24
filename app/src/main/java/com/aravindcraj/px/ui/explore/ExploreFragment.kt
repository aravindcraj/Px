package com.aravindcraj.px.ui.explore

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.aravindcraj.px.adapters.PhotoAdapter
import com.aravindcraj.px.data.models.Photo
import com.aravindcraj.px.databinding.FragmentExploreBinding
import com.aravindcraj.px.utils.hide
import com.aravindcraj.px.utils.hideKeyboard
import com.aravindcraj.px.utils.show
import org.koin.android.ext.android.inject

class ExploreFragment : Fragment() {
    private val exploreViewModel: ExploreViewModel by inject()
    private lateinit var binding: FragmentExploreBinding
    private val adapter = PhotoAdapter()
    private var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExploreBinding.inflate(
            layoutInflater
        )

        init()

        return binding.root
    }

    private fun init() {
        binding.query.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                fetchPhotos(view.text.toString().trim())
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.query.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                fetchPhotos(
                    binding.query.text.toString().trim()
                )
                true
            } else {
                false
            }
        }

        binding.apply {
            search.setOnClickListener {
                exploreViewModel.searchPhotos(
                    query.text.toString().trim()
                )
            }
        }

        binding.photosList.adapter = adapter
        exploreViewModel.apply {
            searchPhotos(searchQuery.value ?: DEFAULT_QUERY)

            photos.observe(viewLifecycleOwner, Observer<PagedList<Photo>> {
                if (it?.size == 0 && !isLoading) {
                    noPhotosFound()
                    showLoading(false)
                } else if (it?.size == 0 && isLoading) {
                    showLoading(true)
                } else {
                    showLoading(false)
                    adapter.submitList(it)
                }
            })

            searchQuery.observe(viewLifecycleOwner, Observer {
                binding.query.setText(it)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ExploreState.StartSearching -> {
                    startSearching()
                }

                is ExploreState.ShowLoading -> {
                    showLoading(true)
                }

                is ExploreState.OnPhotosFetched -> {
                    adapter.submitList(state.photos)
                }
            }
        })
    }

    private fun fetchPhotos(query: String) {
        exploreViewModel.searchPhotos(query)
    }

    private fun startSearching() {

    }

    private fun noPhotosFound() {

    }

    private fun showLoading(flag: Boolean) {
        if (flag) {
            binding.isLoading.show()
            binding.photosList.hide()
        } else {
            binding.isLoading.hide()
            binding.photosList.show()
        }
    }

    companion object {
        private const val DEFAULT_QUERY = "nature"
    }
}
