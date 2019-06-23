package com.tstipanic.taskie.ui.task_list_screen.fragments

import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.EXTRA_SCREEN_TYPE
import com.tstipanic.taskie.common.EXTRA_TASK_ID
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.ui.base.BaseFragment
import com.tstipanic.taskie.ui.details_screen.activity.ContainerActivity
import com.tstipanic.taskie.ui.task_list_screen.adapters.TaskAdapter
import com.tstipanic.taskie.ui.task_list_screen.fragments.add_task_dialog.AddTaskFragmentDialog
import kotlinx.android.synthetic.main.fragment_tasks.*
import org.koin.android.ext.android.inject


class TasksFragment : BaseFragment(), TasksContract.View, AddTaskFragmentDialog.TaskAddedListener {
    private val presenter by inject<TasksContract.Presenter>()
    private val adapter by lazy { TaskAdapter { onItemSelected(it) } }


    companion object {
        fun newInstance(): Fragment {
            return TasksFragment()
        }
    }

    override fun getLayoutResourceId() = R.layout.fragment_tasks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.setView(this)
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initUi()
        presenter.refreshTasks()
    }


    override fun initUi() {
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = adapter
        configureSwipeRefresh()
        addTask.setOnClickListener { addTask() }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(tasksRecyclerView)
    }

    override fun setList(list: List<Task>) {
        adapter.setData(list.toMutableList())
    }

    override fun displayToast(text: String) {
        activity?.displayToast(text)
    }

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager = activity?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    private fun configureSwipeRefresh() {
        swipeLayout.setOnRefreshListener {
            presenter.refreshTasks()
            swipeLayout.isRefreshing = false
        }
    }


    override fun onResume() {
        super.onResume()
        presenter.getTasksFromLocalDatabase()
        presenter.refreshTasks()
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
            presenter.getTasksByPriority()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun onItemSelected(task: Task) {
        val detailsIntent = Intent(context, ContainerActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_TYPE, ContainerActivity.SCREEN_TASK_DETAILS)
            putExtra(EXTRA_TASK_ID, task.id)
        }
        startActivity(detailsIntent)
    }

    private fun addTask() {
        val dialog = AddTaskFragmentDialog.newInstance()
        dialog.setTaskAddedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskAdded(task: Task) {
        presenter.insertTaskInLocalDb(task)
        presenter.refreshTasks()
    }

    private fun deleteAlert(): Boolean {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.myDialog))
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage(getString(R.string.delete_alert_message))
        builder.setIcon(R.drawable.ic_warning)
        builder.setPositiveButton(
            getString(R.string.delete_alert_positive_button),
            { _, _ -> presenter.deleteAll() })

        builder.setNegativeButton(getString(R.string.delete_alert_cancel), { _, _ -> DialogInterface.BUTTON_NEGATIVE })
        builder.show()
        return true
    }

    private val swipeCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = adapter.getTask(viewHolder.adapterPosition)
                presenter.deleteTask(task.id)
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }

}