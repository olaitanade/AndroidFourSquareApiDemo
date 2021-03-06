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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.databinding.FragmentPlacesRecommendationBinding
import com.adyen.android.assignment.ui.placesrecommendation.adapters.PlacesAdapter
import com.adyen.android.assignment.ui.placesrecommendation.filterDialog.FilterDialog
import com.adyen.android.assignment.util.DebouncingQueryTextListener
import com.adyen.android.assignment.util.ResponseResource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class PlacesRecommendationFragment : Fragment(), FilterDialog.FilterListener {

    private var _binding: FragmentPlacesRecommendationBinding? = null
    private val binding get() = _binding!!

    private val placesRecommendationViewModel: PlacesRecommendationViewModel by viewModels()

    private val placesAdapter: PlacesAdapter by lazy {
        PlacesAdapter(onItemClick = { place ->
            Timber.d("Show Place Details")
        })
    }

    private val permissions: List<String> by lazy {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.N)
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            -> {
                getLocationPlaces()

            }else -> {
            // No location access granted.
        }
        }
    }

    private fun getLocationPlaces(
        searchQuery:String? = null,
        filter: Map<String,String> = placesRecommendationViewModel.query.build(),
        nextPage: Boolean = false){

        when {
            permissions.all {  ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }
             -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        location?.let {

                            if(nextPage){
                                placesRecommendationViewModel.nextPage(it.longitude,it.latitude)
                            }else{
                                placesRecommendationViewModel.query.setLatitudeLongitude(it.latitude, it.longitude)
                                    .setQuery(searchQuery)
                                    .setRadius(filter["radius"])
                                    .setSort(filter["sort"])
                                    .setNe(filter["ne"])
                                    .setSw(filter["sw"])
                                    .setNear(filter["near"])
                                    .setCategories(filter["categories"])

                                placesRecommendationViewModel.search()
                            }

                        }

                    }

            }
            else -> {
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
        initialize()
    }

    private fun initialize(){
        binding.placeList.apply {
            adapter = placesAdapter
        }

        binding.search.addTextChangedListener(
            DebouncingQueryTextListener(
                this@PlacesRecommendationFragment.lifecycle
            ) { newText ->

                newText?.let {
                    getLocationPlaces(searchQuery = it)
                }

            })

        binding.swipeRefresh.setOnRefreshListener {
            placesAdapter.clear()
            getLocationPlaces()

        }

        binding.filter.setOnClickListener {
            val fragmentDialog = FilterDialog.getInstance(placesRecommendationViewModel.query)
            fragmentDialog.show(childFragmentManager, "FilterDialog")
        }

        getLocationPlaces()
    }

    private fun setupObserver(){
        placesRecommendationViewModel.places.observe(viewLifecycleOwner,{ resource ->
            when(resource){
                is ResponseResource.Success -> {
                    if (placesRecommendationViewModel.currentPage == 1) {
                        placesAdapter.setData(resource.data)
                    } else {
                        placesAdapter.removeLoading()
                        placesAdapter.updateData(resource.data)
                    }
                    binding.loading.isVisible=false
                    binding.swipeRefresh.isRefreshing = false
                }
                is ResponseResource.Loading -> {
                    if(placesAdapter.places.isNotEmpty()){
                        placesAdapter.places.add(null)
                        binding.placeList.post {
                            placesAdapter.notifyItemInserted(placesAdapter.places.size - 1)
                        }
                    }

                    binding.loading.isVisible=true
                }
                is ResponseResource.Error -> {
                    Timber.d(resource.exception)

                    binding.loading.isVisible=false
                    binding.swipeRefresh.isRefreshing = false
                    if (placesAdapter.places.isNotEmpty()) {
                        placesAdapter.removeLoading()
                    }
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

                    if (!binding.loading.isVisible && currentTotalCount <= lastItem + visibleThreshold) {
                        if (!placesRecommendationViewModel.canFetchMore.isNullOrEmpty()) {
                            getLocationPlaces(nextPage = true)
                        }
                    }
                }
            }
        })
    }

    override fun apply(filter: VenueRecommendationsQueryBuilder) {
        getLocationPlaces(searchQuery = binding.search.text.toString(),filter = filter.build())
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