package `in`.opening.area.zustapp.ui.generic

import `in`.opening.area.zustapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun CustomUpBtn(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .size(36.dp)
            .background(
                shape = CircleShape, color = colorResource(id = R.color.new_material_primary)
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(), tint = Color.White,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

@Composable
fun mirroringIcon(ltrIcon: ImageVector, rtlIcon: ImageVector): ImageVector =
    if (LocalLayoutDirection.current == LayoutDirection.Ltr) ltrIcon else rtlIcon

/**
 * Returns the correct back navigation icon based on the current layout direction.
 */
@Composable
fun mirroringBackIcon() = mirroringIcon(
    ltrIcon = Icons.Outlined.ArrowBack,
    rtlIcon = Icons.Outlined.ArrowForward
)

@Composable
fun mirroringUpArrowIcon() = mirroringIcon(
    ltrIcon = Icons.Outlined.KeyboardArrowUp,
    rtlIcon = Icons.Outlined.KeyboardArrowDown
)