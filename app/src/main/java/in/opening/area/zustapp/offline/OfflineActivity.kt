package `in`.opening.area.zustapp.offline

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeLottieWithoutScope
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_16
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

class OfflineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConstraintLayout(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()) {
                val (noInternetLottie, titleText, settingBtn) = createRefs()
                ComposeLottieWithoutScope(rawId = R.raw.no_internet,
                    modifier = Modifier
                        .size(200.dp)
                        .constrainAs(noInternetLottie) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                Text(text = "No internet connection",
                    modifier = Modifier.constrainAs(titleText) {
                        bottom.linkTo(noInternetLottie.bottom)
                        start.linkTo(parent.start, dp_16)
                        end.linkTo(parent.end, dp_16)
                        width = Dimension.fillToConstraints
                    }, textAlign = TextAlign.Center, style = Typography_Montserrat.body1)
                Button(modifier = Modifier.constrainAs(settingBtn) {
                    top.linkTo(noInternetLottie.bottom, dp_16)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                        startActivity(panelIntent)
                    } else {
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                }) {
                    Text(text = "Open settings", style = Typography_Montserrat.body1,
                        color = colorResource(id = R.color.white))
                }
            }
        }

        ConnectionLiveData(applicationContext).observe(
            this
        ) {
            if (it) {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        // do nothing
    }
}