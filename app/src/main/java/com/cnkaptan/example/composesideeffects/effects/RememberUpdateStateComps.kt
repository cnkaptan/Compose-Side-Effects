package com.cnkaptan.example.composesideeffects.effects

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cnkaptan.example.composesideeffects.startTimer

private const val TAG = "RUS_COMPS"

/**
 * - rememberUpdatedState is used when we want to keep an updated reference to a variable in
 *   a long-running side-effect without having the side-effect to restart on recomposition
 */


@Composable
fun RUSTwoButtonScreen() {
    var buttonColor by remember {
        mutableStateOf("Unknown")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                buttonColor = "Red"
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text(text = "Red Button", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                buttonColor = "Black"
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Black Button", color = Color.White)
        }

        RUS_Timer(buttonColor)
    }
}


/**
 * - we have a long running side effect which does not restart even if the composable is recomposed.
 *   In the example below, we have a timer which starts as soon as the composable is on the screen
 *   and completes after 5 seconds. Pay attention to the fact that we have used Unit
 *   (a never-changing constant value) as a the parameter for key1 in LaunchedEffect so that
 *   this side-effect never restarts even if the parent composable is recomposed.
 *
 * - Now we want to access a variable inside this LaunchedEffect block when the timer ends. We can
 *   assume that this variable is passed as a parameter to this composable from the parent composable.
 *
 * - Every time a button is pressed, we update the state variable with the latest colour value which
 *   also triggers a recomposition of the Timer composable below. When the timer ends,
 *   we print the value of buttonColour variable.
 *
 * - even though the user clicked on the buttons multiple time, the colour value was printed as Unknown.
 *   Why did this happen? This is because when the LaunchedEffect block started its execution,
 *   the value of colour variable was set to Unknown at the time. Later,
 *   when the user clicked on the buttons, even though the Timer composable recomposed with the new colour value,
 *   the LaunchedEffect block did not restart (key1 is Unit). Hence, the value of buttonColour
 *   variable inside that block never got updated.
 *
 * - This is the kind of situation wherein rememberUpdatedState comes to our rescue.
 *   We can use rememberUpdatedState to wrap this colour variable in a state object which will always
 *   hold the latest value the Timer composable was composed with. In the example below,
 *   instead of directly referencing the colour variable, we first wrap it in rememberUpdatedState
 *   and use that instance inside the LaunchedEffect block.
 *
 * - rememberUpdatedState is used when we want to keep an updated reference to a variable in a long-running
 *   side-effect without having the side-effect to restart on recomposition
 */
@Composable
fun RUS_Timer(buttonColor: String) {
    val timerDuration = 5000L
    Log.e(TAG, "Composing Timer")
    Log.e(TAG, "Composing timer with color: $buttonColor")

    // Direk buttonColor kullanirsan update almiyor. Onun yerine rememberUpdatedState ile wraple
     val buttonColorUpdated by rememberUpdatedState(newValue = buttonColor)

    // Buda yemiyor cunku ilk compositionda degeri alip cachliyor sonra bu deger recompositionda update edilmiyor
//    val buttonColorUpdated by remember{
//        mutableStateOf(buttonColor)
//    }

    // Bu neden yemiyor pek bir fikrim yok
//    val buttonColorUpdated by remember(buttonColor) {
//        mutableStateOf(buttonColor)
//    }

    LaunchedEffect(key1 = Unit, block = {
        Log.e(TAG, "TimerStarted")
        startTimer(timerDuration) {
            Log.e(TAG, "Timer ended")
            Log.e(TAG, "Last pressed button color was $buttonColorUpdated")
        }
    })
}