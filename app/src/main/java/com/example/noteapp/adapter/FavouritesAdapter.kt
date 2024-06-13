package com.example.noteapp.adapter

import android.content.Context
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

class FavouritesAdapter(private val context : Context, val noteListener : FavouritesItemClickListener) : RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>(){
    private val itemList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    class FavouritesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val note_layout = itemView.findViewById<LinearLayout>(R.id.itemLL)
        val note_title = itemView.findViewById<TextView>(R.id.title)
        val note_desc = itemView.findViewById<TextView>(R.id.desc)
        val note_date = itemView.findViewById<TextView>(R.id.date)
        val note_fav_false = itemView.findViewById<ImageView>(R.id.favFalseIV)
        val note_fav_true = itemView.findViewById<ImageView>(R.id.favTrueIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        TODO("Not yet implemented")
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

    interface FavouritesItemClickListener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, linerLayout : LinearLayout)
        fun onFavouriteFalse(note: Note)
        fun onFavouriteTrue(note: Note)
    }
}