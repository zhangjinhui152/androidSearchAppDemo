package com.example.tedy.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tedy.R


class Search_RecyclerViewAdapterK(private val textList: ArrayList<String>) :
    RecyclerView.Adapter<Search_RecyclerViewAdapterK.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.res_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = textList.size ?: 0

    override fun onBindViewHolder(holder: Search_RecyclerViewAdapterK.MyViewHolder, position: Int) {

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val title: TextView = itemView.findViewById(R.id.mine_title)
    }
}