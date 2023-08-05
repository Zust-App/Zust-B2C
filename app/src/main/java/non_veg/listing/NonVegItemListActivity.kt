package non_veg.listing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import non_veg.cart.NonVegCartActivity
import non_veg.cart.models.NonVegCartData
import non_veg.common.CustomNonVegBottomBarView
import non_veg.listing.ui.NonVegListingMainContainer
import non_veg.listing.viewmodel.NonVegListingViewModel

@AndroidEntryPoint
class NonVegItemListActivity : AppCompatActivity() {
    private val viewModel: NonVegListingViewModel by viewModels()

    private var title: String? = null

    companion object {
        const val KEY_NV_CATEGORY_ID = "nv_category_id"
        const val KEY_NV_CATEGORY_TITLE = "nv_category_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(content = { paddingValues ->
                NonVegListingMainContainer(paddingValues, viewModel)
            }, bottomBar = {
                CustomNonVegBottomBarView(viewModel = viewModel, {
                    viewModel.createNonVegCart()
                }) {
                    moveToCartDetailsActivity(it)
                }
            }, topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = title ?: "Category", callback = {
                    finish()
                })
            })
        }
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(KEY_NV_CATEGORY_ID)) {
            val categoryId = intent.getIntExtra(KEY_NV_CATEGORY_ID, -1)
            if (categoryId == -1) {
                finish()
            } else {
                viewModel.getAndMappedLocalCartWithServerItems(categoryId)
            }
        } else {
            finish()
        }
        if (intent.hasExtra(KEY_NV_CATEGORY_TITLE)) {
            title = intent.getStringExtra(KEY_NV_CATEGORY_TITLE)
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
}