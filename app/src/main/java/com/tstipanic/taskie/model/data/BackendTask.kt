package com.tstipanic.taskie.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class BackendTask(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("isFavorite") val isFavorite: Boolean,
    @SerializedName("taskPriority") val taskPriority: Int,
    @SerializedName("isCompleted") val isCompleted: Boolean
)