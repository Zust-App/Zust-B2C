package `in`.opening.area.zustapp.rapidwallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import zustbase.orderDetail.ui.INTENT_SOURCE
import zustbase.orderDetail.ui.INTENT_SOURCE_NON_VEG
import zustbase.orderDetail.ui.PREFIX_ORDER_ID_GROCERY
import zustbase.orderDetail.ui.PREFIX_ORDER_ID_NON_VEG
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletResult
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletUiRepresentationModel
import `in`.opening.area.zustapp.rapidwallet.model.RwUserExistWalletData
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.RWUserWalletUiState
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ProductUtils
import kotlinx.coroutines.flow.update
import ui.colorBlack
import ui.colorWhite
import ui.colorWhite1
import java.util.*

@AndroidEntryPoint
class RapidWalletActivity : AppCompatActivity() {
    companion object {
        const val RAPID_WALLET_SUCCESS = "rapid_wallet_success"
    }

    private val rapidWalletViewModel: RapidWalletViewModel by viewModels()
    private var paymentWarningDialog: PaymentWarningDialog? = null
    private var enableBackBtn = false

    private var onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), topBar = {
                ComposeCustomTopAppBar(
                    modifier = Modifier,
                    titleText = "Rapid Wallet",
                    callback = {
                        handleBackPressed()
                    }, color = colorBlack)
            }, containerColor = colorResource(id = R.color.screen_surface_color)) { padding ->
                RapidWalletMainUi(padding, rapidWalletViewModel) { rapidWalletResult ->
                    val intent = Intent()
                    intent.putExtra(RAPID_WALLET_SUCCESS, rapidWalletResult)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun handleBackPressed() {
        if (enableBackBtn) {
            rapidWalletViewModel.getOrderId()
            val intent = Intent()
            intent.putExtra(
                RAPID_WALLET_SUCCESS,
                RapidWalletResult(rapidWalletViewModel.getOrderId()!!, -1)
            )
            AppUtility.showToast(this@RapidWalletActivity, "Payment cancelled")
            setResult(RESULT_CANCELED, intent)
            finish()
        } else {
            AppUtility.showToast(this@RapidWalletActivity, "Please wait")
        }
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(PaymentActivity.PAYMENT_MODEL_KEY)) {
            rapidWalletViewModel.paymentActivityReqData =
                intent.getParcelableExtra(PaymentActivity.PAYMENT_MODEL_KEY)
        }
        if (intent.hasExtra(PaymentActivity.TOTAL_ITEMS_IN_CART)) {
            rapidWalletViewModel.itemCartCount =
                intent.getIntExtra(PaymentActivity.TOTAL_ITEMS_IN_CART, 0)
        }
        if (intent.hasExtra(INTENT_SOURCE)) {
            rapidWalletViewModel.intentSource = intent.getStringExtra(INTENT_SOURCE)
        }
        rapidWalletViewModel.rapidUserExistUiState.update {
            RWUserWalletUiState.ShowUserIdInput(false)
        }
    }

    private fun showWarningDialog(title: String? = null, subTitle: String? = null) {
        if (paymentWarningDialog == null) {
            paymentWarningDialog = PaymentWarningDialog.newInstance(title, subTitle)
        }
        if (paymentWarningDialog?.isAdded == true) {
            return
        }
        if (paymentWarningDialog?.isVisible == true) {
            return
        }
        paymentWarningDialog?.isCancelable = false
        paymentWarningDialog?.show(supportFragmentManager, PaymentWarningDialog.PAYMENT_WARNING_TAG)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun RapidWalletMainUi(
        padding: PaddingValues,
        rapidWalletViewModel: RapidWalletViewModel,
        paymentState: (RapidWalletResult) -> Unit,
    ) {
        val rwUserExistWalletData by rapidWalletViewModel.rapidUserExistUiState.collectAsState(
            RWUserWalletUiState.InitialUi(false)
        )

        val context = LocalContext.current

        var canShowPgBar by remember {
            mutableStateOf(false)
        }

        val currentUiState by rapidWalletViewModel.rapidWalletUiView.collectAsState(
            RapidWalletUiRepresentationModel.EnterUserIdUI(null)
        )

        canShowPgBar = rwUserExistWalletData.isLoading
        val keyboard = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderInformation(rapidWalletViewModel)
            when (currentUiState) {
                is RapidWalletUiRepresentationModel.EnterUserIdUI -> {
                    enableBackBtn = true
                    ShowEnterUserId(keyboard, rapidWalletViewModel, context) { ->
                        rapidWalletViewModel.apply {
                            if (!rapidUserIdCache.isNullOrEmpty()) {
                                showWarningDialog(subTitle = "Fetching wallet details...")
                                verifyRapidWalletAndBalance()
                            } else {
                                AppUtility.showToast(context, "Please enter Rapid User Id")
                            }
                        }
                    }
                }

                is RapidWalletUiRepresentationModel.WalletDetailsUI -> {
                    enableBackBtn = true
                    val data =
                        (currentUiState as RapidWalletUiRepresentationModel.WalletDetailsUI).x
                    ShowWalletBalances(data) {
                        if (it == EDIT_NUMBER) {
                            rapidWalletViewModel.rapidUserExistUiState.update {
                                RWUserWalletUiState.ShowUserIdInput(false)
                            }
                        }
                        if (it == SEND_OTP) {
                            enableBackBtn = false
                            showWarningDialog(subTitle = "We are sending OTP to your mobile number...")
                            rapidWalletViewModel.sendOtpToRapidUser(rapidWalletViewModel.rapidUserIdCache)
                        }
                    }
                }

                is RapidWalletUiRepresentationModel.OtpUi -> {
                    rapidWalletViewModel.rapidUserIdCache?.let {
                        RapidUserOTPUi(it, keyboard, { userInputOTP ->
                            if (rapidWalletViewModel.verifyUserInputOTP(userInputOTP)) {
                                enableBackBtn = false
                                showWarningDialog(subTitle = "Please Don't back pressed or close the app. We are processing your payment with Rapid Wallet")
                                rapidWalletViewModel.createPaymentWithRapidWallet()
                            } else {
                                enableBackBtn = true
                                canShowPgBar = false
                                AppUtility.showToast(context, "Entered invalid otp")
                            }
                        }) {
                            //change Id
                            enableBackBtn = true
                            canShowPgBar = false
                            rapidWalletViewModel.rapidUserExistUiState.update {
                                RWUserWalletUiState.ShowUserIdInput(false)
                            }
                        }
                    }
                }
            }
            RapidWalletTerms()
        }

        LaunchedEffect(key1 = rwUserExistWalletData, block = {
            when (rwUserExistWalletData) {
                is RWUserWalletUiState.ShowUserIdInput -> {
                    rapidWalletViewModel.rapidWalletUiView.update {
                        RapidWalletUiRepresentationModel.EnterUserIdUI(null)
                    }
                }

                is RWUserWalletUiState.Success -> {
                    val successData: RwUserExistWalletData? =
                        (rwUserExistWalletData as RWUserWalletUiState.Success).data
                    rapidWalletViewModel.rapidWalletUiView.update {
                        RapidWalletUiRepresentationModel.WalletDetailsUI(successData)
                    }
                }

                is RWUserWalletUiState.GetRapidWalletOtp -> {
                    rapidWalletViewModel.rapidWalletUiView.update {
                        RapidWalletUiRepresentationModel.OtpUi(null)
                    }
                    AppUtility.showToast(context, "Otp sent successfully")
                }

                is RWUserWalletUiState.InitialUi -> {

                }

                is RWUserWalletUiState.ErrorUi -> {
                    enableBackBtn = true
                    (rwUserExistWalletData as RWUserWalletUiState.ErrorUi).message?.let {
                        AppUtility.showToast(
                            context,
                            it
                        )
                    } ?: kotlin.run {
                        AppUtility.showToast(
                            context,
                            (rwUserExistWalletData as RWUserWalletUiState.ErrorUi).errors?.getTextMsg()
                        )
                    }
                }

                is RWUserWalletUiState.CreatePaymentSuccess -> {

                    (rwUserExistWalletData as RWUserWalletUiState.CreatePaymentSuccess).data?.let {
                        AppUtility.showToast(context, it.message)
                        when (it.status) {
                            "success" -> {
                                rapidWalletViewModel.getOrderId()?.let { orderId ->
                                    paymentState.invoke(RapidWalletResult(orderId = orderId, 1))
                                }
                            }

                            "failure" -> {
                                enableBackBtn = true
                                rapidWalletViewModel.getOrderId()?.let { orderId ->
                                    paymentState.invoke(RapidWalletResult(orderId = orderId, 2))
                                }
                            }

                            else -> {

                            }
                        }
                    }
                }
            }
        })

        if (!canShowPgBar) {
            paymentWarningDialog?.dismiss()
            paymentWarningDialog = null
        }
    }
}

