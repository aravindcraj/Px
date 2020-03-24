package com.aravindcraj.px.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aravindcraj.px.adapters.PhotoAdapter
import com.aravindcraj.px.databinding.FragmentFavouritesBinding
import org.koin.android.ext.android.inject

class FavouritesFragment : Fragment() {
    private val favouritesViewModel: FavouritesViewModel by inject()
    private lateinit var binding: FragmentFavouritesBinding
    private val adapter = PhotoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavouritesBinding.inflate(
            layoutInflater
        )

        initAdapter()
        return binding.root
    }

    private fun initAdapter() {
        binding.savedList.adapter = adapter

        favouritesViewModel.getSavedPhotos().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}
