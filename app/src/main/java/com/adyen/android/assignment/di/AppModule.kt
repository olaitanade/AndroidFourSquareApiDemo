package com.adyen.android.assignment.di

import com.adyen.android.assignment.BuildConfig.API_KEY
import com.adyen.android.assignment.BuildConfig.FOURSQUARE_BASE_URL
import com.adyen.android.assignment.api.PlacesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private val TIME_OUT = 120L
    @Provides
    fun providesOkHttpClient(): OkHttpClient {

        val chainInterceptor = { chain: Interceptor.Chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", API_KEY)
                    .build()
            )
        }
        return OkHttpClient.Builder()
            .addInterceptor(chainInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    fun providesRetrofitBuilder(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FOURSQUARE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun providesApiService(retrofit: Retrofit): PlacesService {
        return retrofit.create(PlacesService::class.java)
    }


}