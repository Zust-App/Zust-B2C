package `in`.opening.area.zustapp.profile

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.profile.models.SuggestProductReqModel
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.json.JSONObject

@Composable
fun SuggestProductContainer(profileViewModel: ProfileViewModel, callback: () -> Unit) {
    val suggestProductReqModel by profileViewModel.suggestProductResponse.collectAsState(initial = JSONObject())

    var rememberLoadingState by remember { mutableStateOf(false) }
    var inputText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    if (suggestProductReqModel.has("id")) {
        rememberLoadingState = false
        AppUtility.showToast(context, stringResource(R.string.thanks_for_suggestion))
        profileViewModel.suggestProductResponse.tryEmit(JSONObject())
        LaunchedEffect(key1 = Unit, block = {
            callback.invoke()
        })
    }

    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
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
            text = stringResource(R.string.suggest_item),
            modifier = Modifier.constrainAs(title1) {
                top.linkTo(closeIcon.bottom, dp_8)
                start.linkTo(parent.start, dp_16)
                end.linkTo(parent.end, dp_16)
                width = Dimension.fillToConstraints
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.black_2))

        Text(text = stringResource(R.string.dinot_find_what_are_you_looking_for),
            modifier = Modifier.constrainAs(title2) {
                top.linkTo(title1.bottom, dp_12)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, textAlign = TextAlign.Center,
            style = ZustTypography.bodyMedium,
            fontWeight = FontWeight.W400,
            color = colorResource(id = R.color.grey_color_2))

        val containerColor = colorResource(id = R.color.screen_surface_color)
        TextField(
            placeholder = {
                Text(text = stringResource(R.string.type_items), style = ZustTypography.bodyMedium)
            },
            value = inputText,
            onValueChange = {
                inputText = it
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),

            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.screen_surface_color),
                    shape = RoundedCornerShape(8.dp))
                .defaultMinSize(minHeight = 80.dp).heightIn(max = 100.dp)
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
                    val bundle = Bundle()
                    bundle.putString("product_name", inputText)
                    FirebaseAnalytics.logEvents(FirebaseAnalytics.SUGGEST_PRODUCT, bundle)
                    profileViewModel.sendUserSuggestProduct(SuggestProductReqModel(inputText))
                } else {
                    AppUtility.showToast(context, context.getString(R.string.please_wirte_any_address))
                }
            }, modifier = Modifier.constrainAs(sendBtn) {
                top.linkTo(inputField.bottom, dp_16)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, dp_16)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = stringResource(R.string.send),
                color = Color.White,
                style = ZustTypography.bodyMedium,
                fontSize = 18.sp)
        }

        if (rememberLoadingState) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSurface,
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