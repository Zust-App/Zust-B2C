package zustElectronics.zeLanding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import dagger.hilt.android.AndroidEntryPoint
import zustElectronics.zeLanding.ui.ZeHomePageMainUi
import zustElectronics.zeLanding.viewmodel.ZeEntryPointViewModel

@AndroidEntryPoint
class ZeEntryPointActivity : AppCompatActivity() {
    private val zeEntryPointViewModel: ZeEntryPointViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeHomePageMainUi(zeEntryPointViewModel)
            LaunchedEffect(Unit) {
                zeEntryPointViewModel.getAllZeProducts()
            }
        }
    }

}