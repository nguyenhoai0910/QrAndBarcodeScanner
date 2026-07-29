package com.example.barcodescanner.feature.tabs.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.barcodescanner.BuildConfig
import com.example.barcodescanner.R
import com.example.barcodescanner.di.barcodeDatabase
import com.example.barcodescanner.di.settings
import com.example.barcodescanner.extension.applySystemWindowInsets
import com.example.barcodescanner.extension.packageManager
import com.example.barcodescanner.extension.showError
import com.example.barcodescanner.feature.common.dialog.DeleteConfirmationDialogFragment
import com.example.barcodescanner.feature.tabs.settings.camera.ChooseCameraActivity
import com.example.barcodescanner.feature.tabs.settings.formats.SupportedFormatsActivity
import com.example.barcodescanner.feature.tabs.settings.permissions.AllPermissionsActivity
import com.example.barcodescanner.feature.tabs.settings.search.ChooseSearchEngineActivity
import com.example.barcodescanner.feature.tabs.settings.theme.ChooseThemeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import com.example.barcodescanner.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(), DeleteConfirmationDialogFragment.Listener {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSettingsBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportEdgeToEdge()
    }

    override fun onResume() {
        super.onResume()
        handleButtonCheckedChanged()
        handleButtonClicks()
        showSettings()
        showAppVersion()
    }

    override fun onDeleteConfirmed() {
        clearHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.clear()
    }

    fun supportEdgeToEdge() {
        binding.appBarLayout.applySystemWindowInsets(applyTop = true)
    }

    private fun handleButtonCheckedChanged() {
        binding.buttonInverseBarcodeColorsInDarkTheme.setCheckedChangedListener { settings.areBarcodeColorsInversed = it }
        binding.buttonOpenLinksAutomatically.setCheckedChangedListener { settings.openLinksAutomatically = it }
        binding.buttonCopyToClipboard.setCheckedChangedListener { settings.copyToClipboard = it }
        binding.buttonSimpleAutoFocus.setCheckedChangedListener { settings.simpleAutoFocus = it }
        binding.buttonFlashlight.setCheckedChangedListener { settings.flash = it }
        binding.buttonVibrate.setCheckedChangedListener { settings.vibrate = it }
        binding.buttonContinuousScanning.setCheckedChangedListener { settings.continuousScanning = it }
        binding.buttonConfirmScansManually.setCheckedChangedListener { settings.confirmScansManually = it }
        binding.buttonSaveScannedBarcodes.setCheckedChangedListener { settings.saveScannedBarcodesToHistory = it }
        binding.buttonSaveCreatedBarcodes.setCheckedChangedListener { settings.saveCreatedBarcodesToHistory = it }
        binding.buttonDoNotSaveDuplicates.setCheckedChangedListener { settings.doNotSaveDuplicates = it }
        binding.buttonEnableErrorReports.setCheckedChangedListener { settings.areErrorReportsEnabled = it }
    }

    private fun handleButtonClicks() {
        binding.buttonChooseTheme.setOnClickListener { ChooseThemeActivity.start(requireActivity()) }
        binding.buttonChooseCamera.setOnClickListener { ChooseCameraActivity.start(requireActivity()) }
        binding.buttonSelectSupportedFormats.setOnClickListener { SupportedFormatsActivity.start(requireActivity()) }
        binding.buttonClearHistory.setOnClickListener { showDeleteHistoryConfirmationDialog() }
        binding.buttonChooseSearchEngine.setOnClickListener { ChooseSearchEngineActivity.start(requireContext()) }
        binding.buttonPermissions.setOnClickListener { AllPermissionsActivity.start(requireActivity()) }
        binding.buttonCheckUpdates.setOnClickListener { showAppInMarket() }
        binding.buttonSourceCode.setOnClickListener { showSourceCode() }
    }

    private fun clearHistory() {
        binding.buttonClearHistory.isEnabled = false

        barcodeDatabase.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    binding.buttonClearHistory.isEnabled = true
                },
                { error ->
                    binding.buttonClearHistory.isEnabled = true
                    showError(error)
                }
            )
            .addTo(disposable)
    }

    private fun showSettings() {
        settings.apply {
            binding.buttonInverseBarcodeColorsInDarkTheme.isChecked = areBarcodeColorsInversed
            binding.buttonOpenLinksAutomatically.isChecked = openLinksAutomatically
            binding.buttonCopyToClipboard.isChecked = copyToClipboard
            binding.buttonSimpleAutoFocus.isChecked = simpleAutoFocus
            binding.buttonFlashlight.isChecked = flash
            binding.buttonVibrate.isChecked = vibrate
            binding.buttonContinuousScanning.isChecked = continuousScanning
            binding.buttonConfirmScansManually.isChecked = confirmScansManually
            binding.buttonSaveScannedBarcodes.isChecked = saveScannedBarcodesToHistory
            binding.buttonSaveCreatedBarcodes.isChecked = saveCreatedBarcodesToHistory
            binding.buttonDoNotSaveDuplicates.isChecked = doNotSaveDuplicates
            binding.buttonEnableErrorReports.isChecked = areErrorReportsEnabled
        }
    }

    private fun showDeleteHistoryConfirmationDialog() {
        val dialog = DeleteConfirmationDialogFragment.newInstance(R.string.dialog_delete_clear_history_message)
        dialog.show(childFragmentManager, "")
    }

    private fun showAppInMarket() {
        val uri = Uri.parse("market://details?id=" + requireContext().packageName)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showSourceCode() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wewewe718/QrAndBarcodeScanner"))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showAppVersion() {
        binding.buttonAppVersion.hint = BuildConfig.VERSION_NAME
    }
}