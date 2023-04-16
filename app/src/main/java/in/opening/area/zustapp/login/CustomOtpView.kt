package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpCell(modifier: Modifier, value: Char?, isCursorVisible: Boolean = false, obscureText: String?) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                delay(300)
                setCursorSymbol(if (cursorSymbol.isEmpty()) {
                    "|"
                } else {
                    ""
                })
            }
        }
    }

    Box(modifier = modifier) {
        Text(text = if (isCursorVisible) cursorSymbol else if (!obscureText.isNullOrBlank() && value?.toString().isNullOrBlank().not()) obscureText else value?.toString() ?: "",
            style = ZustTypography.body2,
            color = colorResource(id = R.color.app_black),
            modifier = Modifier.align(Alignment.Center))
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PinInput(modifier: Modifier = Modifier, length: Int = 5, value: String = "",
             disableKeypad: Boolean = false, obscureText: String? = "*",onValueChanged: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = value,
        readOnly = disableKeypad,
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.Black, disabledLabelColor = Color.White,
            focusedIndicatorColor = Color.White, unfocusedIndicatorColor = Color.White, backgroundColor = Color.White),
        onValueChange = {
            if (it.length <= length) {
                if (it.all { c -> c in '0'..'9' }) {
                    onValueChanged(it)
                }
                if (it.length >= length) {
                    keyboard?.hide()
                }
            }
        },
        // Hide the text field
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

    Row(modifier = Modifier.fillMaxWidth()) {
        repeat(length) {
            OtpCell(modifier = modifier
                .size(width = 55.dp, height = 55.dp)
                .clip(MaterialTheme.shapes.large)
                .background(color = Color(0xffEfefef),
                    shape = RoundedCornerShape(4.dp))
                .clickable {
                    focusRequester.requestFocus()
                    keyboard?.show()
                }, value = value.getOrNull(it),
                isCursorVisible = value.length == it, obscureText)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}
