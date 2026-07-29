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
import com.example.barcodescanner.model.schema.Email
import com.example.barcodescanner.model.schema.Schema
import com.example.barcodescanner.databinding.FragmentCreateQrCodeEmailBinding

class CreateQrCodeEmailFragment : BaseCreateBarcodeFragment() {
    private var _binding: FragmentCreateQrCodeEmailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateQrCodeEmailBinding.inflate(inflater, container, false).let {
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
        return Email(
            email = binding.editTextEmail.textString,
            subject = binding.editTextSubject.textString,
            body = binding.editTextMessage.textString
        )
    }

    private fun initTitleEditText() {
        binding.editTextEmail.requestFocus()
    }

    private fun handleTextChanged() {
        binding.editTextEmail.addTextChangedListener { toggleCreateBarcodeButton() }
        binding.editTextSubject.addTextChangedListener { toggleCreateBarcodeButton() }
        binding.editTextMessage.addTextChangedListener { toggleCreateBarcodeButton() }
    }

    private fun toggleCreateBarcodeButton() {
        parentActivity.isCreateBarcodeButtonEnabled = binding.editTextEmail.isNotBlank() || binding.editTextSubject.isNotBlank() || binding.editTextMessage.isNotBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}