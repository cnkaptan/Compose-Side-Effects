package com.cnkaptan.example.composesideeffects

import kotlinx.coroutines.delay
import kotlin.random.Random

val defaultNameList = mutableListOf("Android", "Compose", "Kotlin", "Launched")

fun selectRandomText(): String{
    val size = defaultNameList.size
    return defaultNameList[Random.nextInt(size)]
}

suspend fun startTimer(time: Long, onTimerEnd: () -> Unit) {
    delay(timeMillis = time)
    onTimerEnd()
}
