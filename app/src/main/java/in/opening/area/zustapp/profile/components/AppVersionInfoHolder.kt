package `in`.opening.area.zustapp.profile.components

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun AppVersionInfoHolder() {
    val versionCode: Int = BuildConfig.VERSION_CODE
    val versionName: String = BuildConfig.VERSION_NAME
    Column(modifier = Modifier
        .padding(profilePaddingValues)
        .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "App Version $versionName",
            style = Typography_Montserrat.subtitle1, modifier = Modifier.padding(),
            color = colorResource(id = R.color.new_hint_color))
        Spacer(modifier = Modifier.height(12.dp))
    }
}