package com.example.barcodescanner.feature.tabs.settings.permissions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.barcodescanner.R
import com.example.barcodescanner.extension.applySystemWindowInsets
import com.example.barcodescanner.feature.BaseActivity
import com.example.barcodescanner.databinding.ActivityAllPermissionsBinding

class AllPermissionsActivity : BaseActivity() {
    private lateinit var binding: ActivityAllPermissionsBinding

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AllPermissionsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}