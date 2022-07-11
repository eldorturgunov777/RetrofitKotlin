package com.example.retrofitkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.adapter.RetrofitAdapter
import com.example.retrofitkotlin.model.Note
import com.example.retrofitkotlin.retrofit.RetrofitInstance
import com.example.retrofitkotlin.retrofit.ServiceApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RetrofitAdapter? = null
    private var note: List<Note> = ArrayList<Note>()
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        createData()
    }

    private fun createData() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)
        progressBar = findViewById(R.id.progress_bar)
        getNotes()
    }

    private fun getNotes() {
        val serviceApi: ServiceApi = RetrofitInstance.getRetrofit().create(ServiceApi::class.java)
        val call: Call<List<Note>> = serviceApi.getNotes()
        call.enqueue(object : Callback<List<Note>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                progressBar!!.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    note = response.body()!!
                    adapter = RetrofitAdapter(this@MainActivity, note, this@MainActivity)
                    recyclerView!!.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
                progressBar!!.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.d("TAG", "onFailure" + t.localizedMessage)
            }

        })
    }

    fun dialogPoster(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Poster")
            .setMessage("Are you sure you want to delete this poster?")
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                val serviceApi: ServiceApi =
                    RetrofitInstance.getRetrofit().create(ServiceApi::class.java)
                val call: Call<Note> = serviceApi.deletePost(note.id)
                call.enqueue(object : Callback<Note> {
                    @SuppressLint("NotifyDataSetChanged", "NewApi")
                    override fun onResponse(call: Call<Note>, response: Response<Note>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@MainActivity,
                                "Successful Delete",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            adapter!!.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<Note>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}