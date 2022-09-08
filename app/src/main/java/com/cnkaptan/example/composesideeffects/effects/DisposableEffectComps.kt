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

/**
 * - Like LaunchedEffect, it is triggered once per the first composition or changed through the Key1.
 *   On retriggered, the previous launch effect will trigger onDisposefunction call.
 *
 * - This allows one to dispose of any object as needed. Note, it’s also different from LaunchedEffect
 *   as even if the DisposableEffect completed its functionality, the previous onDispose is still
 *   triggered when the effect is restarted.
 */

@Composable
fun JustDisposableEffect() {
    var timerStartStop by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                timerStartStop = !timerStartStop
            }) {
                Text(text = if (timerStartStop) "Stop" else "Start")
            }
        }
    }

    val context = LocalContext.current

    DisposableEffect(key1 = timerStartStop) {
        val x = (1..10).random()
        Toast.makeText(context, "Start $x", Toast.LENGTH_SHORT).show()

        onDispose {
            Toast.makeText(context, "Stop $x", Toast.LENGTH_SHORT).show()
        }
    }
}