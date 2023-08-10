package non_veg.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.search.SearchProductActivity
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.uiModels.productList.ProductListUi
import `in`.opening.area.zustapp.viewmodels.SearchProductViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import non_veg.listing.uiModel.NonVegProductListingUiModel
import non_veg.search.viewmodel.NonVegSearchViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NonVegSearchUi(
    modifier: Modifier, modifier1: Modifier,
    viewModel: NonVegSearchViewModel,
) {
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
            textStyle = ZustTypography.body2,
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (searchInput.isEmpty()) {
                        Text(
                            text = "Search products...",
                            style = ZustTypography.body2,
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
                        viewModel._nonVegProductUiModel.update {
                            NonVegProductListingUiModel.Empty(false)
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
            viewModel._nonVegProductUiModel.update {
                NonVegProductListingUiModel.Empty(false)
            }
            return@LaunchedEffect
        }
        if (searchInput.length <= SearchProductActivity.SEARCH_THRESHOLD) {
            viewModel._nonVegProductUiModel.update {
                NonVegProductListingUiModel.Empty(false)
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
