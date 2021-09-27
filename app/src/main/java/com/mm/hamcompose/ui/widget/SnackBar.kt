package com.mm.hamcompose.ui.widget

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import com.mm.hamcompose.theme.HamTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SNACK_INFO = ""
const val SNACK_WARN = " "
const val SNACK_ERROR = "  "
const val SNACK_SUCCESS = "OK"

@Composable
fun HamSnackBar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        backgroundColor = when (data.actionLabel) {
            SNACK_INFO -> HamTheme.colors.themeUi
            SNACK_WARN -> HamTheme.colors.warn
            SNACK_ERROR -> HamTheme.colors.error
            SNACK_SUCCESS -> HamTheme.colors.success
            else -> HamTheme.colors.themeUi
        },
        actionColor = HamTheme.colors.textPrimary,
        contentColor = HamTheme.colors.textPrimary,
    )
}

fun popupSnackBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    label: String,
    message: String,
    onDismissCallback: () -> Unit = {}
) {
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(actionLabel = label, message = message)
        onDismissCallback.invoke()
    }

}