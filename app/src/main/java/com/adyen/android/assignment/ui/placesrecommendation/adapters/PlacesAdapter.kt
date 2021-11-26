package com.adyen.android.assignment.ui.placesrecommendation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.model.places.Place

class PlacesAdapter (
    var places: MutableList<Place> = mutableListOf(),
    val onItemClick: ((Place) -> Unit)
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            PLACE_TYPE -> {
                val view = inflater.inflate(R.layout.item_places, parent, false)
                PlaceViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    fun getNoOfPlaces() = places.filterNotNull().count()

    fun removeLoading() {
        val position = places.size - 1
        if (position != -1) {
            places.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (places[position] == null)
            LOADING_TYPE
        else
            PLACE_TYPE
    }

    override fun getItemCount() = places.size

    fun setData(places: List<Place>) {
        this.places = places.toMutableList()
        notifyDataSetChanged()
    }

    fun updateData(places: List<Place>) {
        val position = this.places.size
        this.places.addAll(places)
        notifyItemRangeInserted(position, places.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            LOADING_TYPE -> (holder as LoadingViewHolder)

            PLACE_TYPE -> {
                (holder as PlaceViewHolder).bind(places[position]!!)
            }
        }
    }

    fun clear() {
        this.places.clear()
        notifyDataSetChanged()
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(place: Place) {
            with(itemView) {
                findViewById<TextView>(R.id.place_name).text = place.name

                setOnClickListener {
                    onItemClick(place)
                }
            }
        }
    }

    companion object {
        const val PLACE_TYPE = 1
        const val LOADING_TYPE = 2
    }
}