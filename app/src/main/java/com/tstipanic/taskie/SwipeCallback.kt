package com.tstipanic.taskie

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tstipanic.taskie.Taskie.Companion.instance
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.response.DeleteTaskResponse
import com.tstipanic.taskie.networking.BackendFactory
import com.tstipanic.taskie.persistance.db.TaskRoomRepository
import com.tstipanic.taskie.ui.adapters.TaskAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipeCallback(val context: Context?, private val adapter: TaskAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val interactor by lazy { BackendFactory.getTaskieInteractor() }
    private val repository by lazy { TaskRoomRepository() }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val task = adapter.getTask(viewHolder.adapterPosition)
        interactor.delete(task.id, deleteTaskCallback())
        repository.deleteTask(task)
        adapter.removeItem(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
    }


    private fun deleteTaskCallback() = object : Callback<DeleteTaskResponse> {
        override fun onFailure(call: Call<DeleteTaskResponse>, t: Throwable) {
            Log.d("deleteTaskCallback", "FAILED")
        }

        override fun onResponse(call: Call<DeleteTaskResponse>, response: Response<DeleteTaskResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleSomethingWentWrong() = Log.d("deleteTaskCallback", "BAD Response")

    private fun handleOkResponse(response: Response<DeleteTaskResponse>) {
        instance.displayToast("Item deleted")

    }
}