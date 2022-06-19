package com.wisnu.kurniawan.debugview.internal.features.notification.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.analytic.ui.AnalyticFragment
import com.wisnu.kurniawan.debugview.internal.foundation.extension.getLaunchIntent
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toMillis
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event

internal class EventNotificationManager(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    init {
        initChannel()
    }

    private fun initChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.debug_view_channel_name)
            val description = context.getString(R.string.debug_view_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW

            NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
                enableLights(true)
                lightColor = ResourcesCompat.getColor(context.resources, R.color.debugview_primary, null)
                enableVibration(true)
                notificationManager?.createNotificationChannel(this)
            }
        }
    }

    fun show(analytic: Analytic, events: List<Event>) {
        if (events.isEmpty()) return

        val builder = buildNotification(analytic, events)
        val id = analytic.createdAt.toMillis().toInt()
        notificationManager?.notify(
            id,
            builder.build()
        )
    }

    fun dismiss(analytic: Analytic) {
        val id = analytic.createdAt.toMillis().toInt()
        notificationManager?.cancel(id)
    }

    private fun buildNotification(analytic: Analytic, events: List<Event>): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentIntent(buildPendingIntent(analytic.tag))
            setLocalOnly(true)
            setSmallIcon(R.drawable.debugview_ic_out)
            setColor(ResourcesCompat.getColor(context.resources, R.color.debugview_primary, null))
            setContentTitle(context.getString(R.string.debug_view_title, analytic.tag))
            setAutoCancel(true)
            setColorized(true)
            setStyle(buildInboxStyle(this, events))
            setCount(this, events)
        }
    }

    private fun buildPendingIntent(tag: String): PendingIntent {
        val openTaskIntent = context.getLaunchIntent().apply {
            putExtra(AnalyticFragment.EXTRA_TAG, tag)
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getActivity(context, 0, openTaskIntent, flags)
    }

    private fun setCount(builder: NotificationCompat.Builder, events: List<Event>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setSubText(events.count().toString())
        } else {
            builder.setNumber(events.count())
        }
    }

    private fun buildInboxStyle(builder: NotificationCompat.Builder, events: List<Event>): NotificationCompat.InboxStyle {
        val inboxStyle = NotificationCompat.InboxStyle()
        events
            .forEachIndexed { index, event ->
                if (index == 0) {
                    builder.setContentText(event.name)
                }
                inboxStyle.addLine(event.name)
            }
        return inboxStyle
    }

    companion object {
        private const val REQUEST_CODE_OPEN_ANALYTIC = 2
        private const val BUFFER_SIZE = 10
        private const val CHANNEL_ID = "task_notification_channel"
    }
}
