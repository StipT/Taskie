package com.tstipanic.taskie.ui.task_list_screen.fragments.add_task_dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.common.priorityFactory
import com.tstipanic.taskie.model.data.Priority
import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.persistance.SharedPrefs
import kotlinx.android.synthetic.main.fragment_dialog_new_task.*
import org.koin.android.ext.android.inject


class AddTaskFragmentDialog : DialogFragment(), AddTaskContract.View {

    private val presenter by inject<AddTaskContract.Presenter>()

    private var taskAddedListener: TaskAddedListener? = null

    companion object {
        fun newInstance(): AddTaskFragmentDialog =
            AddTaskFragmentDialog()
    }

    interface TaskAddedListener {
        fun onTaskAdded(task: Task)
    }

    fun setTaskAddedListener(listener: TaskAddedListener) {
        this.taskAddedListener = listener
    }

    override fun onTaskiesReceived(task: Task) {
        taskAddedListener?.onTaskAdded(task)
        closeDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_dialog_new_task, container)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        initUi()
    }

    override fun readInputs() {
        if (isInputEmpty()) {
            context?.displayToast(getString(R.string.emptyFields))
            return
        }

        val title = newTaskTitleInput.text.toString()
        val description = newTaskDescriptionInput.text.toString()
        val priority = prioritySelector.priorityFactory().getValue()
        presenter.saveTask(title, description, priority)
        clearInputs()
    }

    override fun clearInputs() {
        newTaskTitleInput.text.clear()
        newTaskDescriptionInput.text.clear()
        prioritySelector.setSelection(0)
    }

    override fun initUi() {
        saveTaskAction.setOnClickListener { readInputs() }
        context?.let {
            prioritySelector.adapter =
                ArrayAdapter<Priority>(it, R.layout.support_simple_spinner_dropdown_item, Priority.values())
            prioritySelector.setSelection(SharedPrefs.getInt())
        }
    }

    override fun displayToast(text: String) {
        activity?.displayToast(text)
    }

    override fun closeDialog() {
        dismiss()
    }


    private fun isInputEmpty(): Boolean =
        TextUtils.isEmpty(newTaskTitleInput.text) || TextUtils.isEmpty(newTaskDescriptionInput.text)

}