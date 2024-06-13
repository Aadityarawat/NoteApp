package com.example.noteapp.adapter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.room.entities.Note
import java.util.Random


class NotesAdapter(private val context : Context, val noteListener : NoteItemClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val itemList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()


    class NoteViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val note_layout = itemView.findViewById<LinearLayout>(R.id.itemLL)
        val note_title = itemView.findViewById<TextView>(R.id.title)
        val note_desc = itemView.findViewById<TextView>(R.id.desc)
        val note_date = itemView.findViewById<TextView>(R.id.date)
        val note_fav_false = itemView.findViewById<ImageView>(R.id.favFalseIV)
        val note_fav_true = itemView.findViewById<ImageView>(R.id.favTrueIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = itemList[position]
        holder.note_title.text = current.title
        holder.note_title.isSelected  = true

        holder.note_date.text = current.date
        holder.note_date.isSelected = true
        holder.note_desc.text = current.note
        //holder.note_layout.setBackgroundResource(holder.itemView.resources.getColor(randomColor(), null))
        if ((position % 2) == 0){
            holder.note_layout.background = holder.itemView.resources.getDrawable(randomDrawable(position))
        }else{
            holder.note_layout.background = holder.itemView.resources.getDrawable(randomDrawable(position))
        }
        //holder.note_layout.setBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.note_layout.setOnClickListener {
            noteListener.onItemClicked(itemList[holder.adapterPosition])
        }

        holder.note_layout.setOnLongClickListener{
            noteListener.onLongItemClicked(itemList[holder.adapterPosition], holder.note_layout)
            true
        }

        if (current.favourite == true){
            holder.note_fav_false.visibility = View.GONE
            holder.note_fav_true.visibility = View.VISIBLE
        }else{
            holder.note_fav_false.visibility = View.VISIBLE
            holder.note_fav_true.visibility = View.GONE
        }

        holder.note_fav_false.setOnClickListener {
            holder.note_fav_false.visibility = View.GONE
            holder.note_fav_true.visibility = View.VISIBLE

            noteListener.onFavouriteFalse(itemList[holder.adapterPosition])
        }

        holder.note_fav_true.setOnClickListener {
            holder.note_fav_false.visibility = View.VISIBLE
            holder.note_fav_true.visibility = View.GONE

            noteListener.onFavouriteTrue(itemList[holder.adapterPosition])
        }

    }

    fun filterList(search : String){
        itemList.clear()
        for (item in fullList){
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true){
                itemList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    fun randomDrawable(position: Int) : Int{
        val listLeft = ArrayList<Int>()
        listLeft.add(R.drawable.item_list_bg)
        listLeft.add(R.drawable.item_list_bg_y)
        listLeft.add(R.drawable.item_list_g)
        listLeft.add(R.drawable.item_list_o)
        listLeft.add(R.drawable.item_list_p)

        val listRight = ArrayList<Int>()
        listRight.add(R.drawable.list_item_bg_right)
        listRight.add(R.drawable.list_item_y)
        listRight.add(R.drawable.list_item_o)
        listRight.add(R.drawable.list_item_g)
        listRight.add(R.drawable.list_item_p)

        if ((position % 2) == 0){
            val seed = System.currentTimeMillis()
            val randomIndex = Random(seed).nextInt(listLeft.size)
            return listLeft[randomIndex]
        }
        else{

            val seed = System.currentTimeMillis()
            val randomIndex = Random(seed).nextInt(listRight.size)
            return listRight[randomIndex]
        }

    }

    fun updateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        itemList.clear()
        itemList.addAll(fullList)

        notifyDataSetChanged()
    }

    interface NoteItemClickListener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, linerLayout : LinearLayout)
        fun onFavouriteFalse(note: Note)
        fun onFavouriteTrue(note: Note)
    }

}