private const val RAPID_BANK = 0
private const val RAPID_WALLET = 1
private const val EDIT_NUMBER = 12
private const val SEND_OTP = 13

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ShowEnterUserId(
    keyboard: SoftwareKeyboardController?,
    rapidWalletViewModel: RapidWalletViewModel,
    context: Context, getWalletDetails: () -> Unit,
) {
    var rapidUserId by remember {
        mutableStateOf("")
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = colorWhite),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .wrapContentHeight(), elevation = CardDefaults.cardElevation(defaultElevation = dp_8)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter Rapid Wallet User ID*",
                style = ZustTypography.bodyMedium,
                color = colorResource(id = R.color.black_2),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(modifier = Modifier
                .background(
                    color = colorResource(id = R.color.input_field_bg_color),
                    shape = RoundedCornerShape(8.dp)
                )
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
                textStyle = ZustTypography.bodyMedium,
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

            Button(
                modifier = Modifier.background(
                    color = colorResource(id = R.color.new_material_primary),
                    shape = RoundedCornerShape(8.dp)
                ), onClick = {
                    if (rapidUserId.isEmpty()) {
                        AppUtility.showToast(context, "Please enter correct User ID")
                        return@Button
                    }
                    if (!rapidWalletViewModel.rapidUserExistUiState.value.isLoading) {
                        keyboard?.hide()
                        rapidWalletViewModel.rapidUserIdCache = rapidUserId
                        getWalletDetails.invoke()
                    } else {
                        AppUtility.showToast(context, "Please wait")
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.new_material_primary)
                )
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Next",
                    color = colorResource(id = R.color.white),
                    style = ZustTypography.bodyMedium
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
private fun ShowWalletBalances(
    successData: RwUserExistWalletData?, userActionCallback: (Int) -> Unit,
) {
    var currentRapidMethod by remember {
        mutableStateOf(RAPID_WALLET)
    }

    if (successData?.rapidWallet?.userId.isNullOrEmpty()) {
        return
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = colorWhite),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = dp_8), shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .wrapContentHeight(),
        ) {
            val (nextBtn) = createRefs()
            val (rapidUserId, editUserId) = createRefs()

            Text(
                text = successData?.rapidWallet?.userId!!,
                modifier = Modifier.constrainAs(rapidUserId) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = ZustTypography.titleLarge,
                color = colorResource(id = R.color.new_material_primary)
            )

            Text(text = "Edit", modifier = Modifier
                .constrainAs(editUserId) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clickable {
                    userActionCallback.invoke(EDIT_NUMBER)
                }, style = ZustTypography.bodyMedium, color = colorResource(id = R.color.green)
            )

            val (title2, subtitle2, balance2, radioBtn2) = createRefs()

            successData.rapidWallet.walletBalance?.let {
                Text(
                    text = "Rapid Wallet",
                    modifier = Modifier.constrainAs(title2) {
                        top.linkTo(rapidUserId.bottom, dp_20)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                    style = ZustTypography.bodyMedium,
                    color = colorResource(id = R.color.app_black)
                )

                Text(text = "Available Balance:- ",
                    modifier = Modifier
                        .constrainAs(subtitle2) {
                            top.linkTo(title2.bottom, dp_8)
                            start.linkTo(parent.start)
                        }
                        .clickable {
                            currentRapidMethod = RAPID_WALLET
                        },
                    style = ZustTypography.bodyMedium,
                    color = colorResource(id = R.color.black_2)
                )
                Text(
                    text = it.toString(),
                    modifier = Modifier.constrainAs(balance2) {
                        top.linkTo(subtitle2.top)
                        start.linkTo(subtitle2.end, dp_12)
                        bottom.linkTo(subtitle2.bottom)
                        width = Dimension.fillToConstraints
                    },
                    style = ZustTypography.bodyMedium,
                    color = colorResource(id = R.color.new_material_primary)
                )

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
                .background(
                    color = colorResource(id = R.color.new_material_primary),
                    shape = RoundedCornerShape(8.dp)
                ), onClick = {
                userActionCallback.invoke(SEND_OTP)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.new_material_primary)
            )
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Send OTP",
                    color = colorResource(id = R.color.white),
                    style = ZustTypography.bodyMedium
                )
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
    Card(colors = CardDefaults.cardColors(containerColor = colorWhite),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .wrapContentHeight(), elevation = CardDefaults.cardElevation(defaultElevation = dp_8), shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .wrapContentHeight()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "OrderId:- ",
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier,
                    color = colorResource(id = R.color.new_material_primary)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (rapidWalletViewModel.intentSource == INTENT_SOURCE_NON_VEG) {
                        PREFIX_ORDER_ID_NON_VEG + rapidWalletViewModel.paymentActivityReqData?.orderId.toString()
                    } else {
                        PREFIX_ORDER_ID_GROCERY + rapidWalletViewModel.paymentActivityReqData?.orderId.toString()
                    },
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Payable Amount:- ",
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier,
                    color = colorResource(id = R.color.new_material_primary)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(
                        rapidWalletViewModel.getPayablePrice()
                    ), style = ZustTypography.bodyMedium, modifier = Modifier
                )
            }

        }
    }
}

