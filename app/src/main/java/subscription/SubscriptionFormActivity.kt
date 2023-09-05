package subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.compose.getTextFieldColors
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import subscription.uiModel.SubscriptionFormUiModel
import ui.colorBlack
import ui.colorWhite

@AndroidEntryPoint
class SubscriptionFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), topBar = {
                ComposeCustomTopAppBar(modifier = Modifier.background(color = colorWhite), color = colorBlack, titleText = "Subscription Form ( सब्सक्रिप्शन फ़ॉर्म )") {
                    finish()
                }
            }) { paddingValues ->
                SubscriptionFormMainUi(paddingValues)
            }
        }
    }
}

@Composable
private fun SubscriptionFormMainUi(paddingValues: PaddingValues, viewModel: SubscriptionViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var inputDailyItems by remember {
        mutableStateOf("")
    }
    var thirdDayNeedItems by remember {
        mutableStateOf("")
    }
    var weeklyNeedItems by remember {
        mutableStateOf("")
    }
    var anythingElse by remember {
        mutableStateOf("")
    }
    var pincode by remember {
        mutableStateOf("")
    }
    var loading by remember {
        mutableStateOf(false)
    }
    val showDialog = remember { mutableStateOf(false) } // State to control the dialog visibility

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val subscriptionFormResModel by viewModel.subscriptionFormUiModel.collectAsState()
    loading = subscriptionFormResModel.isLoading
    when (val response = subscriptionFormResModel) {
        is SubscriptionFormUiModel.Success -> {
            if (response.data) {
                AppUtility.showToast(LocalContext.current, "Thanks for your feedback")
                inputDailyItems = ""
                thirdDayNeedItems = ""
                showDialog.value = true
            }
            viewModel.resetFeedbackFormUiModel()
        }

        is SubscriptionFormUiModel.Error -> {
            AppUtility.showToast(LocalContext.current, response.error)
            viewModel.resetFeedbackFormUiModel()
        }

        is SubscriptionFormUiModel.Empty -> {

        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text = "Thanks (धन्यवाद)", style = ZustTypography.bodyMedium)
            },
            text = {
                Text(text = "Your feedback has been submitted. You will receive a Confirmation Call on your mobile registered number (आपकी फ़ीडबैक सबमिट की गई है। आपको आपके पंजीकृत मोबाइल नंबर पर पुष्टि कॉल प्राप्त होगी)", style = ZustTypography.bodyMedium)
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                }) {
                    Text(text = "OK", style = ZustTypography.bodyMedium)
                }
            }
        )
    }
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(paddingValues))
    {
        val (itemContainer, button, progressBar) = createRefs()
        LazyColumn(state = listState, modifier = Modifier
            .fillMaxWidth()
            .constrainAs(itemContainer) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(button.top, dp_16)
                height = Dimension.fillToConstraints
            }
            .padding(horizontal = dp_16, vertical = dp_16))
        {
            item {
                Text(text = "Please fill this form so that we give contactless and hassle free delivery  (कृपया इस फ़ॉर्म को भरें ताकि हम आपको संपर्क रहित और परेशानी रहित वितरण प्रदान कर सकें।)", style = ZustTypography.bodyMedium)
            }
            item {
                Spacer(modifier = Modifier.height(dp_20))
            }
            item {
                Text(text = "Daily need Items (रोज़ाना की आवश्यक वस्तुएँ)", style = ZustTypography.bodySmall)
            }
            item {
                Spacer(modifier = Modifier.height(dp_8))
            }
            item {
                TextField(value = inputDailyItems,
                    textStyle = ZustTypography.bodyMedium,
                    onValueChange = {
                        inputDailyItems = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 80.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    colors = getTextFieldColors(), placeholder = {
                        Text("Like:- Amul Gold 2 Litre (जैसे:- अमूल गोल्ड 2 लीटर)", style = ZustTypography.bodyMedium, color = colorResource(id = R.color.hint_color))
                    })
            }
            item {
                Spacer(modifier = Modifier.height(dp_20))
            }
            item {
                Text(text = "3rd day need items (तीसरे दिन की आवश्यक वस्तुएँ)", style = ZustTypography.bodySmall)
            }
            item {
                Spacer(modifier = Modifier.height(dp_8))
            }
            item {
                TextField(value = thirdDayNeedItems,
                    textStyle = ZustTypography.bodyMedium,
                    onValueChange = {
                        thirdDayNeedItems = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 80.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    colors = getTextFieldColors(), placeholder = {
                        Text("Like:- Madhur Sugar 500g (जैसे:- मधुर शक्कर 500 ग्राम)", style = ZustTypography.bodyMedium, color = colorResource(id = R.color.hint_color))
                    })
            }

            item {
                Spacer(modifier = Modifier.height(dp_20))
            }
            item {
                Text(text = "Weekly need items (साप्ताहिक आवश्यक वस्तुएँ)", style = ZustTypography.bodySmall)
            }
            item {
                Spacer(modifier = Modifier.height(dp_8))
            }
            item {
                TextField(value = weeklyNeedItems,
                    textStyle = ZustTypography.bodyMedium,
                    onValueChange = {
                        weeklyNeedItems = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 80.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    colors = getTextFieldColors(), placeholder = {
                        Text("Write here.. (यहाँ लिखें..)", style = ZustTypography.bodyMedium, color = colorResource(id = R.color.hint_color))
                    })
            }

            item {
                Spacer(modifier = Modifier.height(dp_20))
            }
            item {
                Text(text = "Anything else? (और कुछ?)", style = ZustTypography.bodySmall)
            }
            item {
                Spacer(modifier = Modifier.height(dp_8))
            }
            item {
                TextField(value = anythingElse,
                    textStyle = ZustTypography.bodyMedium,
                    onValueChange = {
                        anythingElse = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 80.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    colors = getTextFieldColors(), placeholder = {
                        Text("Write here.. (यहाँ लिखें..)", style = ZustTypography.bodyMedium, color = colorResource(id = R.color.hint_color))

                    })
            }

            item {
                Spacer(modifier = Modifier.height(dp_20))
            }
            item {
                Text(text = "Pincode (पिनकोड)", style = ZustTypography.bodySmall)
            }
            item {
                Spacer(modifier = Modifier.height(dp_8))
            }
            item(key = pincodeItemIdentifier) {
                TextField(value = pincode,
                    textStyle = ZustTypography.bodyMedium,
                    onValueChange = {
                        pincode = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 30.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    colors = getTextFieldColors(), placeholder = {
                        Text("Enter pincode", style = ZustTypography.bodyMedium, color = colorResource(id = R.color.hint_color))

                    })
            }

        }
        Button(onClick = {
            if (pincode.isNotEmpty()) {
                if (!subscriptionFormResModel.isLoading) {
                    viewModel.submitSubscriptionRequest(inputDailyItems, thirdDayNeedItems, weeklyNeedItems, anythingElse, pincode)
                }
            } else {
                AppUtility.showToast(context = context, "Please enter pincode")
                coroutineScope.launch {
                    listState.animateScrollToItem(20)
                }
            }
        }, modifier = Modifier.constrainAs(button) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Spacer(modifier = Modifier.width(dp_16))
            Text(text = "Send Items", style = ZustTypography.bodyMedium)
            Spacer(modifier = Modifier.width(dp_16))
        }
        if (loading) {
            CustomAnimatedProgressBar(modifier = Modifier.constrainAs(progressBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}

const val pincodeItemIdentifier = "PINCODE_INPUT"
private fun LazyListState.findPincodeFieldIndex(identifier: String): Int {
    val index = layoutInfo.visibleItemsInfo.indexOfFirst { it.key == identifier }
    return if (index != -1) index else 0
}