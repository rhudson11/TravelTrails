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
        val currentLocation = locations[position]
        holder.placeTitle.setText(currentLocation.title)
        holder.numPoints.setText("Points: " + currentLocation.score.toString())

        if(!currentLocation.imgUrl.isNullOrBlank()) {
            Picasso.get()
                    .load(currentLocation.imgUrl)
                    .into(holder.icon)
        }

        holder.placeTitle.setOnClickListener { v: View ->
            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            if(holder.placeTitle.text.contains("Lincoln"))
                sceneViewerIntent.data = Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=http://coltrane.cs.seas.gwu.edu:8080/location/4/model.gltf")
            else
                sceneViewerIntent.data = Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=http://coltrane.cs.seas.gwu.edu:8080/location/1/model.gltf")
            sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
            v.getContext().startActivity(sceneViewerIntent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeTitle : TextView = itemView.findViewById(R.id.placeTitle)
        val numPoints: TextView = itemView.findViewById(R.id.numPoints)
        val icon: ImageView = itemView.findViewById(R.id.imageIcon)
    }
}