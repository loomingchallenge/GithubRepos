package com.githubrepos.adapter.data.datasource.network

class NoInternetException(
    message: String = "Failed to connect to server. Please check your internet connection."
): Exception(message)