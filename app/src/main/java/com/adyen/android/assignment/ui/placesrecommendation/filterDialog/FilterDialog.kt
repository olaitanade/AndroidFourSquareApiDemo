package com.adyen.android.assignment.ui.placesrecommendation.filterDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.databinding.DialogFilterBinding
import timber.log.Timber

class FilterDialog: DialogFragment(), AdapterView.OnItemSelectedListener {
    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogFilterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<VenueRecommendationsQueryBuilder>("filterData")?.let { filterData ->
            val query = filterData.build()
            binding.radius.setText(query["radius"])
            binding.ne.setText(query["ne"])
            binding.sw.setText(query["sw"])
            binding.sort.setText(query["sort"])
            binding.near.setText(query["near"])
            binding.categories.setText(query["categories"])
        }

        binding.apply.setOnClickListener{
            onApply()
        }

        binding.sort.setOnClickListener{
            binding.sortSpinner.performClick()
        }
        binding.sortSpinner.onItemSelectedListener = this
        binding.sortSpinner.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.sort).toMutableList()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    }

    private fun onApply() {

        val filterData = VenueRecommendationsQueryBuilder()
            .setRadius(binding.radius.text.toString())
            .setNe(binding.ne.text.toString())
            .setSw(binding.sw.text.toString())
            .setSort(binding.sort.text.toString())
            .setNear(binding.near.text.toString())
            .setCategories(binding.categories.text.toString())

        (requireParentFragment() as FilterListener).apply(filterData)
        dismiss()
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

    override fun onItemSelected(view: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (view?.id) {
            R.id.sortSpinner -> {
                Timber.d(binding.sortSpinner.selectedItem.toString())
                binding.sort.setText(binding.sortSpinner.selectedItem.toString())
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}