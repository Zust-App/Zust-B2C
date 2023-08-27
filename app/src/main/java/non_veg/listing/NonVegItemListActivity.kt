package non_veg.listing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.compose.ComposeTopAppBarProductList
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.utility.startNonVegSearchActivity
import non_veg.cart.NonVegCartActivity
import non_veg.cart.models.NonVegCartData
import non_veg.common.CustomNonVegBottomBarView
import non_veg.listing.ui.NonVegListingMainContainer
import non_veg.listing.viewmodel.NonVegListingViewModel
import ui.colorBlack
import ui.colorWhite
import ui.linearGradientNonVegBrush
import zustbase.utility.moveToCartDetailsActivity
import zustbase.utility.showSuggestProductSheet

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
                NonVegListingMainContainer(paddingValues, viewModel) {
                    showSuggestProductSheet()
                }
            }, bottomBar = {
                CustomNonVegBottomBarView(viewModel = viewModel, {
                    viewModel.createNonVegCart()
                }) {
                    moveToCartDetailsActivity(it)
                }
            }, topBar = {
                ComposeTopAppBarProductList(Modifier.background(color = colorWhite), buildString {
                    append(title ?: "")
                    append(" Category")
                },
                    "", color = colorBlack, R.drawable.new_search_icon) {
                    if (it == ACTION.NAV_BACK) {
                        finish()
                    } else if (it == ACTION.SEARCH_PRODUCT) {
                        startNonVegSearchActivity()
                    }
                }
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


}