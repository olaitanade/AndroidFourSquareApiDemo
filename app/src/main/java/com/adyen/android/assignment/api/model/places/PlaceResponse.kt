package com.adyen.android.assignment.api.model.places

data class PlaceResponse (
    val results: List<Place>,
    val context: Context,
    var cursor: String?=null
)