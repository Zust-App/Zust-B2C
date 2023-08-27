package `in`.opening.area.zustapp.search.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.search.SearchProductActivity
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.uiModels.productList.ProductListUi
import `in`.opening.area.zustapp.viewmodels.SearchProductViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBarUi(modifier: Modifier, modifier1: Modifier,
                viewModel: SearchProductViewModel) {
    var searchInput by rememberSaveable {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .background(color = colorResource(id = R.color.white),
            shape = RoundedCornerShape(8.dp))
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(8.dp))) {
        val (searchTextField, clearIcon) = createRefs()
        BasicTextField(
            modifier = modifier1
                .padding(12.dp)
                .focusRequester(focusRequester)
                .constrainAs(searchTextField) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(clearIcon.start, dp_4)
                    width = Dimension.fillToConstraints
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboard?.hide()
            }),
            value = searchInput,
            onValueChange = { newText ->
                searchInput = newText
            },
            textStyle = ZustTypography.bodyMedium,
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (searchInput.isEmpty()) {
                        Text(
                            text = "Search products...",
                            style = ZustTypography.bodyMedium,
                            color = colorResource(id = R.color.new_hint_color)
                        )
                    }
                    innerTextField()
                }
            })

        if (searchInput.isNotEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                contentDescription = "clear",
                modifier = modifier1
                    .constrainAs(clearIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        searchInput = ""
                        viewModel.productListUiState.update {
                            ProductListUi.InitialUi(false)
                        }
                    }
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.new_search_icon),
                contentDescription = "clear",
                modifier = modifier1.constrainAs(clearIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
    }

    LaunchedEffect(key1 = searchInput) {
        if (searchInput.isEmpty()) {
            viewModel.productListUiState.update {
                ProductListUi.InitialUi(false)
            }
            return@LaunchedEffect
        }
        if (searchInput.length <= SearchProductActivity.SEARCH_THRESHOLD) {
            viewModel.productListUiState.update {
                ProductListUi.InitialUi(false)
            }
            return@LaunchedEffect
        }
        delay(500)
        viewModel.searchProduct(searchInput.trim())
    }

    LaunchedEffect(focusRequester) {
        delay(100)
        awaitFrame()
        focusRequester.requestFocus()
        keyboard?.show()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun setSearchTextFiledColors(): TextFieldColors {
    val containerColor = Color(0xffffffff)
    return TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White,
        disabledLabelColor = Color.White,
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTextHint(
    value: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    var oldCount by remember {
        mutableStateOf(value)
    }
    SideEffect {
        oldCount = value
    }
    Row(modifier = modifier) {
        val oldCountString = oldCount
        for (i in value.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = value[i]
            val char = if (oldChar == newChar) {
                oldCountString[i]
            } else {
                value[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}

