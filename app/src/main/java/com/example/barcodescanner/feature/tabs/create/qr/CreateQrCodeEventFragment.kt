package com.example.barcodescanner.feature.tabs.create.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barcodescanner.R
import com.example.barcodescanner.extension.textString
import com.example.barcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.example.barcodescanner.model.schema.Schema
import com.example.barcodescanner.model.schema.VEvent
import com.example.barcodescanner.databinding.FragmentCreateQrCodeVeventBinding

class CreateQrCodeEventFragment : BaseCreateBarcodeFragment() {
    private var _binding: FragmentCreateQrCodeVeventBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateQrCodeVeventBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextTitle.requestFocus()
        parentActivity.isCreateBarcodeButtonEnabled = true
    }

    override fun getBarcodeSchema(): Schema {
        return VEvent(
            uid = binding.editTextTitle.textString,
            organizer = binding.editTextOrganizer.textString,
            summary = binding.editTextSummary.textString,
            startDate = binding.buttonDateTimeStart.dateTime,
            endDate = binding.buttonDateTimeEnd.dateTime
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}