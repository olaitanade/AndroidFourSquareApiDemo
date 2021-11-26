package com.adyen.android.assignment.ui.placesrecommendation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.databinding.FragmentPlacesRecommendationBinding
import com.adyen.android.assignment.ui.placesrecommendation.adapters.PlacesAdapter
import com.adyen.android.assignment.util.ResponseResource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class PlacesRecommendationFragment : Fragment() {

    private var _binding: FragmentPlacesRecommendationBinding? = null
    private val binding get() = _binding!!

    private val placesRecommendationViewModel: PlacesRecommendationViewModel by viewModels()

    private val placesAdapter: PlacesAdapter by lazy {
        PlacesAdapter(onItemClick = { place ->

        })
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.N)
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                getLocationPlaces()

            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                getLocationPlaces()
            } else -> {
            // No location access granted.
        }
        }
    }

    private fun getLocationPlaces(){
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // You can use the API that requires the permission.
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                        location?.let {
                            placesRecommendationViewModel.search(
                                VenueRecommendationsQueryBuilder()
                                    .setLatitudeLongitude(it.latitude, it.longitude)
                                    .build()
                            )
                        }

                    }

            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    locationPermissionRequest.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScrollListener()
        setupObserver()


        binding.placeList.apply {
            adapter = placesAdapter
        }
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
            placesAdapter.clear()
            getLocationPlaces()

        }

        getLocationPlaces()
    }

    private fun setupObserver(){
        placesRecommendationViewModel.places.observe(viewLifecycleOwner,{ resource ->
            when(resource){
                is  ResponseResource.Success -> {
                    if (placesRecommendationViewModel.currentPage == 1) {
                        placesAdapter.setData(resource.data)
                    } else {
                        placesAdapter.removeLoading()
                        placesAdapter.updateData(resource.data)
                    }
                }
                is ResponseResource.Loading -> {

                }
                is ResponseResource.Error -> {
                    Timber.d(resource.exception)
                }
            }
        })
    }

    private fun setupScrollListener() {
        binding.placeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val visibleThreshold = 1

                    val layoutManager = binding.placeList.layoutManager as LinearLayoutManager
                    val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    val currentTotalCount = layoutManager.itemCount

//                    if (!sharedViewModel.isLoading && currentTotalCount <= lastItem + visibleThreshold) {
//                        if (sharedViewModel.shouldFetchMore()) {
//                            sharedViewModel.searchUploaded(
//                                search.text(),
//                                sharedViewModel.uploadedCitizenCurrentPage + 1
//                            )
//                        }
//                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


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