package non_veg.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.generic.CustomTitleText
import `in`.opening.area.zustapp.ui.generic.customHomePageSearch
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import non_veg.home.uiModel.NonVegCategoryUiModel
import non_veg.home.viewmodel.ZustNvEntryViewModel

@Composable
fun ZustNvEntryMainUi(viewModel: ZustNvEntryViewModel= androidx.lifecycle.viewmodel.compose.viewModel(), paddingValues: PaddingValues) {
    val nonVegCategoryUiModel = viewModel.nonVegCategoryUiModel.collectAsState().value
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(paddingValues = paddingValues)
        .background(color = colorResource(id = R.color.white))) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {

            customHomePageSearch(arrayListOf("Search `Eggs`", "Search `Meat`", "Search `Chicken`")) {

            }
            item {
                Text(text = "Categories",
                    style = ZustTypography.body1,
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp))
            }
            item {
                ZNonHomePageBannerUi(viewModel)
            }
            item {
                ZNonVegHomeOfferUi(viewModel)
            }
            when (nonVegCategoryUiModel) {
                is NonVegCategoryUiModel.Success -> {
                    zNonVegHomeCategoryUi(nonVegCategoryUiModel.data)
                }

                is NonVegCategoryUiModel.Initial -> {

                }

                is NonVegCategoryUiModel.Error -> {

                }
            }
            item {
                //ZNonVegBestSellersHomeUi(viewModel)
            }
            item {
                // ZNonVegHomeTopPicksUi(viewModel)
            }

            item {
                ZNonVegHomeTagUi()
            }

        }
    }
}