package com.example.myproducts.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproducts.R
import com.example.myproducts.databinding.FragmentProductsBinding
import com.example.myproducts.ui.adapter.ProductsAdapter
import com.example.myproducts.ui.callback.IProductCallback
import com.example.myproducts.utils.ApiResult
import com.example.myproducts.utils.Navigation.safeNavigation
import com.example.myproducts.utils.ViewExtension.gone
import com.example.myproducts.utils.ViewExtension.visible
import com.example.myproducts.utils.ViewListener.Companion.setOnSingleClickListener
import com.example.myproducts.viewmodel.ProductListViewModel
import com.example.myproducts.data.model.ProductDetails
import com.example.myproducts.utils.ViewExtension.disable
import com.example.myproducts.utils.ViewExtension.enable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductsFragment : Fragment() {

    private val TAG = "ProductsFragment"
    private var _binding: FragmentProductsBinding? = null
    private val binding: FragmentProductsBinding
        get() = _binding!!

    private val productListViewModel: ProductListViewModel by viewModel()
    private lateinit var productsAdapter: ProductsAdapter
    private var products: MutableList<ProductDetails> = mutableListOf()

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
        callListeners()
        searchViewSetup()
    }

    private fun callListeners() {
        binding.run {
            btnAdd.setOnSingleClickListener {
                findNavController().safeNavigation(
                    R.id.productsFragment,
                    R.id.addProductFragment
                )
            }
        }
    }

    private fun recyclerView() {
        productsAdapter = ProductsAdapter(object : IProductCallback {
            override fun onClick(position: Int, productItem: ProductDetails) {
                Log.d(TAG, "onClick: " + productItem)
            }
        })

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productsAdapter
        }

        productsAdapter.differ.addListListener { previousList, currentList ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (previousList != currentList)
                    binding.rvProducts.smoothScrollToPosition(0)
            }, 400)
        }

    }

    private fun observeProductsChanges() {
        productListViewModel.response.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    it.data?.let { it1 ->
                        productsAdapter.submitList(it1)
                        products = it1
                        if (products.isEmpty())
                            showErrorView(requireContext().resources.getString(R.string.no_product_found))
                    }
                }

                is ApiResult.Error -> {
                    showErrorView(it.message ?: "")
                }

                is ApiResult.Loading -> {
                    if (it.isLoading) {
                        hideErrorView()
                        showLoading()
                        setStateOfSearchView(false)
                    } else {
                        setStateOfSearchView(true)
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun searchViewSetup() {
        binding.searchView.run {
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    hideErrorView()

                    if (newText != null) {
                        if (newText.isEmpty())
                            productsAdapter.submitList(products)
                        else
                            filterList(newText)
                    }
                    return true
                }
            })
        }
    }

    private fun getProducts() {
        lifecycleScope.launch {
            productListViewModel.getProducts()
        }
    }


    private fun filterList(query: String) {
        val filterList =
            products.filter {
                it.product_name.lowercase().contains(query)
                        || it.product_type.lowercase().contains(query)
            }

        productsAdapter.submitList(filterList)

        if (filterList.isEmpty())
            showErrorView(requireContext().resources.getString(R.string.no_product_found))
    }

    private fun setStateOfSearchView(state: Boolean) {
        if (state) binding.searchView.enable() else binding.searchView.disable()
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