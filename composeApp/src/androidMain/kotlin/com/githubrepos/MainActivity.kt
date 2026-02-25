package com.githubrepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.ui.UI
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val errorReporter: ErrorReporter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.OptOutEdgeToEdgeEnforcement, false)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        setContent {
            UI()
        }
    }
}