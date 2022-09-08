package com.cnkaptan.example.composesideeffects.effects

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * - In remember(value) {...}, whenever the value change, we recompose the entire containing composable
 *   function, even when the return value is the same.
 *
 * - remember(listState.firstVisibleItemIndex) continue to change as we scroll the lazy column,
 *   even if the return value is not changed, the entire composable function gets recomposed, despite no UI change.
 */

@Composable
fun TestNoDerivedStateOf() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(1000) { index ->
                Text(text = "Item: $index")
            }
        }

        val showButtonDerive by remember(listState.firstVisibleItemIndex) {
            mutableStateOf(listState.firstVisibleItemIndex > 0)
        }

        Log.e("TestNoDerivedStateOf","Recompose = $showButtonDerive")

        Column {
            AnimatedVisibility(visible = showButtonDerive) {
                Button(onClick = {}) {
                    Text(text = "Row 1 hiding")
                }
            }
        }
    }
}

/**
 * - if we use derivedStateOf as shown below, the recomposition will only happen when the showButtonDerive changed.
 *
 * - The derivedStateOf will not recompose the function it is within as well.
 *   It will only recompose the function that uses its generated mutable value.
 */
@Composable
fun TestDerivedStateOf() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(1000) { index ->
                Text(text = "Item: $index")
            }
        }

        val showButtonDerive by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        Log.e("TestDerivedStateOf", "Recompose = $showButtonDerive")
        Column {
            AnimatedVisibility(showButtonDerive) {
                Button({}) {
                    Text("Row 1 hiding")
                }
            }
        }
    }
}