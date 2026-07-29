package com.example.barcodescanner.feature.tabs.create.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.barcodescanner.R
import com.example.barcodescanner.extension.isNotBlank
import com.example.barcodescanner.extension.textString
import com.example.barcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.example.barcodescanner.model.schema.Bookmark
import com.example.barcodescanner.model.schema.Schema
import com.example.barcodescanner.databinding.FragmentCreateQrCodeBookmarkBinding

class CreateQrCodeBookmarkFragment : BaseCreateBarcodeFragment() {
    private var _binding: FragmentCreateQrCodeBookmarkBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateQrCodeBookmarkBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitleEditText()
        handleTextChanged()
    }

    override fun getBarcodeSchema(): Schema {
        return Bookmark(
            title = binding.editTextTitle.textString,
            url = binding.editTextUrl.textString
        )
    }

    private fun initTitleEditText() {
        binding.editTextTitle.requestFocus()
    }

    private fun handleTextChanged() {
        binding.editTextTitle.addTextChangedListener { toggleCreateBarcodeButton() }
        binding.editTextUrl.addTextChangedListener { toggleCreateBarcodeButton() }
    }

    private fun toggleCreateBarcodeButton() {
        parentActivity.isCreateBarcodeButtonEnabled = binding.editTextTitle.isNotBlank() || binding.editTextUrl.isNotBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}