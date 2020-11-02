package com.celerocommerce.handylist.ui.main.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.celerocommerce.handylist.R
import com.celerocommerce.handylist.databinding.FragmentCustomerBinding
import com.celerocommerce.handylist.models.Customer
import com.celerocommerce.handylist.session.SessionManager
import com.celerocommerce.handylist.ui.DataState
import com.celerocommerce.handylist.ui.Response
import com.celerocommerce.handylist.ui.main.customer.state.CustomerStateEvent.GetDailyCustomersEvent
import com.celerocommerce.handylist.ui.main.customer.viewmodel.NetworkErrorListener
import com.celerocommerce.handylist.ui.main.customer.viewmodel.setCustomer
import com.celerocommerce.handylist.ui.main.customer.viewmodel.setDailyCustomers
import com.celerocommerce.handylist.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CustomerFragment : BaseCustomerFragment(), CustomerListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener, NetworkErrorListener {

    private var _binding: FragmentCustomerBinding? = null

    private val binding
        get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.networkErrorListener = this

        viewModel.setStateEvent(GetDailyCustomersEvent)

        binding.swipeRefresh.setOnRefreshListener(this)
        initRecyclerView()
        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.networkErrorListener = null
        _binding = null
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            Timber.d("DataState: $dataState")
            stateChangeListener.onDataStateChange(dataState)

            dataState?.data?.data?.getContentIfNotHandled()?.let {
                viewModel.setDailyCustomers(it.dailyCustomersFields.dailyCustomers)
            }

        }

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            Timber.d("ViewState: $viewState")

            viewState?.let {
                (binding.customerRecyclerview.adapter as CustomerListAdapter).submitList(
                    list = viewState.dailyCustomersFields.dailyCustomers
                )
            }
        }

    }

    private fun initRecyclerView() {
        binding.customerRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@CustomerFragment.context)

            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)

            adapter = CustomerListAdapter(
                interaction = this@CustomerFragment,
                requestManager = requestManager
            )
        }
    }

    override fun onRefresh() {
        viewModel.setStateEvent(GetDailyCustomersEvent)
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onItemSelected(
        item: Customer,
        profilePicture: CircleImageView,
        name: TextView,
        problem: TextView
    ) {
        viewModel.setCustomer(item)
        val imageTransition = getString(R.string.profile_pic_transition, item.id)
        profilePicture.transitionName = imageTransition

        val nameTransition = getString(R.string.name_transition, item.id)
        name.transitionName = nameTransition

        val problemTransition = getString(R.string.problem_transition, item.id)
        problem.transitionName = problemTransition

        val extras = FragmentNavigatorExtras(
            profilePicture to imageTransition,
            name to nameTransition,
            problem to problemTransition
        )
        findNavController().navigate(
            CustomerFragmentDirections.actionCustomerFragmentToViewCustomerFragment(
                customerName = item.name!!,
                imageUrl = item.profilePictures!!.large!!,
                profilePictureTransition = imageTransition,
                nameTransition = nameTransition,
                problemTransition = problemTransition
            ), extras
        )
    }

    override fun onNetworkError(response: Response) {
        stateChangeListener.onDataStateChange(
            DataState.error<Any>(
                response = response
            )
        )
    }
}