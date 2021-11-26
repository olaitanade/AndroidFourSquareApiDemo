package com.adyen.android.assignment.ui.placesrecommendation


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.places.*
import com.adyen.android.assignment.repository.PlaceRepository
import com.adyen.android.assignment.util.ResponseResource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlacesRecommendationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var placesRecommendationViewModel: PlacesRecommendationViewModel

    @MockK
    lateinit var placeRepository: PlaceRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        placesRecommendationViewModel = PlacesRecommendationViewModel(placeRepository)
    }

    @Test
    fun `search unsuccessful`() {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(1.0,1.0)


        val response = PlaceResponse(
            results = ArrayList(),
            context = Context(
                GeoBounds(
                    Circle(
                        Center(
                            1,
                            2
                        ),
                        1
                    )
                )
            ),
            cursor = "s443lk4"
        )

        coEvery {
            placeRepository.searchPlace(query.build())
        } returns response

        placesRecommendationViewModel.search()

        val observeResult = placesRecommendationViewModel.places.value

        assert(observeResult is ResponseResource.Error)
    }
}