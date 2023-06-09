package com.example.myproducts.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myproducts.R
import com.example.myproducts.data.model.ProductItemBody
import com.example.myproducts.databinding.FragmentAddProductBinding
import com.example.myproducts.ui.activity.MainActivity
import com.example.myproducts.utils.ApiResult
import com.example.myproducts.utils.ViewExtension.disable
import com.example.myproducts.utils.ViewExtension.enable
import com.example.myproducts.utils.ViewExtension.gone
import com.example.myproducts.utils.ViewExtension.visible
import com.example.myproducts.utils.ViewListener.Companion.setOnSingleClickListener
import com.example.myproducts.viewmodel.AddProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.net.URI

class AddProductFragment : Fragment() {

    private val TAG = "AddProductFragment"
    private var _binding: FragmentAddProductBinding? = null
    private val binding: FragmentAddProductBinding
        get() = _binding!!

    private val addProductViewModel: AddProductViewModel by viewModel()

    private val files: MutableList<File> = mutableListOf()
    private var type = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkButtonStatus()
        selectProductTypeSetup()
        observeChanges()
        callListeners()
    }

    private fun observeChanges() {
        addProductViewModel.response.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    showAddedDialog(it.data?.product_details?.product_name ?: "")
                }

                is ApiResult.Error -> {
                    showErrorView(it.message ?: "")
                }

                is ApiResult.Loading -> {
                    if (it.isLoading) {
                        showLoading()
                    } else
                        hideLoading()

                }
            }

        }
    }

    private fun callListeners() {
        binding.run {
            header.setNavigationOnClickListener {
                navigateToPrevious()
            }

            ivProduct.setOnSingleClickListener {
                pickImage()
            }

            btnSave.setOnSingleClickListener {
                saveProduct()
            }

            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkButtonStatus()
                }
            })

            atType.setOnItemClickListener { adapterView, view, i, l ->
                val types = requireContext().resources.getStringArray(R.array.product_types)
                type = types[i]
                Log.d(TAG, "callListeners: at " + type)
                checkButtonStatus()
            }

            etPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkButtonStatus()
                }
            })

            etTax.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkButtonStatus()
                }
            })
        }
    }

    private fun selectProductTypeSetup() {
        val types = requireContext().resources.getStringArray(R.array.product_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_type, types)
        binding.atType.setAdapter(arrayAdapter)
    }

    private fun pickImage() {
        ImagePicker.Companion.with(this)
            .cropSquare()
            .maxResultSize(512, 512)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.data != null) {

            val imageFile = File(URI(data.data.toString()))
            files.clear()
            files.add(imageFile)

            Glide.with(requireContext())
                .load(imageFile)
                .into(binding.ivProduct)
        }
    }

    private fun checkButtonStatus() {
        val buttonState =
            getName().isNotEmpty()
                    && type.isNotEmpty()
                    && isNumeric(getPrice())
                    && isNumeric(getTax())

        if (buttonState) binding.btnSave.enable() else binding.btnSave.disable()
    }

    private fun getName(): String {
        return binding.etName.text.toString().trim()
    }

    private fun getPrice(): String {
        return binding.etPrice.text.toString().trim()
    }

    private fun getTax(): String {
        return binding.etTax.text.toString().trim()
    }

    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    private fun saveProduct() {
        lifecycleScope.launch {
            addProductViewModel.addProduct(getAddProductBody())
        }
    }

    private fun getAddProductBody() = ProductItemBody(
        binding.etName.text.toString().trim(),
        binding.atType.text.toString().trim(),
        binding.etPrice.text.toString().trim(),
        binding.etTax.text.toString().trim(),
        files
    )


    private fun showLoading() {
        binding.pbLoading.visible()
    }

    private fun hideLoading() {
        binding.pbLoading.gone()
    }

    private fun showErrorView(message: String) {
        (activity as MainActivity).show(message = message)
    }

    private fun showAddedDialog(productName: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Added!")
        builder.setMessage("Your product $productName is successfully added!")
        builder.setCancelable(true)

        builder.setPositiveButton("OK",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                navigateToPrevious()
            })

        builder.setOnCancelListener {
            navigateToPrevious()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun navigateToPrevious() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}