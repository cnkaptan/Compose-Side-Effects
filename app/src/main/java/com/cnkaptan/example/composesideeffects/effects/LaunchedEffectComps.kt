package com.cnkaptan.example.composesideeffects.effects

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cnkaptan.example.composesideeffects.selectRandomText


@Composable
fun LaunchEffectScreen(){
    var mutableName by remember { mutableStateOf("Android") }

    LaunchEffectExample(mutableName) {
        mutableName = selectRandomText()
    }
}

@Composable
fun LaunchEffectExample(name: String, onNameChange: () -> Unit = {}) {
    Log.e("LaunchEffectExample", "Entrance of Composable")
    var count by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Hello $name! = $count")
        Button(onClick = { count++ }) {
            Text(text = "Increase")
        }

        Button(onClick =  onNameChange) {
            Text(text = "NameChange")
        }
    }

    LaunchedEffect(key1 = name, block = {
        Log.e("LaunchedEffectExample", "TimerStarted")
        try {
            com.cnkaptan.example.composesideeffects.startTimer(5000L) { // start a timer for 5 secs
                Log.e("LaunchEffectExample", "Timer ended")
            }
        } catch (ex: Exception) {
            Log.e("LaunchEffectExample", "timer cancelled")
        }
    })
}


@Composable
fun LE_TimerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var timerDuration by remember {
            mutableStateOf(1000L) // default value = 1 sec
        }

        Button({
            timerDuration -= 1000
        }) {
            Text("-1 second")
        }

        Text(timerDuration.toString())

        Button({
            timerDuration += 1000
        }) {
            Text("+1 second")
        }
        Timer(timerDuration = timerDuration)
    }
}

@Composable
fun Timer(timerDuration: Long) {
    LaunchedEffect(key1 = timerDuration, block = {
        Log.e("Timer","Timer Started")
        try {
            com.cnkaptan.example.composesideeffects.startTimer(timerDuration) {
                Log.e("Timer", "Timer ended")
            }
        } catch (ex: Exception) {
            Log.e("Timer","timer cancelled")
        }
    })
}
