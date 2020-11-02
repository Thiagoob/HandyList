package com.celerocommerce.handylist.ui

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.celerocommerce.handylist.R

fun Activity.displayToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displaySuccessDialog(@StringRes message: Int) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(text = getString(message))
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displaySuccessDialog(message: String?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayErrorDialog(@StringRes message: Int) {
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = getString(message))
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayErrorDialog(message: String?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}