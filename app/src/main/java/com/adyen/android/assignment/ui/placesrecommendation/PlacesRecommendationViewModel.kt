package com.adyen.android.assignment.ui.placesrecommendation

import androidx.lifecycle.*
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.places.Place
import com.adyen.android.assignment.repository.PlaceRepository
import com.adyen.android.assignment.util.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class PlacesRecommendationViewModel @Inject constructor(
    private val repository: PlaceRepository
): ViewModel() {

    var currentPage = 1
    var canFetchMore: String? = null
    var query = VenueRecommendationsQueryBuilder()
    private val _places = MutableLiveData<ResponseResource<List<Place>>>()
    val places: LiveData<ResponseResource<List<Place>>> = _places

    private val responseErrorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.d(throwable.message)
        _places.value = ResponseResource.Error(throwable.message)
    }

    fun search() {
        currentPage = 1
        canFetchMore = null
        _places.value = ResponseResource.Loading
        viewModelScope.launch(responseErrorHandler) {
            val result = repository.searchPlace(query.build())
            result?.let {
                _places.value = ResponseResource.Success(it.results)
                it.cursor?.let {
                    canFetchMore = it
                }

            }

        }
    }

    fun nextPage(long: Double, lat: Double) {
        query
            .setLatitudeLongitude(lat,long)
            .setCursor(canFetchMore)

        canFetchMore = null
        _places.value = ResponseResource.Loading
        viewModelScope.launch(responseErrorHandler) {
            val result = repository.searchPlace(query.build())
            result?.let {
                currentPage++
                _places.value = ResponseResource.Success(it.results)
                it.cursor?.let {
                    canFetchMore = it
                }
            }

        }
    }

}