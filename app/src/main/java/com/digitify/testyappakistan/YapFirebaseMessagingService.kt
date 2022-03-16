package com.digitify.testyappakistan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import com.digitify.testyappakistan.onboarding.splash.SplashActivity
import com.digitify.testyappakistan.utils.Constants.FLOW_ID
import com.digitify.testyappakistan.utils.Constants.REGION

class YapFirebaseMessagingService {
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    fun onNewToken(token: String) {

        Log.d("YapFirebaseMessaging>>", token)
    }

    fun onMessageReceived(context: Context, remoteMessage: String) {

        sendNotification(context, remoteMessage)
    }

    private fun sendNotification(
        context: Context,
        remoteMessage: String
    ) {
//        intent.putExtra(
//            Constants.EXTRA,
//            bundleOf(
//                "flow_id" to DeepLinkNavigation.DeepLinkFlow.TRANSACTION_DETAILS.flowId
//            )
//        )

        val notificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        createNotificationChannel(notificationManager)
        val notificationId = java.util.Random().nextInt()
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
//        intent.putExtra("flow_id", remoteMessage)
        intent.putExtra(
            FLOW_ID,
            bundleOf(
                FLOW_ID to "TransactionDetails"
            )
        )
        intent.putExtra(
            REGION,
            bundleOf(
                REGION to "+92"
            )
        )
        val pendingIntent = PendingIntent.getActivity(
            context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT,
            bundleOf()
        )
        remoteMessage.let { notificationTitle ->
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, getNotificationChannelId())
                    .setAutoCancel(true).setSmallIcon(R.drawable.ic_yap).setContentTitle(
                        notificationTitle
                    ).setContentText(remoteMessage ?: "")
            builder.setContentIntent(pendingIntent)
            notificationManager.notify(
                notificationId, builder.build()
            )
        }

    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val morningNotificationChannel =
                NotificationChannel(
                    getNotificationChannelId(),
                    getNotificationChannelName(),
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(morningNotificationChannel)
        }
    }

    private fun getNotificationChannelId() = "transaction"
    private fun getNotificationChannelName() = "NotificationChannelName"

}