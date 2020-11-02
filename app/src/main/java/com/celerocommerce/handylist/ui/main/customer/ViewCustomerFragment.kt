package com.celerocommerce.handylist.ui.main.customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.celerocommerce.handylist.R
import com.celerocommerce.handylist.databinding.FragmentViewCustomerBinding
import com.celerocommerce.handylist.models.Customer
import com.celerocommerce.handylist.ui.main.customer.state.CustomerStateEvent.SetCustomerHandledEvent
import com.celerocommerce.handylist.ui.main.customer.state.CustomerViewState
import com.celerocommerce.handylist.ui.main.customer.viewmodel.CustomerProblemImageListAdapter
import com.celerocommerce.handylist.ui.main.customer.viewmodel.getCustomer
import com.celerocommerce.handylist.ui.main.customer.viewmodel.setIsHandled
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ViewCustomerFragment : BaseCustomerFragment(), CustomerProblemImageListAdapter.Interaction {

    private var _binding: FragmentViewCustomerBinding? = null

    private val binding
        get() = _binding!!

    private val args by navArgs<ViewCustomerFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCustomerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            viewModel.restoreCustomer(getString(R.string.customer_key_saved_state))
        }

        initRecyclerView()
        subscribeObservers()
        initTransition()
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveCustomer(getString(R.string.customer_key_saved_state))
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState?.data?.data?.getContentIfNotHandled()
                ?.let { customerViewState: CustomerViewState ->
                    Timber.d("DataState: $customerViewState")
                    customerViewState.viewCustomerFields.customer?.let {
                        viewModel.setIsHandled(
                            it.isHandled
                        )
                    }
                }
        }
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            Timber.d("ViewState: $viewState")
            viewState.viewCustomerFields.customer?.let {
                Timber.d(it.toString())
                setCustomerProperties(it)
            }
        }
    }

    private fun initRecyclerView() {
        binding.customerViewProblemImageRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@ViewCustomerFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = CustomerProblemImageListAdapter(
                interaction = this@ViewCustomerFragment,
                requestManager = requestManager
            )
        }
    }


    private fun initTransition() {
        binding.customerViewProfilePicture.apply {
            transitionName = args.profilePictureTransition
            Glide.with(this)
                .load(args.imageUrl)
                .into(this)
        }
        binding.customerViewCustomerName.transitionName = args.nameTransition
        binding.customerViewServiceReason.transitionName = args.problemTransition
    }

    private fun setCustomerProperties(customer: Customer) {
        requestManager
            .load(customer.profilePictures!!.large)
            .into(binding.customerViewProfilePicture)

        binding.customer = customer
        binding.customerViewInformationInclude.customer = customer

        if (customer.problemPictures!!.isEmpty()) {
            binding.customerViewProblemImageRecyclerView.visibility = View.INVISIBLE
        } else {
            binding.customerViewProblemImageRecyclerView.visibility = View.VISIBLE

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.viewCustomerConstraintLayout)

            if (customer.problemPictures.size == 1) {
                constraintSet.constrainWidth(
                    binding.customerViewProblemImageRecyclerView.id,
                    ConstraintSet.WRAP_CONTENT
                )
            } else {
                constraintSet.constrainWidth(
                    binding.customerViewProblemImageRecyclerView.id,
                    ConstraintSet.MATCH_CONSTRAINT
                )
            }
            binding.viewCustomerConstraintLayout.setConstraintSet(constraintSet)

            (binding.customerViewProblemImageRecyclerView.adapter as CustomerProblemImageListAdapter).submitList(
                customer.problemPictures
            )
        }
    }

    private fun setClickListeners() {
        binding.customerViewHandledButton.setOnClickListener {
            viewModel.setIsHandled(!viewModel.getCustomer().isHandled)
            viewModel.setStateEvent(SetCustomerHandledEvent)
            findNavController().popBackStack()
        }

        binding.customerViewInformationInclude.customerViewGetDirectionsButton.setOnClickListener {
            val directionsIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/maps?daddr=${viewModel.getCustomer().location!!.coordinates!!.latitude},${viewModel.getCustomer().location!!.coordinates!!.longitude}")
            )
            directionsIntent.setPackage("com.google.android.apps.maps")
            startActivity(directionsIntent)
        }
    }

    override fun onItemSelected(position: Int, item: String) {
    }
}