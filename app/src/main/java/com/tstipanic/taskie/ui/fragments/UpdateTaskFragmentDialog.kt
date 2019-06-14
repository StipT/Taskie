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
import com.tstipanic.taskie.Taskie
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.data.BackendTask
import com.tstipanic.taskie.model.data.PriorityColor
import com.tstipanic.taskie.model.request.UpdateTaskRequest
import com.tstipanic.taskie.networking.BackendFactory
import com.tstipanic.taskie.persistance.db.TaskRoomRepository
import kotlinx.android.synthetic.main.fragment_dialog_new_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTaskFragmentDialog : DialogFragment() {


    private var taskUpdatedListener: TaskUpdatedListener? = null
    private val interactor = BackendFactory.getTaskieInteractor()
    private val repository by lazy { TaskRoomRepository() }
    var taskId: String = ""
    lateinit var task: BackendTask

    companion object {
        fun newInstance(): UpdateTaskFragmentDialog {
            return UpdateTaskFragmentDialog()
        }
    }

    interface TaskUpdatedListener {
        fun onTaskUpdated(task: BackendTask?)
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
        fillFields(repository.getTask(taskId))

        //interactor.getTask(taskId, getTaskCallback())
        initUi()
        initListeners()
    }

    private fun initUi() {
        context?.let {
            prioritySelector.adapter =
                ArrayAdapter<PriorityColor>(it, android.R.layout.simple_spinner_dropdown_item, PriorityColor.values())
            prioritySelector.setSelection(0)
        }

    }

    fun fillFields(task: BackendTask) {
        newTaskTitleInput.setText(task.title)
        newTaskDescriptionInput.setText(task.content)
        prioritySelector.setSelection(task.taskPriority - 1)

    }

    private fun initListeners() = saveTaskAction.setOnClickListener { updateTask() }

    private fun updateTask() {
        if (isInputEmpty()) {
            context?.displayToast(R.string.emptyFields)
            return
        }


        val title = newTaskTitleInput.text.toString()
        val description = newTaskDescriptionInput.text.toString()
        val priority = prioritySelector.selectedItemPosition + 1
        clearUi()
        interactor.editNote(UpdateTaskRequest(taskId, title, description, priority), updateTaskCallback())
        repository.updateTask(taskId, title, description, priority)
    }

    private fun updateTaskCallback() = object : Callback<BackendTask> {
        override fun onFailure(call: Call<BackendTask>, t: Throwable) {
            onTaskiesUpdated(repository.getTask(taskId))
        }

        override fun onResponse(call: Call<BackendTask>, response: Response<BackendTask>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse2(response)
                    else -> handleSomethingWentWrong2()
                }
            }
        }

    }

    private fun handleSomethingWentWrong2() {
        onTaskiesUpdated(repository.getTask(taskId))

    }


    private fun handleOkResponse2(response: Response<BackendTask>) {
        response.body()?.run {
            onTaskiesUpdated(this)
        }
    }

    private fun onTaskiesUpdated(backendTask: BackendTask?) {
        taskUpdatedListener?.onTaskUpdated(backendTask)
        dismiss()
    }


    private fun clearUi() {
        newTaskTitleInput.text.clear()
        newTaskDescriptionInput.text.clear()
        prioritySelector.setSelection(0)
    }

    private fun isInputEmpty(): Boolean = TextUtils.isEmpty(newTaskTitleInput.text) || TextUtils.isEmpty(
        newTaskDescriptionInput.text
    )


    private fun getTaskCallback(): Callback<BackendTask> = object : Callback<BackendTask> {
        override fun onFailure(call: Call<BackendTask>, t: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call<BackendTask>, response: Response<BackendTask>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleSomethingWentWrong() = Taskie.instance.displayToast("Something went wrong!")

    private fun handleOkResponse(response: Response<BackendTask>) {

        response.body()?.run {
            newTaskTitleInput.setText(this.title)
            newTaskDescriptionInput.setText(this.content)
            prioritySelector.setSelection(this.taskPriority - 1)
            task = this

        }

    }
}