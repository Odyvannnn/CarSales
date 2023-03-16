package com.main.carsales.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.main.carsales.R
import com.main.carsales.data.Ad

class AdAdapter(private val adList: ArrayList<Ad>)
    : RecyclerView.Adapter<AdAdapter.AdViewHolder>() {

    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mListener = clickListener
    }

    class AdViewHolder(itemView: View, clickListener: OnItemClickListener)
        :RecyclerView.ViewHolder(itemView){
        val carBrandList: TextView = itemView.findViewById(R.id.car_brand_ad)
        val carModelList: TextView = itemView.findViewById(R.id.car_model_ad)
        val carPriceList: TextView = itemView.findViewById(R.id.car_price_ad)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val adView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return AdViewHolder(adView, mListener)
    }

    override fun getItemCount(): Int {
        return adList.size
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.carBrandList.text = adList[position].car_brand
        holder.carModelList.text = adList[position].car_model
        holder.carPriceList.text = adList[position].price
    }
}