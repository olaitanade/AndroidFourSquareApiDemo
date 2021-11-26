package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import timber.log.Timber

class PlacesUnitTest {
    @Test
    fun testResponseCode() {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(52.376510, 4.905890)
            .build()
        val response = PlacesService.instance
            .getVenueRecommendations(query)
            .execute()

        val errorBody = response.errorBody()
        Timber.d(errorBody?.string())
        assertNull("Received an error: ${errorBody?.string()}", errorBody)

        val responseWrapper = response.body()
        Timber.d(responseWrapper.toString())
        assertNotNull("Response is null.", responseWrapper)
    }
}
