package `in`.opening.area.zustapp.rapidwallet

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.orderDetail.ui.PREFIX_ORDER_ID
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.rapidwallet.model.RwUserExistWalletData
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.RWUserWalletUiState
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ProductUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class RapidWalletActivity : AppCompatActivity() {
    companion object {
        const val RAPID_WALLET_SUCCESS = "rapid_wallet_success"
        const val ORDER_ID_KEY = "ORDER_ID"
    }

    private val rapidWalletViewModel: RapidWalletViewModel by viewModels()
    private var paymentWarningDialog: PaymentWarningDialog? = null

    private var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showWarningDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = "Rapid Wallet", callback = {
                    finish()
                })
            }) { padding ->
                RapidWalletMainUi(padding, rapidWalletViewModel)
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(PaymentActivity.PAYMENT_MODEL_KEY)) {
            rapidWalletViewModel.paymentActivityReqData = intent.getParcelableExtra(PaymentActivity.PAYMENT_MODEL_KEY)
        }
        if (intent.hasExtra(PaymentActivity.TOTAL_ITEMS_IN_CART)) {
            rapidWalletViewModel.itemCartCount = intent.getIntExtra(PaymentActivity.TOTAL_ITEMS_IN_CART, 0)
        }
    }

    private fun showWarningDialog() {
        if (paymentWarningDialog == null) {
            paymentWarningDialog = PaymentWarningDialog()
        }
        if (paymentWarningDialog?.isAdded == true && paymentWarningDialog?.isVisible == true) {
            return
        }
        paymentWarningDialog?.isCancelable = true
        paymentWarningDialog?.show(supportFragmentManager, PaymentWarningDialog.PAYMENT_WARNING_TAG)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RapidWalletMainUi(padding: PaddingValues, rapidWalletViewModel: RapidWalletViewModel) {
    val rwUserExistWalletData by rapidWalletViewModel.rapidUserExistUiState.collectAsState(RWUserWalletUiState.InitialUi(false))
    val context = LocalContext.current
    var rapidUserId by remember {
        mutableStateOf("")
    }
    var canShowInputFiled by remember {
        mutableStateOf(true)
    }

    var canShowPgBar by remember {
        mutableStateOf(false)
    }

    canShowPgBar = rwUserExistWalletData.isLoading
    val keyboard = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(padding)
        .background(color = colorResource(id = R.color.white)),
        horizontalAlignment = Alignment.CenterHorizontally) {
        OrderInformation(rapidWalletViewModel)
        AnimatedVisibility(visible = canShowInputFiled) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .wrapContentHeight(), elevation = 2.dp)
            {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Enter Rapid Wallet User ID*", style = ZustTypography.body2,
                        color = colorResource(id = R.color.black_2), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.input_field_bg_color),
                                shape = RoundedCornerShape(8.dp))
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboard?.hide()
                        }),
                        value = rapidUserId,
                        onValueChange = { newText ->
                            rapidUserId = newText
                        },
                        textStyle = ZustTypography.body1,
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                innerTextField()
                            }
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(modifier = Modifier.background(color = colorResource(id = R.color.new_material_primary),
                        shape = RoundedCornerShape(8.dp)), onClick = {
                        if (rapidUserId.isEmpty()) {
                            AppUtility.showToast(context, "Please enter correct User ID")
                            return@Button
                        }
                        if (!rapidWalletViewModel.rapidUserExistUiState.value.isLoading) {
                            keyboard?.hide()
                            rapidWalletViewModel.verifyRapidWalletAndBalance(rapidUserId)
                        } else {
                            AppUtility.showToast(context, "Please wait")
                        }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor =
                    colorResource(id = R.color.new_material_primary))) {
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(text = "Next", color = colorResource(id = R.color.white),
                            style = ZustTypography.body1)
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                }
            }
        }
        when (rwUserExistWalletData) {
            is RWUserWalletUiState.Success -> {
                canShowInputFiled = false
                val successData = (rwUserExistWalletData as RWUserWalletUiState.Success).data
                ShowWalletBalances(successData) {
                    rapidWalletViewModel.rapidUserExistUiState.update {
                        RWUserWalletUiState.InitialUi(false)
                    }
                }
            }

            is RWUserWalletUiState.ErrorUi -> {
                canShowInputFiled = true
                AppUtility.showToast(context, (rwUserExistWalletData as RWUserWalletUiState.ErrorUi).errors?.getTextMsg())
            }

            is RWUserWalletUiState.InitialUi -> {
                canShowInputFiled = true
            }
        }
        if (canShowPgBar) {
            CustomAnimatedProgressBar(modifier = Modifier)
        }
        RapidWalletTerms()
    }
}

const val RAPID_BANK = 0
const val RAPID_WALLET = 1

