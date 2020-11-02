package com.celerocommerce.handylist.ui.main.customer

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.RequestManager
import com.celerocommerce.handylist.R
import com.celerocommerce.handylist.ui.DataStateChangeListener
import com.celerocommerce.handylist.ui.main.customer.viewmodel.CustomerViewModel
import timber.log.Timber
import javax.inject.Inject

abstract class BaseCustomerFragment : Fragment() {
    protected lateinit var stateChangeListener: DataStateChangeListener

    protected val viewModel by activityViewModels<CustomerViewModel>()

    @Inject
    protected lateinit var requestManager: RequestManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Timber.e("$context must implement DataStateChangeListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelActiveJobs()
    }

    fun cancelActiveJobs() {
        viewModel.cancelActiveJobs()
    }
}