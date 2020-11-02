package com.celerocommerce.handylist.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.celerocommerce.handylist.R
import com.celerocommerce.handylist.databinding.ActivityMainBinding
import com.celerocommerce.handylist.network.responses.ApiResponse
import com.celerocommerce.handylist.ui.BaseActivity
import com.celerocommerce.handylist.ui.main.customer.viewmodel.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel by viewModels<CustomerViewModel>()

    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = (supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.customerFragment))
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            appBarConfiguration
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun expandAppBar() {
        binding.appBar.setExpanded(true)
    }
    override fun displayProgressBar(bool: Boolean) {
        binding.progressBar.visibility = if (bool) View.VISIBLE else View.INVISIBLE
    }
}