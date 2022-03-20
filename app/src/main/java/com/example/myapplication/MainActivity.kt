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

    /*--- Définition des variables ---*/
    private var recyclerView: RecyclerView? = null
    private var requestQueue: RequestQueue? = null
    private var movieList: MutableList<Movie>? = null

    /*--- Initialisation du RecyclerView avec data + Création de la RequestQueue ---*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        requestQueue = VolleySingleton.getmInstance(this)!!.requestQueue
        movieList = ArrayList()
        fetchMovies()
    }


    /*--- La fonction gére le traitement de recherche dans la toolbar  ---*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
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


    /*--- La fonction qui gére le traitement API , la récuperation du JSON + création une instance de la classe Movie + adapter data dans recyclerView ---*/
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