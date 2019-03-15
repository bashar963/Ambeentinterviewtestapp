package com.devbashar.ambeentinterviewtestapp


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.devbashar.ambeentinterviewtestapp.adapters.WifiListAdapter
import com.devbashar.ambeentinterviewtestapp.models.WifiItem
import kotlinx.android.synthetic.main.activity_main.*




@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var wifiItem = mutableListOf<WifiItem>()
    private lateinit var wifiManager : WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWifiResults()
        initButtons()
        initWifiList()

    }

    private fun getWifiResults(){
        progressBar.visibility = View.VISIBLE
        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        this.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)

                } else {
                    intent.getBooleanExtra(Context.WIFI_SERVICE, false)
                }
                if (success) {
                    whenSuccess()
                } else {
                    whenFailure()
                }
            }
        }, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))


        if (!wifiManager.startScan()) {
            Toast.makeText(this, "Scanning....", Toast.LENGTH_SHORT).show()
            whenFailure()
        }
    }

    private fun whenSuccess(){
        val results = wifiManager.scanResults
        if (results.size>0){
            for (i in 0 until  wifiItem.size){
                wifiItem.removeAt(0)
            }
            for (i in 0 until  results.size){
                wifiItem.add(i, WifiItem(results[i].SSID,results[i].BSSID,results[i].level.toString()+" dBm " ))

            }
            wifi_results_list.adapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }
    }

    private fun whenFailure(){
        Toast.makeText(this, "Error while getting wifi results ", Toast.LENGTH_SHORT).show()
    }

    private fun initButtons() {
        updateWifiResults.setOnClickListener {

            getWifiResults()
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
