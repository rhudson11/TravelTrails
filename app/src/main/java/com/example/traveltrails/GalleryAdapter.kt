package com.example.traveltrails

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GalleryAdapter(val locations: List<Location>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.row_gallery, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = locations[position]
        holder.recipeTitle.setText(currentRecipe.title)
        holder.healthScore.setText(currentRecipe.score.toString())

        if(!currentRecipe.imgUrl.isNullOrBlank()) {
            Picasso.get()
                    .load(currentRecipe.imgUrl)
                    .into(holder.icon)
        }

        holder.recipeTitle.setOnClickListener { v: View ->

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle : TextView = itemView.findViewById(R.id.recipeTitle)
        val healthScore: TextView = itemView.findViewById(R.id.healthScore)
        val icon: ImageView = itemView.findViewById(R.id.imageIcon)
    }
}