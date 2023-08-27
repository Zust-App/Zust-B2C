package non_veg.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.compose.NoProductFoundErrorPage
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.search.searchResultModifier
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.cart.NonVegCartActivity
import non_veg.cart.models.NonVegCartData
import non_veg.common.CustomNonVegBottomBarView
import non_veg.listing.uiModel.NonVegProductListingUiModel
import non_veg.search.ui.NonVegSearchResultItemUi
import non_veg.search.ui.NonVegSearchUi
import non_veg.search.viewmodel.NonVegSearchViewModel
import ui.colorBlack
import ui.colorWhite
import ui.linearGradientNonVegBrush
import zustbase.utility.showSuggestProductSheet

@AndroidEntryPoint
class NonVegSearchActivity : AppCompatActivity() {

    private val nonVegSearchViewModel: NonVegSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = {
                    CustomNonVegBottomBarView(viewModel = nonVegSearchViewModel, {
                        nonVegSearchViewModel.createNonVegCart()
                    }) {
                        moveToCartDetailsActivity(it)
                    }
                },
                topBar = {
                    ComposeCustomTopAppBar(Modifier.background(color = colorWhite), color = colorBlack, titleText = "Search") {
                        if (it == ACTION.NAV_BACK) {
                            finish()
                        }
                    }
                },
                containerColor = colorResource(id = R.color.screen_surface_color),
                content = { paddingValue ->
                    SearchProductMainContainer(paddingValue, Modifier)
                },
            )
        }
    }

    private fun moveToCartDetailsActivity(nonVegCartData: NonVegCartData?) {
        if (nonVegCartData?.cartId == null) {
            return
        }
        val nonVegCartActivity = Intent(this, NonVegCartActivity::class.java)
        nonVegCartActivity.apply {
            putExtra(NonVegCartActivity.NON_VEG_CART_ID, nonVegCartData.cartId)
        }
        startActivity(nonVegCartActivity)
    }

    @Composable
    private fun SearchProductMainContainer(paddingValue: PaddingValues, modifier: Modifier) {
        val context = LocalContext.current
        var showProgressBar by remember {
            mutableStateOf(false)
        }
        val productSearchData by nonVegSearchViewModel.nonVegProductListUiModel.collectAsState(NonVegProductListingUiModel.Empty(false))

        ConstraintLayout(modifier = modifier
            .fillMaxWidth()
            .padding(paddingValue)
            .fillMaxHeight()) {
            val (progressBar) = createRefs()
            val (searchSection, searchResult) = createRefs()
            val (resultTitleText) = createRefs()
            showProgressBar = productSearchData.isLoading

            when (val data = productSearchData) {
                is NonVegProductListingUiModel.Success -> {
                    Text(text = buildString {
                        append("Showing ")
                        append((data.data?.size ?: 0))
                        append(" Result for `${nonVegSearchViewModel.searchTextCache}`")
                    }, modifier = modifier.constrainAs(resultTitleText) {
                        top.linkTo(searchSection.bottom, dp_4)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, style = ZustTypography.bodyMedium)
                    if (data.data.isNullOrEmpty()) {
                        NoProductFoundErrorPage(layoutScope = this, topReference = searchSection) {
                            showSuggestProductSheet()
                        }
                    } else {
                        if (data.data.isNotEmpty()) {
                            LazyColumn(modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp,
                                    vertical = 8.dp)
                                .constrainAs(searchResult) {
                                    top.linkTo(resultTitleText.bottom, dp_16)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.fillToConstraints
                                }) {
                                items(data.data) { singleItem ->
                                    NonVegSearchResultItemUi(singleItem, searchResultModifier) {
                                        nonVegSearchViewModel.handleNonVegCartInsertOrUpdate(singleItem, it)
                                    }
                                }
                            }
                        }
                    }
                }

                is NonVegProductListingUiModel.Error -> {
                    AppUtility.showToast(context, "Something went wrong")
                }

                is NonVegProductListingUiModel.Empty -> {

                }
            }

            if (showProgressBar) {
                CircularProgressIndicator(modifier = modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
            }

            NonVegSearchUi(modifier = modifier.constrainAs(searchSection) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, modifier1 = modifier,
                viewModel = nonVegSearchViewModel)
        }
    }


}

