package com.tstipanic.taskie.model.data

import androidx.annotation.ColorRes
import com.tstipanic.taskie.R


enum class Priority(@ColorRes private val colorRes: Int) {
    LOW(R.color.colorLow),
    MEDIUM(R.color.colorMedium),
    HIGH(R.color.colorHigh);

    fun getColor(): Int = colorRes
}