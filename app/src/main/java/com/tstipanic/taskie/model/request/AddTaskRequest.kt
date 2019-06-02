package com.tstipanic.taskie.model.request

data class AddTaskRequest(val title: String, val content: String, val taskPriority: Int)