package com.example.noteapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.others.NetworkBroadcasr
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash : AppCompatActivity() {
    private lateinit var networkBroadCast: NetworkBroadcasr
    private var full_screen_destination : Int? = null
    private lateinit var navController : NavController
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        networkBroadCast = NetworkBroadcasr()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph_Container) as NavHostFragment
         navController = navHostFragment.navController
        binding.bottomnavigationbar.setupWithNavController(navController)
        //NavigationUI.setupWithNavController(binding.bottomnavigationbar, navController, false)
        /*binding.bottomnavigationbar.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController, false)
                Log.d("top level screen ","${item.itemId}")
                true
            }
        }*/

        backStackHandler()
    }

    private fun backStackHandler() {
        binding.bottomnavigationbar.setOnItemSelectedListener { item ->
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()

            when(item.itemId){
                R.id.favouritesFragment -> {
                    navController.navigate(R.id.favouritesFragment, null, navOptions)
                    true
                }
                R.id.quotesFragment -> {
                    navController.navigate(R.id.quotesFragment, null, navOptions)
                    true
                }
                R.id.mainFragment -> {
                    navController.navigate(R.id.mainFragment, null, navOptions)
                    true
                }
                else -> false
            }
        }
    }


    override fun onStart() {
        registerReceiver(networkBroadCast, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkBroadCast)
    }
}