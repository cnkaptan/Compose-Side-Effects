package com.cnkaptan.example.composesideeffects.effects

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cnkaptan.example.composesideeffects.selectRandomText
import kotlinx.coroutines.delay

/**
 * - This side effect will only be launched on the first composition. It will not relaunch on the recomposition.
 *   Nonetheless, we can get it run with the change of provided Key.
 * - It is also a coroutine scope, where we can run suspend function within.
 *   Whatever run within will be canceled when exiting the composable function.
 *
 *   https://medium.com/mobile-app-development-publication/jetpack-compose-side-effects-made-easy-a4867f876928
 *   https://miro.medium.com/max/561/1*OJT5kyi5l4Do1vDzKduTqw.png
 */
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

@Composable
fun CancelDetectLaunchEffect(){
    var timer by remember { mutableStateOf(0) }
    var timerStartStop by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Time = $timer")
            Button(onClick = {
                timerStartStop = !timerStartStop
            }) {
                Text(text = if (timerStartStop) "Stop" else "Start")
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = timerStartStop){
        val x = (1..10).random()
        try {
            while (timerStartStop){
                delay(1000)
                timer++
            }
        }catch (ex: Exception){
            Log.e("CancelDetectLaunchEffect", "$ex")
            Toast.makeText(context, "Oh No $x", Toast.LENGTH_SHORT).show()
        }finally {
            Toast.makeText(context, "Done $x", Toast.LENGTH_SHORT).show()
        }
    }
}
