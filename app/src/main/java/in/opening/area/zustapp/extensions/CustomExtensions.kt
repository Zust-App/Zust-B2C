package `in`.opening.area.zustapp.extensions

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.KeyboardType
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


fun String.showToastMessage(context: Context?) {
    if (context != null ) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}
fun getNumberKeyboardOptions(): KeyboardOptions {
   return KeyboardOptions(keyboardType = KeyboardType.Number)
}

fun Fragment.contentView(
    compositionStrategy: ViewCompositionStrategy = ViewCompositionStrategy.DisposeOnDetachedFromWindow,
    context: Context? = getContext(),
    content: @Composable () -> Unit
): ComposeView? {
    context ?: return null
    val view = ComposeView(context)
    view.setViewCompositionStrategy(compositionStrategy)
    view.setContent(content)
    return view
}

/**
 * The function first checks if the bottomsheet has already been shown
 * using this FragmentManager,
 * If it has not, it creates an instance of the bottomsheet and displays
 *
 * @param bundle Optional bundle to pass ar argument to the bottomsheet
 */
@Suppress("UNCHECKED_CAST")
fun <T> FragmentManager.showBottomSheetIsNotPresent(
    bottomSheetDialogFragmentClass: Class<*>,
    tag: String,
    bundle: Bundle? = null,
): T {
    /**
     * Check if bottomSheetDialogFragmentClass extends BottomSheetDialogFragment
     */
    if (!(BottomSheetDialogFragment::class.java.isAssignableFrom(bottomSheetDialogFragmentClass))) {
        throw Exception("bottomSheetDialogFragmentClass does not extend com.google.android.material.bottomsheet.BottomSheetDialogFragment")
    }

    /**
     * Check if the FragmentManager already contains a fragment with the given tag
     */
    return if (this.findFragmentByTag(tag) == null) {
        val bottomSheetDialogFragment =
            bottomSheetDialogFragmentClass.newInstance() as BottomSheetDialogFragment
        bottomSheetDialogFragment.arguments = bundle
        if (!this.isStateSaved) {
            bottomSheetDialogFragment.show(this, tag)
        }
        bottomSheetDialogFragment as T
    } else {
        this.findFragmentByTag(tag) as BottomSheetDialogFragment as T
    }
}

fun FragmentManager.showBottomSheetIsNotPresent(
    bottomSheetDialogFragmentClass: DialogFragment,
    tag: String,
): DialogFragment {

    return if (this.findFragmentByTag(tag) == null && !this.isStateSaved) {
        bottomSheetDialogFragmentClass.show(this, tag)
        bottomSheetDialogFragmentClass
    } else {
        this.findFragmentByTag(tag) as DialogFragment
    }
}

/**
 * The function first checks if the DialogFragment has already been shown
 * using this FragmentManager,
 * If it has not, it creates an instance of the DialogFragment and displays
 *
 * @param bundle Optional bundle to pass ar argument to the DialogFragment
 */
@Suppress("UNCHECKED_CAST")
fun <T> FragmentManager.showDialogFragmentIfNotPresent(
    dialogFragmentClass: Class<*>,
    tag: String,
    bundle: Bundle? = null,
): T {
    /**
     * Check if bottomSheetDialogFragmentClass extends BottomSheetDialogFragment
     */
    if (!(DialogFragment::class.java.isAssignableFrom(dialogFragmentClass))) {
        throw Exception("dialogFragmentClass does not extend androidx.fragment.app.DialogFragment")
    }

    /**
     * Check if the FragmentManager already contains a fragment with the given tag
     */
    return if (this.findFragmentByTag(tag) == null && !this.isStateSaved) {
        val dialogFragment = dialogFragmentClass.newInstance() as DialogFragment
        dialogFragment.arguments = bundle
        dialogFragment.show(this, tag)
        dialogFragment as T
    } else {
        this.findFragmentByTag(tag) as DialogFragment as T
    }
}

fun View.showViewWithAnimation(enableAnimation: Boolean=false) {
    this.visibility = View.VISIBLE
    if (enableAnimation) {
        this.animate().alpha(1f).setDuration(500).start()
    }
}

fun View.hideViewWithAnimation(enableAnimation: Boolean=false) {
    this.visibility = View.GONE
    if (enableAnimation) {
        this.animate().alpha(0f).setDuration(500).start()
    }
}

