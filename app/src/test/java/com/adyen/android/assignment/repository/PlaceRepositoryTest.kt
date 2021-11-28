package com.adyen.android.assignment.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.model.places.PlaceResponse
import com.adyen.android.assignment.ui.placesrecommendation.PlacesRecommendationViewModel
import com.adyen.android.assignment.util.FakeDataSource
import com.adyen.android.assignment.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class PlaceRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var placeRepository: PlaceRepository

    @MockK
    lateinit var placesService: PlacesService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        placeRepository = PlaceRepository(placesService)
    }

    @Test
    fun `search successful`() = runBlocking{

        coEvery {
            placesService.getPlaceRecommendation(FakeDataSource.query.build())
        } returns FakeDataSource.serviceResponse

        val result = placeRepository.searchPlace(FakeDataSource.query.build())

        assert(result is PlaceResponse)
    }


    @Test
    fun `search successful doesn't have cursor for next page`() = runBlocking{
        coEvery {
            placesService.getPlaceRecommendation(FakeDataSource.query.build())
        } returns FakeDataSource.serviceResponseWithoutCursor

        val result = placeRepository.searchPlace(FakeDataSource.query.build())

        assert((result as PlaceResponse).cursor == null)
    }

}