@Composable
private fun RapidWalletTerms() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (rapidIcon) = createRefs()
        Image(painter = painterResource(id = R.drawable.rapid_bazar_icon),
            contentDescription = "rapid_bazaar",
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RapidUserOTPUi(
    userId: String,
    keyboard: SoftwareKeyboardController?,
    verifyUserOTP: (String) -> Unit,
    changeIdCallback: () -> Unit,
) {
    val context: Context = LocalContext.current
    var rapidUserInputOTP by remember {
        mutableStateOf("")
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = colorWhite),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .wrapContentHeight(), elevation = CardDefaults.cardElevation(defaultElevation = dp_8), shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            val (rapidUserId, editUserId, otpField, enterOTPTitle, verifyBtn) = createRefs()
            Text(
                text = userId,
                modifier = Modifier.constrainAs(rapidUserId) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = ZustTypography.titleLarge,
                color = colorResource(id = R.color.new_material_primary)
            )

            Text(text = "Change ID", modifier = Modifier
                .constrainAs(editUserId) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clickable {
                    changeIdCallback.invoke()
                }, style = ZustTypography.bodyMedium,
                color = colorResource(id = R.color.green)
            )

            Text(text = "Enter OTP", modifier = Modifier.constrainAs(enterOTPTitle) {
                top.linkTo(rapidUserId.bottom, dp_20)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodyMedium)

            BasicTextField(modifier = Modifier
                .constrainAs(otpField) {
                    top.linkTo(enterOTPTitle.bottom, dp_8)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(
                    color = colorResource(id = R.color.input_field_bg_color),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 12.dp, horizontal = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboard?.hide()
                }),
                value = rapidUserInputOTP,
                onValueChange = { newText ->
                    rapidUserInputOTP = newText
                },
                textStyle = ZustTypography.bodyMedium,
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

            Button(modifier = Modifier
                .background(
                    color = colorResource(id = R.color.new_material_primary),
                    shape = RoundedCornerShape(8.dp)
                )
                .constrainAs(verifyBtn) {
                    top.linkTo(otpField.bottom, dp_20)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onClick = {
                if (rapidUserInputOTP.isNotEmpty()) {
                    verifyUserOTP.invoke(rapidUserInputOTP)
                } else {
                    AppUtility.showToast(context = context, "Please enter OTP")
                }
            }, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.new_material_primary)
            )
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = stringResource(id = R.string.verify_otp),
                    color = colorResource(id = R.color.white),
                    style = ZustTypography.bodyMedium
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}


@Composable
fun PaymentWindow(closeWindow: () -> Unit) {
    val secondsRemaining by remember { mutableIntStateOf(300) }
    Text(
        text = "Time remaining: ${secondsRemaining / 60}:${secondsRemaining % 60}"
    )

    if (secondsRemaining == 0) {
        closeWindow.invoke()
    }
    LaunchedEffect(key1 = Unit, block = {
        startPaymentTimer {

        }
    })
}

//5 minutes payment window
private fun startPaymentTimer(onComplete: () -> Unit) {
    Timer().schedule(object : TimerTask() {
        override fun run() {
            onComplete()
        }
    }, 300000)
}
