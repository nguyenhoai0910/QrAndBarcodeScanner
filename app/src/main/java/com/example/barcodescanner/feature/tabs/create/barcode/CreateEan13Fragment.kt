package com.example.barcodescanner.feature.tabs.create.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.barcodescanner.R
import com.example.barcodescanner.extension.textString
import com.example.barcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.example.barcodescanner.model.schema.Other
import com.example.barcodescanner.model.schema.Schema
import com.example.barcodescanner.databinding.FragmentCreateEan13Binding

class CreateEan13Fragment : BaseCreateBarcodeFragment() {
    private var _binding: FragmentCreateEan13Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateEan13Binding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.requestFocus()
        binding.editText.addTextChangedListener {
            parentActivity.isCreateBarcodeButtonEnabled = binding.editText.text.length == 12
        }
    }

    override fun getBarcodeSchema(): Schema {
        return Other(binding.editText.textString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}