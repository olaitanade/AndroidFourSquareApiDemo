package com.adyen.android.assignment.repository

import com.adyen.android.assignment.api.PlacesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val service: PlacesService
) {

    suspend fun searchPlace(query: Map<String, String>) = withContext(Dispatchers.IO) {

        val result =  kotlin.runCatching {
            service.getPlaceRecommendation(query)
        }

        if (result.isFailure) {
            result.exceptionOrNull()?.let {
                throw Exception(it.localizedMessage)
            }
        }

        result.getOrNull()
    }
}