package com.tstipanic.taskie.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.common.priorityFactory
import com.tstipanic.taskie.model.data.BackendTask
import com.tstipanic.taskie.model.data.Priority
import com.tstipanic.taskie.model.request.AddTaskRequest
import com.tstipanic.taskie.networking.BackendFactory
import kotlinx.android.synthetic.main.fragment_dialog_new_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTaskFragmentDialog : DialogFragment() {

    private var taskAddedListener: TaskAddedListener? = null
    private val interactor by lazy { BackendFactory.getTaskieInteractor() }


    interface TaskAddedListener {
        fun onTaskAdded(task: BackendTask)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
    }

    fun setTaskAddedListener(listener: TaskAddedListener) {
        taskAddedListener = listener
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
        initUi()
    }

    private fun initUi() {
        saveTaskAction.setOnClickListener { saveTask() }
        context?.let {
            prioritySelector.adapter = ArrayAdapter<Priority>(it, R.layout.support_simple_spinner_dropdown_item, Priority.values())
            prioritySelector.setSelection(0)
        }
    }

    private fun saveTask() {
        if (isInputEmpty()) {
            context?.displayToast(getString(R.string.emptyFields))
            return
        }

        val title = newTaskTitleInput.text.toString()
        val description = newTaskDescriptionInput.text.toString()
        val priority = prioritySelector.priorityFactory().getValue()
        interactor.save(AddTaskRequest(title, description, priority), addTaskCallback())

        clearUi()

    }

    private fun addTaskCallback(): Callback<BackendTask> = object : Callback<BackendTask> {
        override fun onFailure(call: Call<BackendTask>?, t: Throwable?) {
            //TODO : handle default error
        }

        override fun onResponse(call: Call<BackendTask>?, response: Response<BackendTask>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun onTaskiesReceived(task: BackendTask) {
        taskAddedListener?.onTaskAdded(task)
        dismiss()
    }

    private fun handleOkResponse(task: BackendTask?) = task?.run { onTaskiesReceived(this) }
    private fun handleSomethingWentWrong() = this.activity?.displayToast("Something went wrong!")

    private fun clearUi() {
        newTaskTitleInput.text.clear()
        newTaskDescriptionInput.text.clear()
        prioritySelector.setSelection(0)
    }

    private fun isInputEmpty(): Boolean =
        TextUtils.isEmpty(newTaskTitleInput.text) || TextUtils.isEmpty(newTaskDescriptionInput.text)

    companion object {
        fun newInstance(): AddTaskFragmentDialog = AddTaskFragmentDialog()
    }
}