package `in`.opening.area.zustapp.webpage

import `in`.opening.area.zustapp.databinding.ActivityInAppWebBinding
import `in`.opening.area.zustapp.utility.AppUtility
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import zustbase.orderDetail.ui.ORDER_ID
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class InAppWebActivity : AppCompatActivity() {

    companion object {
        const val WEB_URL = "web_url"
        const val TITLE_TEXT = "title_text"
    }

    private var binding: ActivityInAppWebBinding? = null

    private var titleString: String? = null
    private var webUrl: String? = null
    private val viewModel: InAppWebViewModel by viewModels()
    private var orderId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInAppWebBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        receiveDataFromIntent()
        setUpTitleText()
        setUpData()
        setUpObservers()
    }

    private fun receiveDataFromIntent() {
        if (intent.hasExtra(TITLE_TEXT)) {
            titleString = intent.getStringExtra(TITLE_TEXT)
        }
        if (intent.hasExtra(WEB_URL)) {
            webUrl = intent.getStringExtra(WEB_URL)
        }
        if (intent.hasExtra(ORDER_ID)) {
            orderId = intent.getIntExtra(ORDER_ID, -1);
        }
    }

    private fun setUpTitleText() {
        if (webUrl.isNullOrEmpty() && (orderId == null || orderId == -1)) {
            finish()
            return
        }
        if (titleString != null) {
            binding?.titleText?.text = titleString
        }
        binding?.navBackIcon?.setOnClickListener {
            finish()
        }
    }

    private fun setUpData() {
        if (webUrl == null) {
            finish()
        }
        setUpWebView()
    }

    private fun setUpWebView() {
        if (webUrl != null) {
            if (orderId == null) {
                binding?.customWebView?.webViewExtension(webUrl!!, this, {
                    binding?.composeView?.setContent {
                        SetUpComposeView(it)
                    }
                }, {
                    showHideLoader(it)
                })
            }
            if (orderId != null && orderId != -1) {
                viewModel.getInvoiceBasedOnOrder(orderId!!)
            }
        } else {
            finish()
        }
    }


    @Composable
    private fun SetUpComposeView(indicatorProgress: Int) {
        if (indicatorProgress < 100) {
            var progress by remember { mutableStateOf(0f) }
            val progressAnimDuration = 1500
            val progressAnimation by animateFloatAsState(
                targetValue = indicatorProgress.toFloat(),
                animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)), // Rounded edges
                progress = progressAnimation
            )
            LaunchedEffect(indicatorProgress) {
                progress = indicatorProgress.toFloat()
            }
        }
    }


    private fun showHideLoader(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TITLE_TEXT, titleString)
        outState.putString(WEB_URL, webUrl)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(TITLE_TEXT)) {
            titleString = savedInstanceState.getString(TITLE_TEXT)
        }
        if (savedInstanceState.containsKey(WEB_URL)) {
            webUrl = savedInstanceState.getString(WEB_URL)
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.orderUiResponse.collectLatest {
                parseInvoiceResponse(it)
            }
        }
    }

    private fun parseInvoiceResponse(invoiceOrderUiState: InvoiceOrderUiState) {
        showHideLoader(invoiceOrderUiState.isLoading)
        when (invoiceOrderUiState) {
            is InvoiceOrderUiState.Success -> {
                if (invoiceOrderUiState.invoiceHtml != null) {
                    binding?.customWebView?.loadData(invoiceOrderUiState.invoiceHtml, "text/html", "UTF-8");
                } else {
                    finish()
                }
            }
            is InvoiceOrderUiState.Initial -> {
                //leave as empty
            }
            is InvoiceOrderUiState.ErrorUi -> {
                AppUtility.showToast(this, invoiceOrderUiState.errorMsg)
                finish()
            }
        }
    }


}