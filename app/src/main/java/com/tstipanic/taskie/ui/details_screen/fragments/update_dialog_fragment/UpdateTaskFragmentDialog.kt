package com.tstipanic.taskie.ui.details_screen.fragments.update_dialog_fragment

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
import com.tstipanic.taskie.model.data.PriorityColor
import com.tstipanic.taskie.model.data.Task
import kotlinx.android.synthetic.main.fragment_dialog_new_task.*
import org.koin.android.ext.android.inject

class UpdateTaskFragmentDialog : DialogFragment(), UpdateTaskContract.View {


    private val presenter by inject<UpdateTaskContract.Presenter>()
    private var taskUpdatedListener: TaskUpdatedListener? = null

    lateinit var task: Task
    var taskId: String = ""


    companion object {
        fun newInstance(): UpdateTaskFragmentDialog {
            return UpdateTaskFragmentDialog()
        }
    }

    interface TaskUpdatedListener {
        fun onTaskUpdated(task: Task?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
    }

    fun setTaskUpdatedListener(listener: TaskUpdatedListener) {
        taskUpdatedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_update_task, container)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.getTaskFromDb(taskId)
        initUi()
        initListeners()

    }

    override fun onTaskUpdated(task: Task) {
        taskUpdatedListener?.onTaskUpdated(task)
    }

    private fun initUi() {
        context?.let {
            prioritySelector.adapter =
                ArrayAdapter<PriorityColor>(it, android.R.layout.simple_spinner_dropdown_item, PriorityColor.values())
            prioritySelector.setSelection(0)
        }

    }

    override fun closeDialog() {
        dismiss()
    }

    override fun fillFields(task: Task) {
        newTaskTitleInput.setText(task.title)
        newTaskDescriptionInput.setText(task.content)
        prioritySelector.setSelection(task.taskPriority - 1)

    }

    private fun initListeners() = saveTaskAction.setOnClickListener { updateTask() }

    override fun updateTask() {
        if (isInputEmpty()) {
            context?.displayToast(R.string.emptyFields)
            return
        }
        val title = newTaskTitleInput.text.toString()
        val description = newTaskDescriptionInput.text.toString()
        val priority = prioritySelector.selectedItemPosition + 1
        presenter.onUpdateTask(taskId, title, description, priority)
        clearUi()


    }

    private fun clearUi() {
        newTaskTitleInput.text.clear()
        newTaskDescriptionInput.text.clear()
        prioritySelector.setSelection(0)
    }

    private fun isInputEmpty(): Boolean =
        TextUtils.isEmpty(newTaskTitleInput.text) || TextUtils.isEmpty(newTaskDescriptionInput.text)

    override fun showToast(text: String) {
        activity?.displayToast(text)
    }
}