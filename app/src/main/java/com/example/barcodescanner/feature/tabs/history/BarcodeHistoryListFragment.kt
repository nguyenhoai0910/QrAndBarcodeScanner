package com.example.barcodescanner.feature.tabs.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barcodescanner.R
import com.example.barcodescanner.di.barcodeDatabase
import com.example.barcodescanner.extension.orZero
import com.example.barcodescanner.extension.showError
import com.example.barcodescanner.feature.barcode.BarcodeActivity
import com.example.barcodescanner.model.Barcode
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import com.example.barcodescanner.databinding.FragmentBarcodeHistoryListBinding

class BarcodeHistoryListFragment : Fragment(), BarcodeHistoryAdapter.Listener {
    private var _binding: FragmentBarcodeHistoryListBinding? = null
    private val binding get() = _binding!!


    companion object {
        private const val PAGE_SIZE = 20
        private const val TYPE_ALL = 0
        private const val TYPE_FAVORITES = 1
        private const val TYPE_KEY = "TYPE_KEY"

        fun newInstanceAll(): BarcodeHistoryListFragment {
            return BarcodeHistoryListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE_KEY, TYPE_ALL)
                }
            }
        }

        fun newInstanceFavorites(): BarcodeHistoryListFragment {
            return BarcodeHistoryListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE_KEY, TYPE_FAVORITES)
                }
            }
        }
    }

    private val disposable = CompositeDisposable()
    private val scanHistoryAdapter = BarcodeHistoryAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBarcodeHistoryListBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadHistory()
    }

    override fun onBarcodeClicked(barcode: Barcode) {
        BarcodeActivity.start(requireActivity(), barcode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.clear()
    }

    private fun initRecyclerView() {
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scanHistoryAdapter
        }
    }

    private fun loadHistory() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()

        val dataSource = when (arguments?.getInt(TYPE_KEY).orZero()) {
            TYPE_ALL -> barcodeDatabase.getAll()
            TYPE_FAVORITES -> barcodeDatabase.getFavorites()
            else -> return
        }

        RxPagedListBuilder(dataSource, config)
            .buildFlowable(BackpressureStrategy.LATEST)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                scanHistoryAdapter::submitList,
                ::showError
            )
            .addTo(disposable)
    }
}