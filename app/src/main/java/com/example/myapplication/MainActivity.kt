package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var requestQueue: RequestQueue? = null
    private var movieList: MutableList<Movie>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        val bar: android.app.ActionBar? = actionBar
        bar?.title = "Test"
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        requestQueue = VolleySingleton.getmInstance(this)!!.requestQueue
        movieList = ArrayList()
        fetchMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.mainmenu, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.getActionView() as SearchView
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = MovieAdapter(this@MainActivity, movieList!!)
                adapter.filter.filter(newText)
                recyclerView!!.adapter = adapter
                return false
            }
        })
        return true
    }



    private fun fetchMovies() {
        val url = "https://jsonkeeper.com/b/VQGE"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(i)
                        val title = jsonObject.getString("title")
                        val overview = jsonObject.getString("overview")
                        val poster = jsonObject.getString("poster")
                        val rating = jsonObject.getDouble("rating")
                        val movie = Movie(title, poster, overview, rating)
                        movieList!!.add(movie)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    val adapter = MovieAdapter(this@MainActivity, movieList!!)
                    recyclerView!!.adapter = adapter
                }
            }
        ) { error -> Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue!!.add(jsonArrayRequest)
    }
}