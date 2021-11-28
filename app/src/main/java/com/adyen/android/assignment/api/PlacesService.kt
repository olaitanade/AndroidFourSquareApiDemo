package com.adyen.android.assignment.api

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.api.model.VenueRecommendationsResponse
import com.adyen.android.assignment.api.model.places.PlaceResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit


interface PlacesService {
    /**
     * Get venue recommendations.
     *
     * See [the docs](https://developer.foursquare.com/docs/api/venues/explore)
     */
    @GET("venues/explore")
    fun getVenueRecommendations(@QueryMap query: Map<String, String>): Call<ResponseWrapper<VenueRecommendationsResponse>>

    @GET("places/search")
    suspend fun getPlaceRecommendation(@QueryMap query: Map<String, String>): Response<PlaceResponse>

}
