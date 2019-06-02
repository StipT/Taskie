package com.tstipanic.taskie.persistance.db

import androidx.room.TypeConverter
import com.tstipanic.taskie.model.data.Priority

class TypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromPriority(value: Priority): Int = when (value) {
            Priority.HIGH -> 3
            Priority.MEDIUM -> 2
            Priority.LOW -> 1
        }

        @TypeConverter
        @JvmStatic
        fun toPriority(value: Int): Priority =
            when (value) {
                3 -> Priority.HIGH
                2 -> Priority.MEDIUM
                1 -> Priority.LOW
                else -> Priority.LOW
            }
    }
}