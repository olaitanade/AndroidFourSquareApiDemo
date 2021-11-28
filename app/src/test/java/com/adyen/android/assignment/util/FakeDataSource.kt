package com.adyen.android.assignment.util

import com.adyen.android.assignment.api.model.places.*

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
}