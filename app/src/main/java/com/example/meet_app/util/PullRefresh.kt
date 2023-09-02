package com.example.meet_app.util

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {


    val refreshScope = rememberCoroutineScope()
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isRefreshing by remember { mutableStateOf(refreshing) }

    val state = rememberPullRefreshState(refreshing, onRefresh)
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    offsetY += dragAmount
                    if (offsetY < 50f) {
                        isRefreshing = true
                        refreshScope.launch {

                            onRefresh()


                        }

                    }
                }
            }
            .pullRefresh(state)

    ) {
        content()
        PullRefreshIndicator(
            refreshing,
            state,
            Modifier.align(Alignment.TopCenter)
        )
    }
}
