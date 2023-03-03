package `in`.opening.area.zustapp.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

interface ComposeAbstraction {
    @Composable
    fun CollectAllViews()

    @Composable
    @Preview
    fun PreviewAllViews()
}