package com.example.traveltrails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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
        holder.healthScore.setText("Points: " + currentRecipe.score.toString())

        if(!currentRecipe.imgUrl.isNullOrBlank()) {
            Picasso.get()
                    .load(currentRecipe.imgUrl)
                    .into(holder.icon)
        }

        holder.recipeTitle.setOnClickListener { v: View ->
            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            sceneViewerIntent.data = Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf")
            sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
            v.getContext().startActivity(sceneViewerIntent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle : TextView = itemView.findViewById(R.id.recipeTitle)
        val healthScore: TextView = itemView.findViewById(R.id.healthScore)
        val icon: ImageView = itemView.findViewById(R.id.imageIcon)
    }
}