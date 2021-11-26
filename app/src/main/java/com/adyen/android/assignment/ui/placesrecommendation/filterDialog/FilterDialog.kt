package com.adyen.android.assignment.ui.placesrecommendation.filterDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder

class FilterDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_filter, container, false)
    }

    override fun onStart() {
        super.onStart()

        val dialog2: Dialog? = dialog
        if (dialog2 != null) {
            dialog2.window
                ?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }

    companion object {
        fun getInstance(filter: VenueRecommendationsQueryBuilder?): FilterDialog {
            val args = Bundle().apply {
                putParcelable("filterData", filter)
            }

            return FilterDialog().apply {
                arguments = args
            }
        }
    }

    interface FilterListener {
        fun apply(filter: VenueRecommendationsQueryBuilder)
    }
}