package com.celerocommerce.handylist.ui.main.customer.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.celerocommerce.handylist.databinding.ItemCustomerProblemBinding

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}

class CustomerProblemImageListAdapter(
    private val interaction: Interaction? = null,
    private val requestManager: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CustomerProblemImageViewHolder(
            ItemCustomerProblemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            requestManager,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomerProblemImageViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<String>) {
        differ.submitList(list)
    }

    class CustomerProblemImageViewHolder
    constructor(
        private val binding: ItemCustomerProblemBinding,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(bindingAdapterPosition, item)
            }

            requestManager
                .load(item)
                .transition(withCrossFade())
                .into(binding.itemCustomerProblemImageView)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: String)
    }
}