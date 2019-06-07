package com.tstipanic.taskie.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.tstipanic.taskie.R
import com.tstipanic.taskie.SwipeCallback
import com.tstipanic.taskie.common.EXTRA_SCREEN_TYPE
import com.tstipanic.taskie.common.EXTRA_TASK_ID
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.data.BackendTask
import com.tstipanic.taskie.model.response.DeleteTaskResponse
import com.tstipanic.taskie.model.response.GetTasksResponse
import com.tstipanic.taskie.networking.BackendFactory
import com.tstipanic.taskie.persistance.db.TaskRoomRepository
import com.tstipanic.taskie.ui.activities.ContainerActivity
import com.tstipanic.taskie.ui.adapters.TaskAdapter
import com.tstipanic.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tasks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TasksFragment : BaseFragment(), AddTaskFragmentDialog.TaskAddedListener {

    private val repository = TaskRoomRepository()
    private val adapter by lazy { TaskAdapter { onItemSelected(it) } }
    private val interactor by lazy { BackendFactory.getTaskieInteractor() }

    companion object {
        fun newInstance(): Fragment {
            return TasksFragment()
        }
    }


    override fun getLayoutResourceId() = R.layout.fragment_tasks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initUi()

        refreshTasks()
    }

    private fun initUi() {
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = adapter
        configureSwipeRefresh()
        addTask.setOnClickListener { addTask() }
        ItemTouchHelper(SwipeCallback(context, adapter)).attachToRecyclerView(tasksRecyclerView)
    }


    private fun configureSwipeRefresh() {
        swipeLayout.setOnRefreshListener {
            refreshTasks()
            swipeLayout.isRefreshing = false
        }
    }


    override fun onResume() {
        super.onResume()
        refreshTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_all -> {
            deleteAlert()
        }
        R.id.priority_sort -> {
            prioritySort()
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun prioritySort(): Boolean {
        adapter.setData(repository.orderTaskByPriotity().toMutableList())
        return true
    }

    private fun deleteAll() {
        repository.deleteAll()
        for (task: BackendTask in adapter.getData()) {
            interactor.delete(task.id, deleteTaskCallback())
        }
        adapter.setData(repository.getAll().toMutableList())
    }

    private fun deleteTaskCallback() = object : Callback<DeleteTaskResponse> {
        override fun onFailure(call: Call<DeleteTaskResponse>, t: Throwable) {
            Log.e("deleteTaskCallback", "Delettion failed")
        }

        override fun onResponse(call: Call<DeleteTaskResponse>, response: Response<DeleteTaskResponse>) {
            Log.e("deleteTaskCallback", "Delettion failed  " + response.body())
        }
    }

    private fun onItemSelected(task: BackendTask) {
        val detailsIntent = Intent(context, ContainerActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_TYPE, ContainerActivity.SCREEN_TASK_DETAILS)
            putExtra(EXTRA_TASK_ID, task.id)
        }
        startActivity(detailsIntent)
    }

    private fun refreshTasks() {
        interactor.getTasks(getTaskieCallback())
    }

    private fun getTaskieCallback(): Callback<GetTasksResponse> = object : Callback<GetTasksResponse> {
        override fun onFailure(call: Call<GetTasksResponse>, t: Throwable) {
            Log.d("GetTasksResponse", "Failed")
        }

        override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkResponse(response: Response<GetTasksResponse>) {
        response.body()?.notes?.run {
            adapter.setData(this)
            for (task: BackendTask in this) {
                repository.insertTask(task)
            }
        }
    }

    private fun handleSomethingWentWrong() = this.activity?.displayToast("Something went wrong!")

    private fun addTask() {
        val dialog = AddTaskFragmentDialog.newInstance()
        dialog.setTaskAddedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskAdded(task: BackendTask) {
        repository.insertTask(task)
        refreshTasks()
    }

    private fun deleteAlert(): Boolean {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.myDialog))
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage(getString(R.string.delete_alert_message))
        builder.setIcon(R.drawable.ic_warning)
        builder.setPositiveButton(
            getString(R.string.delete_alert_positive_button),
            { _, _ -> deleteAll() })

        builder.setNegativeButton(getString(R.string.delete_alert_cancel), { _, _ -> DialogInterface.BUTTON_NEGATIVE })
        builder.show()
        return true
    }


}