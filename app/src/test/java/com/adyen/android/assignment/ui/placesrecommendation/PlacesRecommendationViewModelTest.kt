package com.adyen.android.assignment.ui.placesrecommendation


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.repository.PlaceRepository
import com.adyen.android.assignment.util.FakeDataSource
import com.adyen.android.assignment.util.MainCoroutineRule
import com.adyen.android.assignment.util.ResponseResource
import com.adyen.android.assignment.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class PlacesRecommendationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var placesRecommendationViewModel: PlacesRecommendationViewModel

    @MockK
    lateinit var placeRepository: PlaceRepository


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        placesRecommendationViewModel = PlacesRecommendationViewModel(placeRepository)
    }

    @Test
    fun `search successful`() {
        placesRecommendationViewModel.query
            .setLatitudeLongitude(1.0,1.0)


        val response = FakeDataSource.successfulPlaceResponse

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } returns response

       placesRecommendationViewModel.search()

        val observeResult = placesRecommendationViewModel.places.getOrAwaitValue()
        assert(observeResult is ResponseResource.Success)
        assert(placesRecommendationViewModel.currentPage == 1)
    }

    @Test
    fun `search successful with a link header for next page`() {
        placesRecommendationViewModel.query
            .setLatitudeLongitude(1.0,1.0)

        val response = FakeDataSource.successfulPlaceResponse

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } returns response

        placesRecommendationViewModel.search()
        assert(placesRecommendationViewModel.canFetchMore != null)
        assert(placesRecommendationViewModel.currentPage == 1)
    }

    @Test
    fun `search successful without a link header for next page`() {
        placesRecommendationViewModel.query
            .setLatitudeLongitude(1.0,1.0)


        val response = FakeDataSource.successfulPlaceResponseWithoutLink

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } returns response

        placesRecommendationViewModel.search()

        assert(placesRecommendationViewModel.canFetchMore == null)
        assert(placesRecommendationViewModel.currentPage == 1)
    }

    @Test
    fun `search unsuccessful`() {

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        }.throws(Exception("Enter latitude and longitude"))

        placesRecommendationViewModel.search()

        val observeResult = placesRecommendationViewModel.places.getOrAwaitValue()
        assert(observeResult is ResponseResource.Error)
        assert(placesRecommendationViewModel.currentPage == 1)
    }


    @Test
    fun `next page successful`() {
        placesRecommendationViewModel.query
            .setLatitudeLongitude(1.0,1.0)


        val response = FakeDataSource.successfulPlaceResponse

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } returns response

        placesRecommendationViewModel.nextPage(1.0,1.0)

        val observeResult = placesRecommendationViewModel.places.getOrAwaitValue()

        assert(observeResult is ResponseResource.Success)
        assert(placesRecommendationViewModel.currentPage == 2)
    }

    @Test
    fun `next page unsuccessful`() {

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } .throws(Exception("Enter latitude and longitude"))

        placesRecommendationViewModel.nextPage(1.0,1.0)

        val observeResult = placesRecommendationViewModel.places.getOrAwaitValue()

        assert(observeResult is ResponseResource.Error)
        assert(placesRecommendationViewModel.currentPage == 1)
    }

    @Test
    fun `next page successful with a link header for next page`() {
        placesRecommendationViewModel.query
            .setLatitudeLongitude(1.0,1.0)

        val response = FakeDataSource.successfulPlaceResponse

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } returns response

        placesRecommendationViewModel.nextPage(1.0,1.0)

        assert(placesRecommendationViewModel.canFetchMore != null)
        assert(placesRecommendationViewModel.currentPage == 2)
    }

    @Test
    fun `next page successful without a link header for next page`() {
        placesRecommendationViewModel.query
            .setLatitudeLongitude(1.0,1.0)


        val response = FakeDataSource.successfulPlaceResponseWithoutLink

        coEvery {
            placeRepository.searchPlace(placesRecommendationViewModel.query.build())
        } returns response

        placesRecommendationViewModel.nextPage(1.0,1.0)

        assert(placesRecommendationViewModel.canFetchMore == null)
        assert(placesRecommendationViewModel.currentPage == 2)
    }
}