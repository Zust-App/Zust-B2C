package `in`.opening.area.zustapp.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.ui.theme.dp_16
import non_veg.payment.ui.ViewSpacer20
import ui.colorBlack

@Composable
fun FullScreenErrorUi(
    errorCode: Int?,
    retryCallback: () -> Unit,
    changeLocation: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        if (errorCode == ApiRequestManager.NOT_COVERAGE_ERROR_CODE) {
            Text(text = "Service not available in \nyou area currently",
                color = colorResource(id = R.color.black),
                style = ZustTypography.titleLarge,
                modifier = Modifier.padding(horizontal = dp_16, vertical = 8.dp),
                textAlign = TextAlign.Center)

            Text(text = "But we are working on it.\nSo Hold on!", color = colorResource(id = R.color.black_3),
                style = ZustTypography.bodySmall.copy(fontSize = 16.sp),
                modifier = Modifier.padding(horizontal = dp_16),
                textAlign = TextAlign.Center)

            ViewSpacer20()

            Image(painter = painterResource(id = R.drawable.no_service_available), contentDescription = "no internet")

            ViewSpacer20()

            OutlinedButton(onClick = {
                changeLocation.invoke()
            }, modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(), border = BorderStroke(color = colorResource(id = R.color.screen_surface_color), width = 1.dp)) {
                Text(text = "Change Location",
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = colorBlack)
            }
        } else {

            Text(text = "Oops, Something Went \n" +
                    "wrong!",
                color = colorResource(id = R.color.black),
                style = ZustTypography.titleLarge,
                modifier = Modifier.padding(horizontal = dp_16, vertical = 8.dp),
                textAlign = TextAlign.Center)

            Text(text = "Please try again in sometime.", color = colorResource(id = R.color.black_3),
                style = ZustTypography.bodySmall.copy(fontSize = 16.sp),
                modifier = Modifier.padding(horizontal = dp_16),
                textAlign = TextAlign.Center)

            ViewSpacer20()

            Image(painter = painterResource(id = R.drawable.error_icon), contentDescription = "error icon")

            ViewSpacer20()

            OutlinedButton(onClick = {
                retryCallback.invoke()
            }, modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(), border = BorderStroke(color = colorResource(id = R.color.screen_surface_color), width = 1.dp)) {
                Text(text = "Retry",
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = colorBlack)
            }
        }
    }
}