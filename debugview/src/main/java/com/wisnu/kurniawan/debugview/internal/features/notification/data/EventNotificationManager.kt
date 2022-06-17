package com.wisnu.kurniawan.debugview.internal.foundation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.wisnu.kurniawan.debugview.Event
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toMillis
import com.wisnu.kurniawan.debugview.internal.model.Analytic


internal class NotificationManager(private val context: Context) {

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
                lightColor = ResourcesCompat.getColor(context.resources, R.color.primary, null)
                enableVibration(true)
                notificationManager?.createNotificationChannel(this)
            }
        }
    }

    fun show(analytic: Analytic, events: List<Event>) {
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
            setSmallIcon(R.drawable.debug_view_ic_out)
            setColor(ResourcesCompat.getColor(context.resources, R.color.primary, null))
            setContentTitle(context.getString(R.string.debug_view_title, analytic.tag))
            setAutoCancel(true)
            setColorized(true)
            addAction(getClearAction(analytic.tag))
            setStyle(buildInboxStyle(this, events))
            setCount(this, events)
        }
    }

    private fun buildPendingIntent(tag: String): PendingIntent {
        val openTaskIntent = Intent(
            Intent.ACTION_VIEW,
            StepFlow.TaskDetailScreen.deeplink(taskId, listId).toUri()
        )
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(openTaskIntent)
            getPendingIntent(REQUEST_CODE_OPEN_ANALYTIC, flags)
        }
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
            .takeLast(BUFFER_SIZE)
            .reversed()
            .forEachIndexed { index, event ->
                if (index + 1 == BUFFER_SIZE) {
                    builder.setContentText(event.name)
                }
                inboxStyle.addLine(event.name)
            }
        return inboxStyle
    }

    private fun getClearAction(tag: String): NotificationCompat.Action {
        val clearTitle: CharSequence = context.getString(R.string.chuck_clear)
        val deleteIntent = Intent(context, ClearEventsService::class.java).apply {
            putExtra(ClearEventsService.EXTRA_TAG, tag)
        }
        val intent = PendingIntent.getService(context, REQUEST_CODE_ACTION_CLEAR, deleteIntent, PendingIntent.FLAG_ONE_SHOT)
        return NotificationCompat.Action(
            R.drawable.debug_view_ic_delete,
            clearTitle, intent
        )
    }

    companion object {
        private const val REQUEST_CODE_ACTION_CLEAR = 1
        private const val REQUEST_CODE_OPEN_ANALYTIC = 2
        private const val BUFFER_SIZE = 10
        private const val CHANNEL_ID = "task_notification_channel"
    }
}
