package com.tstipanic.taskie.networking

import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.model.request.AddTaskRequest
import com.tstipanic.taskie.model.request.UpdateTaskRequest
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.DeleteTaskResponse
import com.tstipanic.taskie.model.response.GetTasksResponse
import com.tstipanic.taskie.model.response.LoginResponse
import com.tstipanic.taskie.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/api/register")
    fun register(@Body userData: UserDataRequest): Call<RegisterResponse>

    @POST("/api/login")
    fun login(@Body userData: UserDataRequest): Call<LoginResponse>

    @GET("/api/note")
    fun getTasks(): Call<GetTasksResponse>

    @POST("/api/note")
    fun save(@Body taskData: AddTaskRequest): Call<Task>

    @GET("/api/note/{someId}")
    fun getTask(@Path("someId") someId: String): Call<Task>

    @POST("/api/note/delete")
    fun deleteTask(@Query("id") id: String): Call<DeleteTaskResponse>

    @POST("/api/note/edit")
    fun editNote(@Body updateTaskRequest: UpdateTaskRequest): Call<Task>
}