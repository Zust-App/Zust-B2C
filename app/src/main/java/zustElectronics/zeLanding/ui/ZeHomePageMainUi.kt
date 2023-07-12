package zustElectronics.zeLanding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.ui.theme.montserratFontFamily
import zustElectronics.zeLanding.uiState.ZeLandingProductsUiState
import zustElectronics.zeLanding.viewmodel.ZeEntryPointViewModel

@Composable
fun ZeHomePageMainUi(zeEntryPointViewModel: ZeEntryPointViewModel) {
    val zeLandingProductsUiState by zeEntryPointViewModel.zeProductsUiState.collectAsState()
    LazyColumn() {
        item {
            HomePageBrandingUi()
        }
        when (zeLandingProductsUiState) {
            is ZeLandingProductsUiState.SuccessState -> {
                (zeLandingProductsUiState as ZeLandingProductsUiState.SuccessState).data?.let { data ->
                    if (!data.data.isNullOrEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            Text(
                                text = "Trending items",
                                fontFamily = montserratFontFamily,
                                fontWeight = FontWeight.W600
                            )
                        }
                        items(data.data) { singleData ->
                            ZeHomeTrendingVerticalItems(singleData)
                        }
                    }
                }
            }

            is ZeLandingProductsUiState.ErrorState -> {

            }

            is ZeLandingProductsUiState.InitialState -> {

            }
        }
    }
}

@Composable
private fun HomePageBrandingUi() {
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .background(brush = Brush.linearGradient(
            colors = listOf(Color(0xff6750A4),
                Color(0xffC6B0FF), Color(0xffF8F4FF))))) {
        HomePageLinearGradient()
        ZeCustomHomeTopBar(modifier = Modifier) {

        }
        ZeHomeSearchBarUi()
    }
}

@Composable
private fun HomePageLinearGradient() {
    ConstraintLayout(modifier = Modifier
        .height(150.dp)
        .fillMaxWidth()) {
        Text(
            text = "Launching Zust Electronics",
            fontSize = 38.56.sp,
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight(700),
            color = Color(0xFF6750A4),
            modifier = Modifier.zIndex(2f))
    }
}

@Composable
@Preview
fun X() {
    val x: ZeEntryPointViewModel by viewModel()
    ZeHomePageMainUi(x)
}