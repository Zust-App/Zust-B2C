package `in`.opening.area.zustapp.address.compose

import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.ui.generic.CustomProgressBar
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.uiModels.SearchPlacesUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun SearchAddressMainUi(viewModel: AddressViewModel, modifier: Modifier,callback:(SearchPlacesDataModel)->Unit) {
    val response by viewModel.searchPlacesUiState.collectAsState(initial = SearchPlacesUi.InitialUi("",
        false))

    var searchInputPlace by remember {
        mutableStateOf("")
    }
    var canShowProgressBar by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    when (response) {
        is SearchPlacesUi.SearchPlaceResult -> {
            canShowProgressBar = response.isLoading
        }
        is SearchPlacesUi.ErrorUi -> {
            canShowProgressBar = response.isLoading
            AppUtility.showToast(context, response.errorMessage)
        }
        is SearchPlacesUi.InitialUi -> {
            canShowProgressBar = response.isLoading
        }
    }

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (searchBar, searchResult, progressBar) = createRefs()

        SearchPlacesBarUi(modifier = Modifier.constrainAs(searchBar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, Modifier, viewModel)

        LazyColumn(modifier = Modifier.constrainAs(searchResult) {
            top.linkTo(searchBar.bottom, dp_8)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }) {
            if (response is SearchPlacesUi.SearchPlaceResult) {
                if ((response as SearchPlacesUi.SearchPlaceResult).data.isNotEmpty()) {
                    item {
                        Text(text = "Search Result", modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp), style = ZustTypography.body1)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    items((response as SearchPlacesUi.SearchPlaceResult).data) { data ->
                        SearchPlacesResultItemUi(data) {
                            callback.invoke(it)
                        }
                    }
                }
            }
        }
        if (canShowProgressBar) {
            CustomAnimatedProgressBar(modifier = Modifier.constrainAs(progressBar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            })
        }
    }
}
