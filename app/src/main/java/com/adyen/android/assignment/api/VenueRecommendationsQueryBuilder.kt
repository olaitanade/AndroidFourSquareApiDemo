package com.adyen.android.assignment.api

import android.os.Parcelable
import com.adyen.android.assignment.api.model.places.PlaceSort
import kotlinx.android.parcel.Parcelize

@Parcelize
class VenueRecommendationsQueryBuilder : PlacesQueryBuilder() , Parcelable{
    private var latitudeLongitude: String? = null
    private var query: String? = null
    private var radius: String? = null
    private var categories: String? = null
    private var chains: String? = null
    private var excludeChains: String? = null
    private var fields: String? = null
    private var ne: String? = null
    private var sw: String? = null
    private var near: String? = null
    private var sort: String? = null

    fun setLatitudeLongitude(latitude: Double, longitude: Double): VenueRecommendationsQueryBuilder {
        this.latitudeLongitude = "$latitude,$longitude"
        return this
    }

    fun setQuery(query:String?): VenueRecommendationsQueryBuilder {
        this.query = query
        return this
    }

    fun setRadius(radius:String?): VenueRecommendationsQueryBuilder {
        this.radius = radius
        return this
    }

    fun setCategories(categories:String?): VenueRecommendationsQueryBuilder {
        this.categories = categories
        return this
    }

    fun setChain(chains:String?): VenueRecommendationsQueryBuilder {
        this.chains = chains
        return this
    }

    fun setExcludeChains(excludeChains:String?): VenueRecommendationsQueryBuilder {
        this.excludeChains = excludeChains
        return this
    }

    fun setField(fields:String?): VenueRecommendationsQueryBuilder {
        this.fields = fields
        return this
    }


    fun setNe(ne:String?): VenueRecommendationsQueryBuilder {
        this.ne = ne
        return this
    }

    fun setSw(sw:String?): VenueRecommendationsQueryBuilder {
        this.sw = sw
        return this
    }

    fun setNear(near:String?): VenueRecommendationsQueryBuilder {
        this.near = near
        return this
    }

    fun setSort(sort:String?): VenueRecommendationsQueryBuilder {
        if(sort.isNullOrEmpty()){
            this.sort = PlaceSort.RELEVANCE.value
        }else{
            this.sort = sort
        }
        return this
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        latitudeLongitude?.apply { queryParams["ll"] = this }
        query?.apply { queryParams["query"] = this }
        radius?.apply { queryParams["radius"] = this }
        categories?.apply { queryParams["categories"] = this }
        chains?.apply { queryParams["chains"] = this }
        excludeChains?.apply { queryParams["excludeChains"] = this }
        ne?.apply { queryParams["ne"] = this }
        sw?.apply { queryParams["sw"] = this }
        sort?.apply { queryParams["sort"] = this }
        fields?.apply { queryParams["fields"] = this }
        near?.apply { queryParams["near"] = this }
    }
}
