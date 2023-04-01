package `in`.opening.area.zustapp.profile

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.models.SuggestProductReqModel
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import `in`.opening.area.zustapp.databinding.SuggestProductBtmSheetBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.ViewUtils
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class SuggestProductBtmSheet : BottomSheetDialogFragment() , OnApplyWindowInsetsListener {
    private var binding: SuggestProductBtmSheetBinding? = null

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = SuggestProductBtmSheetBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "suggest_product"
        fun newInstance(): SuggestProductBtmSheet {
            val args = Bundle()
            val fragment = SuggestProductBtmSheet()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        binding?.composeView?.setContent {
            SuggestProductContainer(profileViewModel) {
                dialog?.dismiss()
            }
        }
        return insets
    }

}

@Composable
fun SuggestProductContainer(profileViewModel: ProfileViewModel, callback: () -> Unit) {
    val suggestProductReqModel by profileViewModel.suggestProductResponse.collectAsState(initial = JSONObject())

    var rememberLoadingState by remember { mutableStateOf(false) }
    var inputText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    if (suggestProductReqModel.has("id")) {
        rememberLoadingState = false
        AppUtility.showToast(context, "Thanks for suggestion")
        profileViewModel.suggestProductResponse.tryEmit(JSONObject())
        LaunchedEffect(key1 = Unit, block = {
            callback.invoke()
        })
    }

    ConstraintLayout(modifier = Modifier
        .wrapContentHeight().sizeIn(maxHeight = 400.dp)
        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(12.dp))
        .padding(horizontal = 16.dp)) {
        val (closeIcon, title1, title2, inputField, sendBtn, pgBar) = createRefs()
        Icon(painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = "close", modifier = Modifier
                .clickable {
                    callback.invoke()
                }
                .constrainAs(closeIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, dp_16)
                })

        Text(
            text = "Suggest items",
            modifier = Modifier.constrainAs(title1) {
                top.linkTo(closeIcon.bottom, dp_8)
                start.linkTo(parent.start, dp_16)
                end.linkTo(parent.end, dp_16)
                width = Dimension.fillToConstraints
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            style = Typography_Montserrat.body1,
            color = colorResource(id = R.color.black_2))

        Text(text = "Didn’t find what you are looking for? Please \n" +
                "suggest the products",
            modifier = Modifier.constrainAs(title2) {
                top.linkTo(title1.bottom, dp_12)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, textAlign = TextAlign.Center,
            style = Typography_Montserrat.body2,
            fontWeight = FontWeight.W400,
            color = colorResource(id = R.color.grey_color_2))

        TextField(
            placeholder = {
                Text(text = "Type items...", style = Typography_Montserrat.body2)
            },
            value = inputText,
            onValueChange = {
                inputText = it
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = colorResource(id = R.color.screen_surface_color)),

            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.screen_surface_color),
                    shape = RoundedCornerShape(8.dp))
                .defaultMinSize(minHeight = 140.dp)
                .constrainAs(inputField) {
                    top.linkTo(title2.bottom, dp_16)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
        Button(
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.new_material_primary)),
            onClick = {
                if (inputText.isNotEmpty()) {
                    rememberLoadingState = true
                    profileViewModel.sendUserSuggestProduct(SuggestProductReqModel(inputText))
                } else {
                    AppUtility.showToast(context, "Please write Any products")
                }
            }, modifier = Modifier.constrainAs(sendBtn) {
                top.linkTo(inputField.bottom, dp_16)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, dp_16)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = "Send",
                color = Color.White,
                style = Typography_Montserrat.body1,
                fontSize = 18.sp)
        }

        if (rememberLoadingState) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .constrainAs(pgBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}