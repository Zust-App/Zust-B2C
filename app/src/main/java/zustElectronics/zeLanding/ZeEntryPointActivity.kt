package zustElectronics.zeLanding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import zustElectronics.zeLanding.ui.ZeHomePageMainUi

@AndroidEntryPoint
class ZeEntryPointActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeHomePageMainUi()
        }
    }

}