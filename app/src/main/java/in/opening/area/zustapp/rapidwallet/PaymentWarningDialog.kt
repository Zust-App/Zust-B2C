package `in`.opening.area.zustapp.rapidwallet

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.generic.CustomProgressBar
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.DialogFragment


class PaymentWarningDialog : DialogFragment() {
    companion object {
        const val PAYMENT_WARNING_TAG = "payment_warning_tag"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_SUBTITLE = "arg_subtitle"
        fun newInstance(title: String? = null, subtitle: String? = null): PaymentWarningDialog {
            val args = Bundle().apply {
                putString(ARG_TITLE, title ?: "Please Wait")
                putString(ARG_SUBTITLE, subtitle ?: "We are fetching your balance please wait...")
            }
            return PaymentWarningDialog().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    val (title, progressBar, subTitle) = createRefs()

                    Text(
                        text = arguments?.getString(ARG_TITLE) ?: "Please Wait",
                        modifier = Modifier.constrainAs(title) {
                            top.linkTo(parent.top, dp_12)
                            start.linkTo(progressBar.end, dp_16)
                            end.linkTo(parent.end, dp_16)
                            width = Dimension.fillToConstraints
                        },
                        style = ZustTypography.bodyMedium,
                        color = colorResource(id = R.color.app_black)
                    )

                    Text(
                        text = arguments?.getString(ARG_SUBTITLE)
                            ?: "We are fetching your balance please wait...",
                        modifier = Modifier.constrainAs(subTitle) {
                            top.linkTo(title.bottom, dp_16)
                            start.linkTo(progressBar.end, dp_16)
                            end.linkTo(parent.end, dp_16)
                            bottom.linkTo(parent.bottom, dp_16)
                            width = Dimension.fillToConstraints
                        }, style = ZustTypography.bodyMedium,
                        color = colorResource(id = R.color.black_3)
                    )

                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .constrainAs(progressBar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start, dp_16)
                                bottom.linkTo(parent.bottom)
                            }
                            .width(30.dp)
                            .height(30.dp)
                    )
                }
            }
        }
    }

}