package com.celerocommerce.handylist.ui.main.customer

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.celerocommerce.handylist.R
import com.celerocommerce.handylist.databinding.ItemCustomerBinding
import com.celerocommerce.handylist.models.Customer
import de.hdodenhof.circleimageview.CircleImageView

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {

    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem == newItem
    }

}
class CustomerListAdapter(private val interaction: Interaction? = null, private val requestManager: RequestManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CustomerViewHolder(
            ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            requestManager,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomerViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Customer>) {
        differ.submitList(list)
    }

    class CustomerViewHolder
    constructor(
        private val binding: ItemCustomerBinding,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Customer) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(item, binding.customerItemProfilePicture, binding.customerItemName, binding.customerItemServiceReason)
            }

            requestManager
                .load(item.profilePictures!!.large)
                .into(binding.customerItemProfilePicture)

            binding.customer = item
            binding.customerItemIsHandled.visibility = if (item.isHandled) View.VISIBLE else View.INVISIBLE

        }
    }

    interface Interaction {
        fun onItemSelected(item: Customer, profilePicture: CircleImageView, name: TextView, problem: TextView)
    }
}