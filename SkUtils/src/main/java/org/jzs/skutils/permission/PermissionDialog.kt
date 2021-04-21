package org.jzs.skutils.permission

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import org.jzs.skutils.R


/**
 * Created by Jzs on 2017/11/2 0002.
 */
class PermissionDialog(internal var mContext: Context) {

    private val mBuilder: AlertDialog.Builder

    /**
     * The dialog's btn click listener.
     */
    private val mClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_NEGATIVE -> {
            }
            DialogInterface.BUTTON_POSITIVE -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                mContext.startActivity(intent)
            }
        }
    }

    init {
        mBuilder = AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle(R.string.permission_title_permission_failed)
                .setMessage(R.string.permission_message_permission_failed)
                .setPositiveButton(R.string.permission_setting, mClickListener)
                .setNegativeButton(R.string.permission_cancel, mClickListener)
    }

    fun setTitle(title: String): PermissionDialog {
        mBuilder.setTitle(title)
        return this
    }

    fun setTitle(@StringRes title: Int): PermissionDialog {
        mBuilder.setTitle(title)
        return this
    }

    fun setMessage(message: String): PermissionDialog {
        mBuilder.setMessage(message)
        return this
    }

    fun setMessage(@StringRes message: Int): PermissionDialog {
        mBuilder.setMessage(message)
        return this
    }

    fun setNegativeButton(text: String, negativeListener: DialogInterface.OnClickListener?): PermissionDialog {
        mBuilder.setNegativeButton(text, negativeListener)
        return this
    }

    fun setNegativeButton(@StringRes text: Int, negativeListener: DialogInterface.OnClickListener?): PermissionDialog {
        mBuilder.setNegativeButton(text, negativeListener)
        return this
    }

    fun setPositiveButton(text: String): PermissionDialog {
        mBuilder.setPositiveButton(text, mClickListener)
        return this
    }

    fun setPositiveButton(@StringRes text: Int): PermissionDialog {
        mBuilder.setPositiveButton(text, mClickListener)
        return this
    }

    fun show() {
        mBuilder.show()
    }

    interface SettingService {

        /**
         * Execute setting.
         */
        fun execute()

        fun cancel()

    }

}
