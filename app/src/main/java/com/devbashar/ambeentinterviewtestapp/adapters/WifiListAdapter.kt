package com.devbashar.ambeentinterviewtestapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devbashar.ambeentinterviewtestapp.R
import com.devbashar.ambeentinterviewtestapp.models.WifiItem


class WifiListAdapter(private val wifiList: MutableList<WifiItem>): RecyclerView.Adapter<WifiListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val wifiName: TextView = itemView.findViewById(R.id.wifi_name)
        val wifiMAC : TextView = itemView.findViewById(R.id.MAC_adderess)
        val wifiStrength : TextView = itemView.findViewById(R.id.wifi_strength)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wifi_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =wifiList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.wifiName.text = wifiList[position].wifiName
        holder.wifiMAC.text = wifiList[position].wifiMAC
        holder.wifiStrength.text = wifiList[position].wifiStrength

    }
}
