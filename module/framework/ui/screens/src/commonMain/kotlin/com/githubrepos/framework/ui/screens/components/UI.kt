package com.githubrepos.framework.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.adapter.presentation.error.getMessage
import com.githubrepos.framework.ui.screens.navigation.NavigationController
import com.githubrepos.framework.ui.screens.theme.GrayTransparent
import com.githubrepos.framework.ui.screens.theme.MainTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun UI(navigationController: NavigationController) {
    MainTheme(darkTheme = false) {
        val topBarController: TopBarController = remember { TopBarController() }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(
                WindowInsets.displayCutout
            )
        ) {
            Scaffold(
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
                topBar = topBarController.topBar.value
            ) { innerPadding ->
                navigationController.navHost(Modifier.padding(innerPadding), topBarController)
            }
            Error(navigationController.errorReporter)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Error(errorReporter: ErrorReporter) {
    val errorData by errorReporter.error.collectAsState() // TODO collectAsStateWithLifecycle requires at least Compose UI 1.7.0-alpha05. https://stackoverflow.com/questions/78490378/java-lang-illegalstateexception-compositionlocal-locallifecycleowner-not-presen
    errorData?.let { error ->
        val snackbarData = object : SnackbarData {
            override val actionLabel: String? = null
            override val duration: SnackbarDuration = SnackbarDuration.Indefinite
            override val message: String = error.getMessage()
            override fun dismiss() { }
            override fun performAction() { }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(GrayTransparent)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    error.onErrorDismissed?.let { it() }
                    GlobalScope.launch {
                        errorReporter.clearError()
                    }
                }
            )
        ) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                snackbarData = snackbarData,
                backgroundColor = MaterialTheme.colors.error,
                contentColor = MaterialTheme.colors.onError
            )
        }
    }
}