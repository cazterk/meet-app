package com.example.meet_app.util

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class PullRefreshState(isRefreshing: Boolean) {
    private val _contentOffset = Animatable(0f)

    var isRefreshing by mutableStateOf(isRefreshing)

    var isPullInProcess: Boolean by mutableStateOf(false)
        internal set

    val contentOffset: Float get() = _contentOffset.value

    suspend fun animateOffsetTo(offset: Float) {
        _contentOffset.animateTo(offset)
    }

    suspend fun dispatchScrollDelta(delta: Float) {
        _contentOffset.snapTo(_contentOffset.value + delta)
    }

}

@Composable
fun rememberPullRefreshState(
    isRefreshing: Boolean,
): PullRefreshState = remember {
    PullRefreshState(isRefreshing)
}.apply {
    this.isRefreshing = isRefreshing
}