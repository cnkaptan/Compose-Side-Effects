package com.cnkaptan.example.composesideeffects.effects

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cnkaptan.example.composesideeffects.selectRandomText
import kotlinx.coroutines.*

private const val TAG = "RCS_COMPS"
@Composable
fun RCS_TimerScreen() {
    val scope = rememberCoroutineScope()
    Column {
        Button(onClick = {
            scope.launch {
                startTimer()
            }
        }) {
            Text("Start Timer")
        }
    }
}

// 3 kere tikla sonuncunun jobini tuttugu icin iptal edince sadece sonuncu iptal olacak,
// digerleri calismaya devam edecek
@Composable
fun RCS_TimerScreen2() {
    Log.e(TAG,"RCS_TimerScreen2 Composed")
    val scope = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }

    Column {
        Button(onClick = {
            job = scope.launch {
                startTimer()
            }
        }) {
            Text("Start Timer")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            Log.e(TAG,"Cancelling timer")
            job?.cancel()
        }) {
            Log.e(TAG,"Cancel Timber Button Composed")
            Text("Cancel Timer")
        }

    }
}


@Composable
fun RCS_TimerScreen3() {
    val scope = rememberCoroutineScope()
    Column {
        Button(onClick = {
            scope.launch {
                startTimer()
            }
        }) {
            Text("Start Timer")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            Log.e(TAG,"Cancelling timer")
            scope.cancel()
        }) {
            Text("Cancel Timer")
        }

    }
}

@Composable
fun RCS_Screen(){
    var mutableName by remember { mutableStateOf("Android") }

    RCS_Example(mutableName) {
        mutableName = selectRandomText()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RCS_Example(name: String, onNameChange: () -> Unit = {}) {
    Log.e(TAG, "Entrance of Composable")
    var count by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    // Bunu Composable icinde direk cagiramiyorsun, SuppressLint exlettiriyor.
    // Suppress ekleyip cagirirsanda her recompositionda bu coroutine launch ediliyor.
    // eger Composition fonksiyonun icinde coroutine cagirman gerekiyorsa LaunchEffect Kullan
    scope.launch {
        startTimer()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Hello $name! = $count")
        Button(onClick = { count++ }) {
            Text(text = "Increase")
        }

        Button(onClick =  onNameChange) {
            Text(text = "NameChange")
        }

        Button(onClick = {
            scope.launch {
                startTimer()
            }
        }) {
            Text(text = "Start Timer")
        }
    }
}

private suspend fun startTimer() {
    Log.e(TAG, "Timer started")
    try {
        com.cnkaptan.example.composesideeffects.startTimer(5000) {
            Log.e(TAG, "Timer ended")
        }
    } catch (ex: Exception) {
        Log.e(TAG, "Timer cancelled")
    }
}