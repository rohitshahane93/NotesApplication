package com.example.myapplication.util.extensions

import com.example.myapplication.data.dataClasses.Note
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

fun Note.getDisplayTime(): String {
    val time = this.updatedTime ?: this.createdTime
    val date = Date(time)
    val format = SimpleDateFormat("dd MMM yyyy hh:mm a")
    return format.format(date)
}

fun Note.getDisplayTitle(): String {
    return if (this.title.isNotEmpty()) {
        this.title
    } else {
        val lines = this.description.split("\\n")
        lines.first {
            it.isNotBlank()
        }
    }
}