package com.aravindcraj.px.data.models

/**
 *
 * [Result] a generic wrapper for all type of results.
 *
 */

sealed class Result {
    data class Success<out T : Any>(val data: T) : Result()
    data class Error(val exception: Exception) : Result()
}