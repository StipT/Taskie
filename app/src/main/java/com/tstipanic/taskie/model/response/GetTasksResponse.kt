package com.tstipanic.taskie.model.response

import com.tstipanic.taskie.model.data.Task

data class GetTasksResponse(val notes: MutableList<Task>? = mutableListOf())