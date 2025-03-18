package sdmed.extra.cso.bases

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.EntryPointAccessors
import sdmed.extra.cso.R
import sdmed.extra.cso.interfaces.services.INotificationServiceEntryPoint
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.services.FNotificationService
import kotlin.apply
import kotlin.jvm.java

class FBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("title") ?: context.getString(R.string.app_name)
        val content = intent?.getStringExtra("content") ?: ""
        val notifyIndex = NotifyIndex.parseIndex(intent?.getIntExtra("alarmType", 0))
        val fNotificationService = EntryPointAccessors.fromApplication(context.applicationContext, INotificationServiceEntryPoint::class.java).getNotificationService()
        fNotificationService.sendNotify(context, notifyIndex, title, content, FNotificationService.NotifyType.DEFAULT)
    }

    fun setAlarm(context: Context, content: String, afterSec: Int = 1) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val broadcastIntent = Intent(context, FBroadcastReceiver::class.java).apply {
            putExtra("content", content)
            putExtra("alarmType", FNotificationService.NotifyType.DEFAULT.index)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            broadcastIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val triggerTime = System.currentTimeMillis() + 1000 * afterSec
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTime, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }
}