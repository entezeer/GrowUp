package com.growup.core.callback

interface BaseCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(message: String)
}