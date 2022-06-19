package com.wisnu.kurniawan.debugview

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.wisnu.kurniawan.debugview.internal.features.main.di.MainModule
import com.wisnu.kurniawan.debugview.internal.features.notification.di.EventNotificationModule
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import com.wisnu.kurniawan.debugview.internal.foundation.extension.getLaunchIntent
import com.wisnu.kurniawan.debugview.internal.foundation.extension.require
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


object DebugView {

    private const val DEBUG_VIEW_SHORTCUT_ID = "com.gojek.merchant.debugdrawer.dynamic_shortcut"
    private const val DEBUG_VIEW_REQUEST_CODE = 10000
    private const val NOTIFICATION_ID = 9999
    private const val NOTIFICATION_CHANNEL_ID = "debug-drawer"

    private val idProvider by lazy { IdProviderImpl() }
    private val dateTimeProvider by lazy { DateTimeProviderImpl() }

    fun init(context: Context, vararg tags: String) {
        require(tags.toList())

        GlobalScope.launch(Dispatchers.IO) {
            DataModule.inject(context, tags.toList())
            DataModule.inject(DataModule.db, dateTimeProvider)
            MainModule.inject(DataModule.localManager, idProvider)
            EventNotificationModule.inject(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                setShortcutEntryPoint(context)
            } else {
                showNotificationEntryPoint(context)
            }

            initListenAnalyticChanges()
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun setShortcutEntryPoint(context: Context) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)
        shortcutManager.addDynamicShortcuts(listOf(createDebugDrawerShortcut(context)))
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun createDebugDrawerShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, DEBUG_VIEW_SHORTCUT_ID)
            .setLongLabel(context.getString(R.string.debug_view_app_name))
            .setShortLabel(context.getString(R.string.debug_view_app_name))
            .setIcon(Icon.createWithResource(context, R.drawable.debugview_ic_eye))
            .setIntent(context.getLaunchIntent())
            .build()
    }

    private fun showNotificationEntryPoint(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            DEBUG_VIEW_REQUEST_CODE,
            context.getLaunchIntent(),
            flags
        )

        val notification = NotificationCompat.Builder(
            context,
            getNotificationChannelId(context, notificationManager)
        )
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.debugview_ic_eye)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle(context.getString(R.string.debug_view_app_name))
            .setContentText(context.getString(R.string.debug_view_app_name))
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(context, R.color.debugview_backgroundl1))
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()

        notificationManager.notify(DEBUG_VIEW_SHORTCUT_ID, NOTIFICATION_ID, notification)
    }

    private fun getNotificationChannelId(context: Context, manager: NotificationManager): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            if (channel == null) {
                channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.debug_view_channel_name),
                    NotificationManager.IMPORTANCE_LOW
                )
                manager.createNotificationChannel(channel)
            }
        }
        return NOTIFICATION_CHANNEL_ID
    }

    private suspend fun initListenAnalyticChanges() {
        MainModule.mainEnvironment?.getLast10EventWithAnalytic()
            ?.catch { }
            ?.collect { analytics ->
                analytics.forEach {
                    EventNotificationModule.eventNotificationManager.show(
                        it.analytic,
                        it.events
                    )
                }
            }
    }

    fun record(event: Event) {
        val createdAt = dateTimeProvider.now()
        GlobalScope.launch(Dispatchers.IO) {
            MainModule.mainEnvironment?.getAnalytic(event)
                ?.catch { }
                ?.collect {
                    MainModule.mainEnvironment?.insertEvent(it, event, createdAt)
                }
        }
    }
}
