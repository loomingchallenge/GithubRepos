package com.githubrepos.framework.ui.screens.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

class TopBarController {

    val topBar: MutableState<@Composable () -> Unit> = mutableStateOf(@Composable { })
}

@Composable
internal fun OverflowMenu(
    modifier: Modifier = Modifier,
    content: @Composable (MutableState<Boolean>) -> Unit
) {
    val showMenu = remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        onClick = {
            showMenu.value = !showMenu.value
        }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "more",
        )
    }
    DropdownMenu(
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false }
    ) {
        content(showMenu)
    }
}