package com.example.myproducts.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myproducts.ui.callback.IProductCallback
import com.example.myproducts.ui.viewholder.ProductViewHolder
import com.example.runningtrackerapp.data.model.ProductItem

class ProductsAdapter(val iProductCallback: IProductCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "CashValueAdapter"

    private val differCallback = object : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(
            oldItem: ProductItem,
            newItem: ProductItem
        ): Boolean {
            return oldItem.product_name == newItem.product_name
        }

        override fun areContentsTheSame(
            oldItem: ProductItem,
            newItem: ProductItem
        ): Boolean {
            return oldItem === newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val onCardClick = { position: Int ->
            val item = differ.currentList[position]
            iProductCallback.onClick(
                position,
                item
            )
        }
        return ProductViewHolder(
            ProductViewHolder.createViewHolder(parent),
            onCardClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val productViewHolder = holder as ProductViewHolder
        productViewHolder.bind(
            holder,
            position,
            differ.currentList[position]
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductItem>) {
        differ.submitList(list)
    }
}