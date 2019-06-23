package com.tstipanic.taskie.model.interactor

import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.model.request.AddTaskRequest
import com.tstipanic.taskie.model.request.UpdateTaskRequest
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.DeleteTaskResponse
import com.tstipanic.taskie.model.response.GetTasksResponse
import com.tstipanic.taskie.model.response.LoginResponse
import com.tstipanic.taskie.model.response.RegisterResponse
import retrofit2.Callback

interface Interactor {

    fun getTasks(taskieResponseCallback: Callback<GetTasksResponse>)

    fun register(request: UserDataRequest, registerCallback: Callback<RegisterResponse>)

    fun login(request: UserDataRequest, loginCallback: Callback<LoginResponse>)

    fun save(request: AddTaskRequest, saveCallback: Callback<Task>)

    fun getTask(userId: String, getTaskCallback: Callback<Task>)

    fun delete(id: String, getDeleteTaskCallback: Callback<DeleteTaskResponse>)

    fun editNote(request: UpdateTaskRequest, updateTaskCallback: Callback<Task>)
}