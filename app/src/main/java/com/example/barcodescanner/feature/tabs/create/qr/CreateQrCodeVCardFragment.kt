package com.example.barcodescanner.feature.tabs.create.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barcodescanner.R
import com.example.barcodescanner.extension.textString
import com.example.barcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.example.barcodescanner.model.Contact
import com.example.barcodescanner.model.schema.Schema
import com.example.barcodescanner.model.schema.VCard
import com.example.barcodescanner.databinding.FragmentCreateQrCodeVcardBinding

class CreateQrCodeVCardFragment : BaseCreateBarcodeFragment() {
    private var _binding: FragmentCreateQrCodeVcardBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateQrCodeVcardBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextFirstName.requestFocus()
        parentActivity.isCreateBarcodeButtonEnabled = true
    }

    override fun getBarcodeSchema(): Schema {
       return VCard(
           firstName = binding.editTextFirstName.textString,
           lastName = binding.editTextLastName.textString,
           organization = binding.editTextOrganization.textString,
           title = binding.editTextJob.textString,
           email = binding.editTextEmail.textString,
           phone = binding.editTextPhone.textString,
           secondaryPhone = binding.editTextFax.textString,
           url = binding.editTextWebsite.textString
       )
    }

    override fun showContact(contact: Contact) {
        binding.editTextFirstName.setText(contact.firstName)
        binding.editTextLastName.setText(contact.lastName)
        binding.editTextEmail.setText(contact.email)
        binding.editTextPhone.setText(contact.phone)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}