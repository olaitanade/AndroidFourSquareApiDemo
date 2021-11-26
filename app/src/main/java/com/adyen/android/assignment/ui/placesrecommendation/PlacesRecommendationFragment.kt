package com.adyen.android.assignment.ui.placesrecommendation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.FragmentPlacesRecommendationBinding
import com.adyen.android.assignment.ui.placesrecommendation.adapters.PlacesAdapter


class PlacesRecommendationFragment : Fragment() {

    private var _binding: FragmentPlacesRecommendationBinding? = null
    private val binding get() = _binding!!

    private val placesAdapter: PlacesAdapter by lazy {
        PlacesAdapter(onItemClick = { place ->

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScrollListener()
        setupObserver()

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                placesAdapter.clear()
                //sharedViewModel.searchUploaded("$text", 1)
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
//            if (!sharedViewModel.isLoading) {
//                adapter.clear()
//                sharedViewModel.refresh()
//            } else {
//                swipeRefresh.isRefreshing = false
//            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlacesRecommendationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}