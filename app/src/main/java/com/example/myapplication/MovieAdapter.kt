package com.example.myapplication

import android.R.attr
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.R.attr.data
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList


class MovieAdapter(context: Context, private var movies: MutableList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>(), Filterable {
    val context: Context
    var movieList: MutableList<Movie>
    val movieListFull: MutableList<Movie>

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MovieHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.items, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: MovieHolder, position: Int) {
        val movie = movieList[position]
        holder.rating.rating = movie.rating.toFloat()
        holder.title.text = movie.title
        holder.overview.text = movie.overview
        Glide.with(context).load(movie.poster).into(holder.imageView)
        holder.constraintLayout.setOnClickListener(View.OnClickListener {
            val intent = Intent(context,DetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", movie.title)
            bundle.putString("overview", movie.overview)
            bundle.putString("poster", movie.poster)
            bundle.putDouble("rating", movie.rating)
            intent.putExtras(bundle)
            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class MovieHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var title: TextView
        var overview: TextView
        var rating: RatingBar
        var constraintLayout: ConstraintLayout

        init {
            imageView = itemView.findViewById(R.id.imageview)
            title = itemView.findViewById(R.id.title_tv)
            overview = itemView.findViewById(R.id.overview_tv)
            rating = itemView.findViewById(R.id.rating)
            constraintLayout = itemView.findViewById(R.id.main_layout)
        }
    }

    init {
        this.context = context
        movieList = movies
        movieListFull = ArrayList(movieList)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    movieList = movies
                } else {
                    val resultList = ArrayList<Movie>()
                    for (row in movieList) {
                        if (row.title.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    movieList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = movieList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movieList = results?.values as ArrayList<Movie>
                notifyDataSetChanged()
            }

        }
    }
}




