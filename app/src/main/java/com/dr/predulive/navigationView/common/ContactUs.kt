package com.dr.predulive.navigationView.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.dr.predulive.R

class ContactUs {

    fun loadContactUs(applicationContext: Context, activity: Activity) {
        val url:String = "https://predulive.com/contact.php";

        // initializing object for custom chrome tabs.
        val customIntent = CustomTabsIntent.Builder()
        // below line is setting toolbar color
        // for our custom chrome tab.
        customIntent.setToolbarColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorAccent
            )
        )
        // we are calling below method after
        // setting our toolbar color.
        openCustomTab(activity, customIntent.build(), Uri.parse(url))
    }

    private fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri?) {
        // package name is the default package
        // for our custom chrome tab
        val packageName = "com.android.chrome"
        if (packageName != null) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName)

            // in that custom tab intent we are passing
            // our url which we have to browse.
            customTabsIntent.launchUrl(activity, uri!!)
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}