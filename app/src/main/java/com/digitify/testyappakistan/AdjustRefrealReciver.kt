package com.digitify.testyappakistan

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adjust.sdk.Adjust
import com.digitify.core.adjustRefferal.ReferralInfo
import com.digitify.core.utils.REFERRAL_ID
import com.digitify.core.utils.REFERRAL_TIME
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.testyappakistan.onboarding.demo.DemoMainActivity
import com.digitify.testyappakistan.onboarding.splash.SplashActivity
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AdjustReferrerReceiver : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.resolveActivity(packageManager) != null && intent.resolveActivity(packageManager).packageName == BuildConfig.APPLICATION_ID) {
            intent.data?.let { uri ->
                Adjust.appWillOpenUrl(uri, this)
                val customerId = uri.getQueryParameter(REFERRAL_ID)
                customerId?.let { cusId ->
                    uri.getQueryParameter(REFERRAL_TIME)?.let { time ->
                        sharedPreferenceManager.setReferralInfo(
                            ReferralInfo(
                                cusId,
                                time
                            )
                        )
                        takeDecision()
                    } ?: takeDecision()
                } ?: takeDecision()
            } ?: takeDecision()
        }
    }


    private fun isRunning(ctx: Context): Boolean {
        val activityManager =
            ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks: List<ActivityManager.RunningTaskInfo> =
            activityManager.getRunningTasks(Int.MAX_VALUE)
        for (task in tasks) {
            if (SplashActivity::class.java.canonicalName.equals(
                    task.baseActivity?.shortClassName,
                    ignoreCase = true
                )
                ||
                DemoMainActivity::class.java.canonicalName.equals(
                    task.baseActivity?.shortClassName,
                    ignoreCase = true
                )
            ) {
                return true
            }
        }
        return false
    }

    private fun takeDecision() {
        if (isRunning(this))
            finish()
        else
            launchYapDashboard()
    }

    private fun isDashboardRunning(ctx: Context): Boolean {
        val activityManager =
            ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks: List<ActivityManager.RunningTaskInfo> =
            activityManager.getRunningTasks(Int.MAX_VALUE)
        for (task in tasks) {
            if (PkDashboardActivity::class.java.canonicalName.equals(
                    task.baseActivity?.shortClassName,
                    ignoreCase = true
                )
            ) {
                return true
            }
        }
        return false
    }

    private fun launchYapDashboard() {
        if (isDashboardRunning(this)) {
            val i = Intent(this, PkDashboardActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        } else {
            startLauncherActivity()
        }
    }

    private fun startLauncherActivity() {
        startActivity(packageManager.getLaunchIntentForPackage(packageName))
        finish()
    }
}
