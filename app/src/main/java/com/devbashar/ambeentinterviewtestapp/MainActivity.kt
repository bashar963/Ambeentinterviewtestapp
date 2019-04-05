package com.devbashar.ambeentinterviewtestapp


import android.Manifest
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.devbashar.ambeentinterviewtestapp.adapters.WifiListAdapter
import com.devbashar.ambeentinterviewtestapp.models.WifiItem
import com.devbashar.ambeentinterviewtestapp.viewModel.WifiItemViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.github.jksiezni.permissive.Permissive



class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var wifiItem = mutableListOf<WifiItem>()
    private lateinit var wifiViewModel:WifiItemViewModel
    private lateinit var wifiResult:LiveData<MutableList<WifiItem>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wifiViewModel = ViewModelProviders.of(this).get(WifiItemViewModel::class.java)

        askPermission()


        initButtons()
        initWifiList()

    }
    private fun askPermission() {
        Permissive.Request(Manifest.permission.ACCESS_FINE_LOCATION)
            .whenPermissionsGranted {
                getWifiResults()
            }
            .whenPermissionsRefused {
                Toast.makeText(this@MainActivity,"Please give a location permission in order tha app to work",Toast.LENGTH_LONG).show()
            }
            .execute(this)
    }

    private fun getWifiResults(){
        wifiResult = wifiViewModel.getWifiResults()
        wifiResult.observe(this, Observer {
            wifiItem.removeAll(wifiItem)
            wifiItem.addAll(it!!)
            wifi_results_list.adapter!!.notifyDataSetChanged()
            viewAdapter.notifyDataSetChanged()
        })
    }



    private fun initButtons() {
        updateWifiResults.setOnClickListener {
            askPermission()
        }
    }

    private fun initWifiList() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = WifiListAdapter(wifiItem)

        wifi_results_list.apply {
            adapter = viewAdapter
            layoutManager = viewManager
            setHasFixedSize(true)
        }
    }


}
