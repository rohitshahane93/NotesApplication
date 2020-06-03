package com.example.myapplication.views.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.util.color.ColorUtil
import com.example.myapplication.util.extensions.getDisplayTime
import com.example.myapplication.util.extensions.getDisplayTitle

class NotesAdapter(
    private var context: Context,
    private var list: List<Note>,
    private val listener: NoteClickListener
) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var isSelectionMode = false

    fun startSelectionMode() {
        isSelectionMode = true
    }

    fun finishSelectionMode() {
        isSelectionMode = false
    }

    fun notifyDataSetChanged(list: List<Note>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_note_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = list[position]
        holder.txtTitle.text = note.getDisplayTitle()
        holder.txtDesc.text = note.description
        holder.txtTime.text = note.getDisplayTime()
        if (note.isSelected) {
            holder.cardView.setCardBackgroundColor(
                ColorUtil.getColorWithAlpha(
                    ContextCompat.getColor(
                        context,
                        R.color.colorAccent
                    ), 0.2f
                )
            )
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                setSelected(note)
            } else {
                listener.onNoteClick(note)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (isSelectionMode) {
                setSelected(note)
                true
            } else {
                listener.onNoteLongPress(note)
            }
        }
    }

    private fun setSelected(note: Note) {
        val listItem = list.find {
            it.id == note.id
        }
        listItem?.let { item ->
            val index = list.indexOf(item)
            item.isSelected = !item.isSelected
            notifyItemChanged(index)
            listener.onSelectedCountChanged(getSelected().count())
        }
    }

    fun unSelectAll() {
        list.forEach {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun getSelected(): List<Note> {
        return list.filter {
            it.isSelected
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txt_title)
        val txtDesc: TextView = view.findViewById(R.id.txt_desc)
        val txtTime: TextView = view.findViewById(R.id.txt_time)
        val cardView: CardView = view.findViewById(R.id.cardView)
    }

    interface NoteClickListener {
        fun onNoteClick(note: Note)
        fun onNoteLongPress(note: Note): Boolean
        fun onSelectedCountChanged(count: Int)
    }
}