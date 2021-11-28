package com.adyen.android.assignment.util

import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.places.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

object FakeDataSource {

    val successfulPlaceResponse = PlaceResponse(
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

    val successfulPlaceResponseWithoutLink = PlaceResponse(
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
        )
    )

    val query = VenueRecommendationsQueryBuilder()
        .setLatitudeLongitude(52.376510, 4.905890)

    val serviceResponse = Response.success(successfulPlaceResponse)
    val serviceResponseWithoutCursor = Response.success(successfulPlaceResponseWithoutLink)

    private val errorBody = "{\"message\": \"exactly one of ne/sw, near, or ll/radius is required\"}"
        .toResponseBody("application/json".toMediaTypeOrNull())
    val serviceResponseError = Response.error<Any>(400, errorBody)
}