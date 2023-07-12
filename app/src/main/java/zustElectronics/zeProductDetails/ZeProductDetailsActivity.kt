package zustElectronics.zeProductDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import zustElectronics.zeProductDetails.ui.ZeProductDetailMainUi
import zustElectronics.zeProductDetails.viewModel.ZeProductDetailViewModel

@AndroidEntryPoint
class ZeProductDetailsActivity : AppCompatActivity() {
    private val zeProductDetailViewModel: ZeProductDetailViewModel by viewModels()

    companion object {
        const val ZE_PRODUCT_ID = "ze_product_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeProductDetailMainUi(zeProductDetailViewModel)
            val productId = intent.getIntExtra(ZE_PRODUCT_ID, -1)
            if (productId != -1) {
                zeProductDetailViewModel.getZeProductDetails(productId = productId)
            }
        }
    }
}