package com.aravindcraj.px.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.io(body: suspend (CoroutineScope) -> Unit): Job {
    return viewModelScope.launch {
        body.invoke(this)
    }
}