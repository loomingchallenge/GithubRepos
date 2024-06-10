package com.githubrepos.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider

class NetworkStateProviderImp(
    private val context: Context
) : NetworkStateProvider {

    private val connectivityManager by lazy { context.getSystemService<ConnectivityManager>()!! }

    override fun hasNetworkConnection(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
