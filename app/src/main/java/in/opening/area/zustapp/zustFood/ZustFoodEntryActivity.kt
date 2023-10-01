package `in`.opening.area.zustapp.zustFood

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.montserratFontFamily

@AndroidEntryPoint
class ZustFoodEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = "Zust Food", subTitleText = null) {
                    finish()
                }
            }, content = { paddingValues ->
                ConstraintLayout(modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
                    .padding(paddingValues = paddingValues)
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    val (previewImage, upcomingTitleText) = createRefs()
                    Text(
                        modifier = Modifier
                            .constrainAs(upcomingTitleText) {
                                bottom.linkTo(parent.bottom, dp_24)
                                start.linkTo(parent.start, dp_20)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                            .wrapContentHeight(),
                        text = "COMING \nSOON",
                        style = TextStyle(
                            fontSize = 63.5.sp,
                            lineHeight = 81.86.sp,
                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF6750A4),
                        )
                    )
                }
            })
        }
    }
}