package `in`.opening.area.zustapp.login.fragment

import `in`.opening.area.zustapp.extensions.contentView
import `in`.opening.area.zustapp.login.FragmentActionListener
import `in`.opening.area.zustapp.login.OtpVerification
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class OtpVerificationFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()

    private var listener: FragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = contentView(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)) {
        OtpVerification(loginViewModel = loginViewModel) {
            listener?.action(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.startResendOtpTimer()
    }

    companion object {
        fun newInstance(): OtpVerificationFragment {
            val args = Bundle()
            val fragment = OtpVerificationFragment()
            fragment.arguments = args
            return fragment
        }
    }

}