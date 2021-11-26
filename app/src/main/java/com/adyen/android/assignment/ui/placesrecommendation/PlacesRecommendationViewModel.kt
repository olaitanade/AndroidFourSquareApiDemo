package com.adyen.android.assignment.ui.placesrecommendation

import androidx.lifecycle.*
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.places.Place
import com.adyen.android.assignment.repository.PlaceRepository
import com.adyen.android.assignment.util.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlacesRecommendationViewModel @Inject constructor(
    private val repository: PlaceRepository
): ViewModel() {

    var currentPage = 1
    var query = VenueRecommendationsQueryBuilder()
    private val _places = MutableLiveData<ResponseResource<List<Place>>>()
    val places: LiveData<ResponseResource<List<Place>>> = _places

    fun search() {
        _places.value = ResponseResource.Loading
        viewModelScope.launch {
            val result = repository.searchPlace(query.build())
            _places.value = ResponseResource.Success(result.results)
        }
    }

}