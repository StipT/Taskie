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
import com.tstipanic.taskie.common.*
import com.tstipanic.taskie.model.data.BackendTask
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
        initListeners()
        refreshTasks()
    }

    private fun initUi() {
        progress.visible()
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = adapter
        configureSwipeRefresh()
        ItemTouchHelper(SwipeCallback(context, adapter)).attachToRecyclerView(tasksRecyclerView)
    }


    private fun configureSwipeRefresh() {
        swipeLayout.setOnRefreshListener {
            refreshTasks()
            swipeLayout.isRefreshing = false
        }
    }

    private fun initListeners() {
        addTask.setOnClickListener { addTask() }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all -> {
                deleteAlert()
                return true
            }
            R.id.priority_sort -> {
                prioritySort()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun prioritySort() {
        adapter.setData(repository.orderTaskByPriotity().toMutableList())
    }

    private fun deleteAll() {
        repository.deleteAll()
        refreshTasks()
    }

    private fun onItemSelected(task: BackendTask) {
        val detailsIntent = Intent(context, ContainerActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_TYPE, ContainerActivity.SCREEN_TASK_DETAILS)
            putExtra(EXTRA_TASK_ID, task.id)
        }
        startActivity(detailsIntent)
    }

    private fun refreshTasks() {
        progress.gone()
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

    fun deleteAlert() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.myDialog))
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage(getString(R.string.delete_alert_message))
        builder.setIcon(R.drawable.ic_warning)
        builder.setPositiveButton(
            getString(R.string.delete_alert_positive_button),
            { _, _ -> deleteAll() })

        builder.setNegativeButton(getString(R.string.delete_alert_cancel), { _, _ -> DialogInterface.BUTTON_NEGATIVE })
        builder.show()
    }


}