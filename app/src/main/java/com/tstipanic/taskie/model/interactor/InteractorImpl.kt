package com.tstipanic.taskie.model.interactor

import com.tstipanic.taskie.model.data.BackendTask
import com.tstipanic.taskie.model.request.AddTaskRequest
import com.tstipanic.taskie.model.request.UpdateTaskRequest
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.DeleteTaskResponse
import com.tstipanic.taskie.model.response.GetTasksResponse
import com.tstipanic.taskie.model.response.LoginResponse
import com.tstipanic.taskie.model.response.RegisterResponse
import com.tstipanic.taskie.networking.ApiService
import retrofit2.Callback

class InteractorImpl(private val apiService: ApiService) : Interactor {

    override fun getTasks(taskieResponseCallback: Callback<GetTasksResponse>) {
        apiService.getTasks().enqueue(taskieResponseCallback)
    }

    override fun register(request: UserDataRequest, registerCallback: Callback<RegisterResponse>) {
        apiService.register(request).enqueue(registerCallback)
    }

    override fun login(request: UserDataRequest, loginCallback: Callback<LoginResponse>) {
        apiService.login(request).enqueue(loginCallback)
    }

    override fun save(request: AddTaskRequest, saveCallback: Callback<BackendTask>) {
        apiService.save(request).enqueue(saveCallback)
    }

    override fun getTask(userId: String, getTaskCallback: Callback<BackendTask>) {
        apiService.getTask(userId).enqueue(getTaskCallback)
    }

    override fun delete(id: String, getDeleteTaskCallback: Callback<DeleteTaskResponse>) {
        apiService.deleteTask(id).enqueue(getDeleteTaskCallback)
    }

    override fun editNote(request: UpdateTaskRequest, updateTaskCallback: Callback<BackendTask>) {
        apiService.editNote(request).enqueue(updateTaskCallback)
    }
}