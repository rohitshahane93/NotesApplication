package com.example.myapplication.util.extensions

import com.example.myapplication.data.dataClasses.Note

fun List<Note>.toNewLineSeparatedString(): String {
    return this.joinToString(separator = "\n") {
        "${it.title}"
    }
}

fun List<Note>.ids(): List<String> {
    return this.map {
        it.id
    }
}