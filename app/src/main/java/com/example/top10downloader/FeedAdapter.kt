package com.example.top10downloader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.top10downloader.DataObjects.FeedEntry

class ViewHolder(var view: View){
    var tvName: TextView = view.findViewById(R.id.tvName)
    var tvArtist: TextView = view.findViewById(R.id.tvArtist)
    var tvSummary : TextView = view.findViewById(R.id.tvSummary)
}


class FeedAdapter(context:Context,var resource: Int, private var application : ArrayList<FeedEntry>) : ArrayAdapter<FeedEntry>(context, resource) {

    val TAG = "FeedAdapter"
    val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return application.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder :ViewHolder
        if(convertView == null){
            view  = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
            Log.d(TAG, "Using convertView for creating new view ")
        }
//        var tvName : TextView = view.findViewById(R.id.tvName)
//        var tvArtist = view.findViewById<TextView>(R.id.tvArtist)
//        var tvSummary = view.findViewById<TextView>(R.id.tvSummary)

        var currentApplication = application.get(position)

        viewHolder.tvName.text = currentApplication.name
        viewHolder.tvArtist.text = currentApplication.artist
        viewHolder.tvSummary.text = currentApplication.summary

        return view

    }
}