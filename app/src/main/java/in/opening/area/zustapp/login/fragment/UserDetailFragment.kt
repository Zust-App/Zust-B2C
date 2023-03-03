package `in`.opening.area.zustapp.login.fragment

import `in`.opening.area.zustapp.extensions.contentView
import `in`.opening.area.zustapp.login.FragmentActionListener
import `in`.opening.area.zustapp.login.ProfileContainer
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
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
        ProfileContainer(loginViewModel = loginViewModel) {
            listener?.action(it)
        }
    }

    companion object{
        fun newInstance(): UserDetailFragment{
            val args = Bundle()
            val fragment = UserDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


}