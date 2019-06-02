package com.tstipanic.taskie.model.response

import com.tstipanic.taskie.model.data.BackendTask

data class GetTasksResponse(val notes: MutableList<BackendTask>? = mutableListOf())