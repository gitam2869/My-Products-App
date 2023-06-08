package com.example.myproducts.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproducts.R
import com.example.myproducts.databinding.FragmentProductsBinding
import com.example.myproducts.ui.adapter.ProductsAdapter
import com.example.myproducts.ui.callback.IProductCallback
import com.example.myproducts.utils.ApiResult
import com.example.myproducts.utils.ViewExtension.gone
import com.example.myproducts.utils.ViewExtension.visible
import com.example.myproducts.viewmodel.ProductListViewModel
import com.example.runningtrackerapp.data.model.ProductItem
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductsFragment : Fragment() {

    private val TAG = "ProductsFragment"
    private var _binding: FragmentProductsBinding? = null
    private val binding: FragmentProductsBinding
        get() = _binding!!

    private val productListViewModel: ProductListViewModel by viewModel()
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView()

        observeProductsChanges()
        getProducts()
    }

    private fun recyclerView() {
        productsAdapter = ProductsAdapter(object : IProductCallback {
            override fun onClick(position: Int, productItem: ProductItem) {
                Log.d(TAG, "onClick: " + productItem)
            }
        })

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productsAdapter
        }
    }

    private fun observeProductsChanges() {
        productListViewModel.response.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    it.data?.let { it1 -> productsAdapter.submitList(it1) }
                }

                is ApiResult.Error -> {
                    showErrorView(it.message ?: "")
                }

                is ApiResult.Loading -> {
                    if (it.isLoading) {
                        hideErrorView()
                        showLoading()
                    } else
                        hideLoading()

                }
            }
        }
    }

    private fun getProducts() {
        productListViewModel.getProducts()
    }


    private fun showLoading() {
        binding.pbLoading.visible()
    }

    private fun hideLoading() {
        binding.pbLoading.gone()
    }

    private fun showErrorView(message: String) {
        binding.tvError.text = message
        binding.tvError.visible()
    }

    private fun hideErrorView() {
        binding.tvError.gone()
    }
}