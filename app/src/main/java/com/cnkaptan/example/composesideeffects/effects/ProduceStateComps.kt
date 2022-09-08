package com.cnkaptan.example.composesideeffects.effects

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * - For LaunchedEffect, if we want to trigger a recomposition, we need to change a Mutable State
 *   variable that is declared external to it.
 *
 * - We can simplify this with produceState, where it will generate mutable state value by itself.
 *
 * - https://miro.medium.com/max/621/1*PyfRGxCNg23JVytHp4v8AA.png
 *
 * - Besides having value to set, it also has awaitDispose to detect termination of the effect,
 *   similar to onDispose of DisposableEffect.
 */

@Composable
fun JustProduceState(){
    var timerStartStop by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val timer by produceState(initialValue = 0, key1 = timerStartStop ){
        val x = (1..10).random()
        var job: Job? = null
        Toast.makeText(context, "Start $x", Toast.LENGTH_SHORT).show()
        if (timerStartStop){
            // Use MainScope to ensure awaitDispose is triggered
            job = MainScope().launch {
                while (true){
                    delay(1000)
                    value++
                }
            }
        }

        awaitDispose {
            Toast.makeText(context, "Done $x", Toast.LENGTH_SHORT).show()
            job?.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Time $timer")
            Button(onClick = {
                timerStartStop = !timerStartStop
            }) {
                Text(if (timerStartStop) "Stop" else "Start")
            }
        }
    }
}