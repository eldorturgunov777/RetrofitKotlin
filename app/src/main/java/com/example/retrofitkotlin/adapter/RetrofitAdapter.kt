package com.example.retrofitkotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.CreateActivity
import com.example.retrofitkotlin.MainActivity
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.model.Note


/**
 * Created by Eldor Turgunov on 11.07.2022.
 * Retrofit Kotlin
 * eldorturgunov777@gmail.com
 */
class RetrofitAdapter(context: Context, list: List<Note>, activity: MainActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var context: Context
    var list: List<Note>
    var activity: MainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note: Note = list[position]
        if (holder is ViewHolder) {
            holder.title.text = note.title
            holder.body.text = note.body
            holder.itemView.setOnClickListener { view: View? ->
                val intent = Intent(context, CreateActivity::class.java)
                intent.putExtra("IdExtra", note.id)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            holder.longClick.setOnLongClickListener {
                activity.dialogPoster(note)
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var body: TextView
        var longClick: ConstraintLayout

        init {
            title = itemView.findViewById(R.id.title)
            body = itemView.findViewById(R.id.body)
            longClick = itemView.findViewById(R.id.longClick)
        }
    }

    init {
        this.context = context
        this.list = list
        this.activity = activity
    }
}
