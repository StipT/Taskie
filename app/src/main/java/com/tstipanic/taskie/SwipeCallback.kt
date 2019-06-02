package com.tstipanic.taskie

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tstipanic.taskie.ui.adapters.TaskAdapter

class SwipeCallback(val context: Context?, private val adapter: TaskAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeItem(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
    }
}