@Composable
private fun ShowWalletBalances(successData: RwUserExistWalletData?, editUserIdCallback: () -> Unit) {
    var currentRapidMethod by remember {
        mutableStateOf(RAPID_BANK)
    }

    if (successData?.rapidWallet?.userId.isNullOrEmpty()) {
        return
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)
        .wrapContentHeight(), elevation = 2.dp, shape = RoundedCornerShape(8.dp)) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .wrapContentHeight(),
        ) {
            val (title1, subtitle1, balance1, radioBtn1) = createRefs()
            val (title2, subtitle2, balance2, radioBtn2) = createRefs()
            val (divider) = createRefs()
            val (nextBtn) = createRefs()

            val (rapidUserId, editUserId) = createRefs()
            Text(text = successData?.rapidWallet?.userId!!,
                modifier = Modifier.constrainAs(rapidUserId) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }, style = ZustTypography.h1, color = colorResource(id = R.color.new_material_primary))

            Text(text = "Edit", modifier = Modifier
                .constrainAs(editUserId) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clickable {
                    editUserIdCallback.invoke()
                }, style = ZustTypography.body2,
                color = colorResource(id = R.color.green))

            successData.rapidBank?.bankBalance?.let {
                Text(text = "Rapid Bank", modifier = Modifier.constrainAs(title1) {
                    top.linkTo(rapidUserId.bottom, dp_12)
                    start.linkTo(parent.start)
                    end.linkTo(radioBtn1.start, dp_8)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body1, color = colorResource(id = R.color.app_black))

                Text(text = "Available Balance:- ", modifier = Modifier
                    .constrainAs(subtitle1) {
                        top.linkTo(title1.bottom, dp_8)
                        start.linkTo(parent.start)
                    }
                    .clickable {
                        currentRapidMethod = RAPID_BANK
                    }, style = ZustTypography.body2, color = colorResource(id = R.color.black_2))
                Text(text = it.toString(), modifier = Modifier.constrainAs(balance1) {
                    top.linkTo(subtitle1.top)
                    start.linkTo(subtitle1.end, dp_12)
                    bottom.linkTo(subtitle1.bottom)
                    end.linkTo(radioBtn1.start, dp_8)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body1, color = colorResource(id = R.color.new_material_primary))

                RadioButton(
                    selected = currentRapidMethod == RAPID_BANK,
                    onClick = {
                        currentRapidMethod = RAPID_BANK
                    },
                    modifier = Modifier.constrainAs(radioBtn1) {
                        top.linkTo(title1.top)
                        bottom.linkTo(balance1.bottom)
                        end.linkTo(parent.end)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.new_material_primary),
                        unselectedColor = colorResource(id = R.color.new_hint_color)
                    ),
                )
            }

            Divider(modifier = Modifier
                .height(1.dp)
                .constrainAs(divider) {
                    top.linkTo(balance1.bottom, dp_16)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            successData.rapidWallet.walletBalance?.let {
                Text(text = "Rapid Wallet", modifier = Modifier.constrainAs(title2) {
                    top.linkTo(divider.bottom, dp_16)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body1, color = colorResource(id = R.color.app_black))

                Text(text = "Available Balance:- ", modifier = Modifier
                    .constrainAs(subtitle2) {
                        top.linkTo(title2.bottom, dp_8)
                        start.linkTo(parent.start)
                    }
                    .clickable {
                        currentRapidMethod = RAPID_WALLET
                    }, style = ZustTypography.body2, color = colorResource(id = R.color.black_2))
                Text(text = it.toString(), modifier = Modifier.constrainAs(balance2) {
                    top.linkTo(subtitle2.top)
                    start.linkTo(subtitle2.end, dp_12)
                    bottom.linkTo(subtitle2.bottom)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body1, color = colorResource(id = R.color.new_material_primary))

                RadioButton(
                    selected = currentRapidMethod == RAPID_WALLET,
                    onClick = {
                        currentRapidMethod = RAPID_WALLET
                    },
                    modifier = Modifier.constrainAs(radioBtn2) {
                        top.linkTo(title2.top)
                        bottom.linkTo(balance2.bottom)
                        end.linkTo(parent.end)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.new_material_primary),
                        unselectedColor = colorResource(id = R.color.new_hint_color)
                    ),
                )
            }

            Button(modifier = Modifier
                .constrainAs(nextBtn) {
                    top.linkTo(balance2.bottom, dp_24)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(color = colorResource(id = R.color.new_material_primary),
                    shape = RoundedCornerShape(8.dp)), onClick = {

            }, colors = ButtonDefaults.buttonColors(backgroundColor =
            colorResource(id = R.color.new_material_primary))) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = "Next", color = colorResource(id = R.color.white),
                    style = ZustTypography.body1)
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
private fun OrderInformation(rapidWalletViewModel: RapidWalletViewModel) {
    if (rapidWalletViewModel.paymentActivityReqData == null) {
        return
    }
    if (rapidWalletViewModel.paymentActivityReqData?.orderId == null) {
        return
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)
        .wrapContentHeight(),
        elevation = 2.dp, shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .wrapContentHeight()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "OrderId:- ", style = ZustTypography.body1, modifier = Modifier, color = colorResource(id = R.color.new_material_primary))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = PREFIX_ORDER_ID + rapidWalletViewModel.paymentActivityReqData?.orderId.toString(),
                    style = ZustTypography.body1,
                    modifier = Modifier)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Payable Amount:- ",
                    style = ZustTypography.body1, modifier = Modifier, color = colorResource(id = R.color.new_material_primary))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(rapidWalletViewModel.getPayablePrice()), style = ZustTypography.body1,
                    modifier = Modifier)
            }

        }
    }
}

@Composable
private fun RapidWalletTerms() {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        val (rapidIcon) = createRefs()
        Image(painter = painterResource(id = R.drawable.rapid_bazar_icon), contentDescription = "rapid_bazaar",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .constrainAs(rapidIcon) {
                    top.linkTo(parent.top, dp_16)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
    }
}

