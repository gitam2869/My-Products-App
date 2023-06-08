package com.example.myproducts.ui.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.myproducts.R
import com.example.myproducts.databinding.ItemProductBinding
import com.example.myproducts.utils.ViewListener.Companion.setOnSingleClickListener
import com.example.myproducts.data.model.ProductDetails

class ProductViewHolder(itemView: View, onCardClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val TAG = "CashValueViewHolder"
    private val binding = ItemProductBinding.bind(itemView)

    init {
        binding.cvProduct.setOnSingleClickListener {
            onCardClick(adapterPosition)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(
        holder: RecyclerView.ViewHolder,
        position: Int,
        productItem: ProductDetails
    ) {
        binding.run {
            Glide.with(holder.itemView.context)
                .load(productItem.image)
                .placeholder(R.mipmap.ic_launcher)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(binding.ivProduct)

            tvName.text = productItem.product_name
            tvType.text = productItem.product_type
            tvPrice.text = productItem.price.toString()
            tvTax.text = productItem.tax.toString()
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): View {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
            return view
        }
    }
}