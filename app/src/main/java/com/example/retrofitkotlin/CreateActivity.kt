package com.example.retrofitkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofitkotlin.model.Note
import com.example.retrofitkotlin.retrofit.RetrofitInstance
import com.example.retrofitkotlin.retrofit.ServiceApi
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateActivity : AppCompatActivity() {
    var title_edit_text: TextInputEditText? = null
    var body_edit_text: TextInputEditText? = null
    var note: Note? = null
    private var idExtra = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initViews()
    }

    private fun initViews() {
        title_edit_text = findViewById(R.id.title_edit_text)
        body_edit_text = findViewById(R.id.body_edit_text)
        val createData = findViewById<Button>(R.id.bt_post)
        createData.setOnClickListener {
            val note = Note(title_edit_text!!.text.toString(), body_edit_text!!.text.toString(),0,0)
            if (idExtra == 0) {
                insertNote(note)
            } else {
                note.id = (idExtra)
                updateNote(idExtra, note)
            }
        }
        if (intent.hasExtra("IdExtra")) {
            idExtra = intent.extras!!.getInt("IdExtra")
            createData.text = "Update"
            getPostApi(idExtra)
        }
    }

    private fun updateNote(idExtra: Int, note: Note) {
        val serviceApi: ServiceApi = RetrofitInstance.getRetrofit().create(ServiceApi::class.java)
        val call: Call<Note> = serviceApi.updatePost(idExtra, note)
        call.enqueue(object : Callback<Note?> {
            override fun onResponse(call: Call<Note?>, response: Response<Note?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateActivity, "Successful Update", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@CreateActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<Note?>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPostApi(idExtra: Int) {
        val serviceApi: ServiceApi = RetrofitInstance.getRetrofit().create(ServiceApi::class.java)
        val call: Call<Note> = serviceApi.getNote(idExtra)
        call.enqueue(object : Callback<Note?> {
            override fun onResponse(call: Call<Note?>, response: Response<Note?>) {
                if (response.isSuccessful) {
                    note = response.body()
                    if (note != null) {
                        title_edit_text?.setText(note!!.title)
                        body_edit_text?.setText(note!!.body)
                    }
                }
            }

            override fun onFailure(call: Call<Note?>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun insertNote(note: Note) {
        val serviceApi: ServiceApi = RetrofitInstance.getRetrofit().create(ServiceApi::class.java)
        val call: Call<Note> = serviceApi.createPost(note)
        call.enqueue(object : Callback<Note?> {
            override fun onResponse(call: Call<Note?>, response: Response<Note?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateActivity, "Successful Create", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@CreateActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<Note?>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}