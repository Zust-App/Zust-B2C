package zustbase.analysis.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.home.components.FullScreenErrorUi
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import zustbase.ZustLandingViewModel
import zustbase.analysis.uimodels.UserReportAnalysisUiModel

@Composable
fun ZustUserAnalysisMainUi(zustLandingViewModel: ZustLandingViewModel) {
    val userAnalysisReportUiModel by zustLandingViewModel.userAnalysisReportUiModel.collectAsState()
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (heading, dataContainer, pgBar) = createRefs()
        Text(text = "Order Report", color = colorResource(id = R.color.app_black), style = ZustTypography.titleLarge, modifier = Modifier
            .constrainAs(heading) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
            .padding(horizontal = dp_16, vertical = dp_16))
        if (userAnalysisReportUiModel.isLoading) {
            CustomAnimatedProgressBar(modifier = Modifier.constrainAs(pgBar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }
        when (val response = userAnalysisReportUiModel) {
            is UserReportAnalysisUiModel.Success -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(dataContainer) {
                        top.linkTo(heading.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }) {
                    item {
                        CurrentUserAnalysisReport(response.data.userReport)
                    }
                    if (!response.data.topGainer.isNullOrEmpty()) {
                        item {
                            Text(text = "Top Gainers",
                                color = colorResource(id = R.color.app_black),
                                style = ZustTypography.titleLarge, modifier = Modifier
                                    .constrainAs(heading) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                    }
                                    .padding(horizontal = dp_16, vertical = dp_8))
                        }
                        response.data.topGainer.forEachIndexed { index, topGainer ->
                            item(index) {
                                if (topGainer.name != null && topGainer.expense != null) {
                                    TopGainerSingleItemUi(name = topGainer.name, expense = topGainer.expense, rank = index + 1)
                                }
                            }
                        }
                    }
                }
            }

            is UserReportAnalysisUiModel.Empty -> {

            }

            is UserReportAnalysisUiModel.Error -> {
                FullScreenErrorUi(errorCode = response.errorCode, retryCallback = {
                    zustLandingViewModel.getUserAnalysisData()
                }) {

                }
            }
        }
    }
}