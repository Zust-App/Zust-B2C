package `in`.opening.area.zustapp.refer

import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.refer.ui.ReferAndEarnMainUi
import `in`.opening.area.zustapp.utility.AppUtility
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ui.colorBlack
import ui.colorWhite

@AndroidEntryPoint
class ReferAndEarnActivity : AppCompatActivity() {
    companion object {
        const val REFER_DATA_KEY = "refer_data"
    }

    private val viewModel: ReferAndEarnViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            if (it.hasExtra(REFER_DATA_KEY)) {
                viewModel.referCache = it.getParcelableExtra(REFER_DATA_KEY)
            } else {
                AppUtility.showToast(this, "Something went wrong")
                finish()
            }
        }
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier.background(color = colorWhite),
                    titleText = "Refer and Earn", color = colorBlack, callback = {
                        finish()
                    })
            }) {
                viewModel.getUserReferralDetails()
                ReferAndEarnMainUi(viewModel, it)
            }
        }
    }
}