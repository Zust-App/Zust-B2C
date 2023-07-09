package zustElectronics.zeLanding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.montserratFontFamily

@Composable
fun ZeHomePageMainUi() {
    LazyColumn() {
        item {
            HomePageBrandingUi()
        }
        item {
            Text(
                text = "Trending items",
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.W600
            )
        }
        item {
            ZeHomeTrendingVerticalItems()
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
    ZeHomePageMainUi()
}