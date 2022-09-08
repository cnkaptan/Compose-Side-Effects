package com.cnkaptan.example.composesideeffects.effects

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


/**
 * - Without SideEffect, the timer++ will have not to trigger a recomposition, as it was changed while recomposition.
 */
@Composable
fun TryWithoutSideEffect(){
    var timer by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Time $timer")
    }

    Thread.sleep(1000)
    timer++
}


/**
 * - The SideEffect get triggered on every recomposition.
 *   Besides, it is not a coroutine scope (i.e. one cannot use suspend function within).
 *
 * - With SideEffect, the timer++ will trigger recomposition.
 *   This will make it a timer loop (without the use of while(true) as in the LaunchedEffect).
 */
@Composable
fun TryWithSideEffect(){
    var timer by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Time $timer")
    }

    SideEffect {
        Thread.sleep(1000)
        timer++
    }
}