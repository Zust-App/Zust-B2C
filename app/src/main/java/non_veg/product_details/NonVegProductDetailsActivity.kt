package non_veg.product_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.R
import non_veg.product_details.viewmodel.NvProductDetailsViewModel

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