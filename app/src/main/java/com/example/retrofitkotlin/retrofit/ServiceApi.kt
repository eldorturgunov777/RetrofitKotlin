package com.example.retrofitkotlin.retrofit

import com.example.retrofitkotlin.model.Note
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Eldor Turgunov on 11.07.2022.
 * Retrofit Kotlin
 * eldorturgunov777@gmail.com
 */
interface ServiceApi {
    //get
    @GET("posts")
    fun getNotes(): Call<List<Note>>

    //get one note
    @GET("posts/{id}")
    fun getNote(@Path("id") id: Int): Call<Note>

    //insert
    @POST("posts")
    fun createPost(@Body note: Note): Call<Note>

    //update
    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body note: Note): Call<Note>

    //delete
    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Note>
}