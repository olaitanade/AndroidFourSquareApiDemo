package com.adyen.android.assignment.api.model.places


data class Place (

    val fsqID: String,

    val categories: List<Category>,
    val chains: List<Chain>,


    val dateClosed: String,

    val description: String,
    val distance: Long,
    val email: String,
    val fax: String,
    val geocodes: Geocodes,
    val hours: Hours,


    val hoursPopular: List<HoursPopular>,

    val location: Location,
    val menu: String,
    val name: String,
    val photos: List<Photo>,
    val popularity: Long,
    val price: Long,
    val rating: Long,

    val relatedPlaces: RelatedPlaces,

    val socialMedia: SocialMedia,

    val stats: Stats,
    val tastes: List<String>,
    val tel: String,
    val timezone: String,
    val tips: List<Tip>,
    val verified: Boolean,
    val website: String
)

data class Category (
    val id: Long,
    val name: String,
    val icon: Photo
)

data class Photo (
    val id: String,

    val createdAt: String,

    val prefix: String,
    val suffix: String,
    val width: Long,
    val height: Long,
    val classifications: List<String>,
    val tip: Tip
)

data class Tip (
    val id: String,

    val createdAt: String,

    val text: String,
    val url: String,

    val agreeCount: Long,

    val disagreeCount: Long
)

data class Chain (
    val id: String,
    val name: String
)

data class Geocodes (
    val dropOff: DropOff,

    val frontDoor: DropOff,

    val main: DropOff,
    val road: DropOff,
    val roof: DropOff
)

data class DropOff (
    val latitude: Double,
    val longitude: Double
)

data class Hours (
    val display: String,

    val isLocalHoliday: Boolean,

    val openNow: Boolean,

    val regular: List<HoursPopular>,
    val seasonal: List<Seasonal>
)

data class HoursPopular (
    val close: String,
    val day: Long,
    val open: String
)

data class Seasonal (
    val closed: Boolean,

    val endDate: String,

    val hours: List<HoursPopular>,

    val startDate: String
)

data class Location (
    val address: String,

    val addressExtended: String,

    val adminRegion: String,

    val country: String,

    val crossStreet: String,

    val dma: String,
    val locality: String,
    val neighborhood: List<String>,

    val poBox: String,

    val postTown: String,

    val postcode: String,
    val region: String
)

data class RelatedPlaces (
    val children: List<Any?>
)

data class SocialMedia (
    val facebookID: String,

    val instagram: String,
    val twitter: String
)

data class Stats (
    val totalPhotos: Long,

    val totalRatings: Long,

    val totalTips: Long
)

data class Context (
    val geoBounds: GeoBounds
)

data class GeoBounds (
    val circle: Circle
)

data class Circle (
    val center: Center,
    val radius: Long
)

data class Center (
    val latitude: Long,
    val longitude: Long
)

