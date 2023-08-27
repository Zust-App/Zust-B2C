package non_veg.product_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeTopAppBarProductList
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.utility.startNonVegSearchActivity
import non_veg.common.CustomNonVegBottomBarView
import non_veg.product_details.ui.NvProductDetailsMainLayout
import non_veg.product_details.viewmodel.NvProductDetailsViewModel
import ui.colorBlack
import ui.colorWhite
import ui.linearGradientNonVegBrush
import zustbase.utility.moveToCartDetailsActivity

@AndroidEntryPoint
class NonVegProductDetailsActivity : AppCompatActivity() {
    companion object {
        const val KEY_NV_PRODUCT_ID = "nv_key_product_id"
        const val KEY_NV_PRODUCT_PRICE_ID = "nv_key_product_price_id"
    }

    private val nonVegProductDetailsViewModel: NvProductDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(topBar = {
                ComposeTopAppBarProductList(Modifier.background(color = colorWhite),
                    "Product Details",
                    "", color = colorBlack, endImageId = R.drawable.new_search_icon) {
                    if (it == ACTION.NAV_BACK) {
                        finish()
                    } else if (it == ACTION.SEARCH_PRODUCT) {
                        startNonVegSearchActivity()
                    }
                }
            }, content = { paddingValues ->
                NvProductDetailsMainLayout(paddingValues, nonVegProductDetailsViewModel)
            }, bottomBar = {
                CustomNonVegBottomBarView(viewModel = nonVegProductDetailsViewModel, proceedToCartClick = {
                    nonVegProductDetailsViewModel.createNonVegCart()
                }, cartDataCallback = {
                    moveToCartDetailsActivity(it)
                })
            })
            LaunchedEffect(key1 = Unit, block = {
                nonVegProductDetailsViewModel.getNonVegProductDetail()
            })
        }
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(KEY_NV_PRODUCT_ID)) {
            nonVegProductDetailsViewModel.productId = intent.getIntExtra(KEY_NV_PRODUCT_ID, -1)
        } else {
            finish()
        }

        if (intent.hasExtra(KEY_NV_PRODUCT_PRICE_ID)) {
            nonVegProductDetailsViewModel.productPriceId = intent.getIntExtra(KEY_NV_PRODUCT_PRICE_ID, -1)
        } else {
            finish()
        }
    }

}