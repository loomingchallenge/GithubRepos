package com.githubrepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.ui.UI
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val errorReporter: ErrorReporter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.OptOutEdgeToEdgeEnforcement, false)
        super.onCreate(savedInstanceState)

        setContent {
            UI()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val isErrorSet = runBlocking { errorReporter.isErrorSet() }
        if (isErrorSet) {
            runBlocking { errorReporter.clearError() }
        } else {
            super.onBackPressed()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    UI()
}