package `in`.opening.area.zustapp.login.fragment

import `in`.opening.area.zustapp.extensions.contentView
import `in`.opening.area.zustapp.login.FragmentActionListener
import `in`.opening.area.zustapp.login.LoginMainContainer
import `in`.opening.area.zustapp.uiModels.login.GetOtpLoginUi
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.flow.update

class PhoneInputFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private var listener: FragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActionListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = contentView(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)) {
        loginViewModel.getOtpUiState.update { GetOtpLoginUi.InitialUi(false) }
        LoginMainContainer(loginViewModel = loginViewModel) {
            listener?.action(it)
        }
    }
    companion object{
        fun newInstance(): PhoneInputFragment{
            val args = Bundle()
            val fragment = PhoneInputFragment()
            fragment.arguments = args
            return fragment
        }
    }



}