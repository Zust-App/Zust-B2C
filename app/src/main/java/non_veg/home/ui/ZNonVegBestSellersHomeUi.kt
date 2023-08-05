package non_veg.home.ui

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import non_veg.home.ui.commonUi.ZNonVegTopPicsItemUi
import non_veg.home.viewmodel.ZustNvEntryViewModel
//9308968024
@Composable
fun ZNonVegBestSellersHomeUi(viewModel: ZustNvEntryViewModel) {
    LazyRow() {
        items(count = 4){
            ZNonVegTopPicsItemUi()
        }
    }
}