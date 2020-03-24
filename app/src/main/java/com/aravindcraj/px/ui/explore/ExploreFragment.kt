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
import com.aravindcraj.px.utils.gone
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
                fetchPhotos(
                    query.text.toString().trim()
                )
            }
        }

        binding.photosList.adapter = adapter

        exploreViewModel.apply {
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

        startSearching()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
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
        binding.startExploring.gone()
    }

    private fun startSearching() {
        binding.startExploring.show()
        showLoading(false)
    }

    private fun noPhotosFound() {
        binding.noPhotosFound.show()
    }

    private fun showLoading(flag: Boolean) {
        if (flag) {
            with(binding) {
                isLoading.show()
                photosList.hide()
            }
        } else {
            with(binding) {
                isLoading.hide()
                photosList.show()
            }
        }
    }
}
