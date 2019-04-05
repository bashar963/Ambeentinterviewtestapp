package com.devbashar.ambeentinterviewtestapp.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import com.devbashar.ambeentinterviewtestapp.models.WifiItem

@Suppress("DEPRECATION")
class WifiItemViewModel(application: Application):AndroidViewModel(application) {

    private  var wifiItem = MutableLiveData<MutableList<WifiItem>>()
    private lateinit var wifiManager : WifiManager
    private val applicationInstance = application


     fun getWifiResults():MutableLiveData<MutableList<WifiItem>>{
        wifiManager = applicationInstance.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        applicationInstance.registerReceiver(object : BroadcastReceiver() {
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
            Toast.makeText(applicationInstance.applicationContext, "Scanning....", Toast.LENGTH_SHORT).show()
            whenFailure()
        }
        return wifiItem
    }

    private fun whenSuccess(){
        val results = wifiManager.scanResults
        val wifiList= mutableListOf<WifiItem>()
        if (results.size>0){
            for (i in 0 until  results.size){
                wifiList.add(i, WifiItem(results[i].SSID,results[i].BSSID,results[i].level.toString()+" dBm " ))
            }
            wifiItem.value=wifiList
        }

    }
    private fun whenFailure(){
        Toast.makeText(applicationInstance.applicationContext, "Error while getting wifi results ", Toast.LENGTH_SHORT).show()
    }
}