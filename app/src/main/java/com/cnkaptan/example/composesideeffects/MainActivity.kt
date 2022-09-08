package com.cnkaptan.example.composesideeffects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.cnkaptan.example.composesideeffects.effects.*
import com.cnkaptan.example.composesideeffects.ui.theme.ComposeSideEffectsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSideEffectsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    LaunchEffectScreen()
//                    LE_TimerScreen()
//                    CancelDetectLaunchEffect()
//                    RCS_TimerScreen()
//                    RCS_TimerScreen2()
//                    RCS_TimerScreen3()
//                    RCS_Screen()
//                    RUSTwoButtonScreen()
//                    TryWithSideEffect()
//                    JustDisposableEffect()
//                    JustProduceState()
//                    TestDerivedStateOf()
                    TestSnapshotFlow()
                }
            }
        }
    }
}