package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.login.fragment.OtpVerificationFragment
import `in`.opening.area.zustapp.login.fragment.PhoneInputFragment
import `in`.opening.area.zustapp.login.fragment.UserDetailFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class OnBoardingFragmentManager(private val fragmentManager: FragmentManager, private var containerId: Int?) {

    fun setUpFragmentBasedOnId(fragType: String) {
        if (containerId != null) {
            val fragCanonicalName = getCanonicalNameOfFragment(fragType)
            var fragment = findFragmentByTag(fragCanonicalName)
            if (fragment == null) {
                fragment = getFragmentBasedOnType(fragType)
                fragmentManager.beginTransaction().add(containerId!!, fragment, fragCanonicalName).addToBackStack(null).commit()
            }
            showAlreadyAddedFragment(fragment)
        }
    }

    private fun findFragmentByTag(fragCanonicalName: String): Fragment? {
        return fragmentManager.findFragmentByTag(fragCanonicalName)
    }

    private fun getFragmentBasedOnType(fragType: String): Fragment {
        return when (fragType) {
            LoginNav.MOVE_TO_PHONE -> {
                PhoneInputFragment.newInstance()
            }
            LoginNav.MOVE_TO_OTP -> {
                OtpVerificationFragment.newInstance()
            }
            LoginNav.MOVE_TO_PROFILE -> {
                UserDetailFragment.newInstance()
            }
            else -> {
                PhoneInputFragment.newInstance()
            }
        }
    }


    private fun getCanonicalNameOfFragment(fragType: String): String {
        return when (fragType) {
            LoginNav.MOVE_TO_PHONE -> {
                PhoneInputFragment::class.java.name
            }
            LoginNav.MOVE_TO_PROFILE -> {
                UserDetailFragment::class.java.name
            }
            LoginNav.MOVE_TO_OTP -> {
                OtpVerificationFragment::class.java.name
            }
            else -> {
                PhoneInputFragment::class.java.name
            }
        }
    }

    private fun showAlreadyAddedFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        for (i in fragmentManager.fragments.indices) {
            val f = fragmentManager.fragments[i]
            if (f::class.java.name == fragment::class.java.name) {
                transaction.show(f)
            } else {
                transaction.hide(f)
            }
        }
        transaction.commit()
    }

}