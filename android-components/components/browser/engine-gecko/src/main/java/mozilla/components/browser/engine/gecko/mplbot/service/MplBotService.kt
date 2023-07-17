package mozilla.components.browser.engine.gecko.mplbot.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mozilla.components.browser.engine.gecko.R
import mozilla.components.browser.engine.gecko.mplbot.MplBot
import mozilla.components.browser.engine.gecko.mplbot.mpl.Inventory
import org.mozilla.geckoview.GeckoSession

private val serviceScope: CoroutineScope = MainScope()
class MplBotService: Service(), CoroutineScope by serviceScope {

    private var bgSession: GeckoSession? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_REDELIVER_INTENT
    }

    private suspend fun getBackgroundSession() : GeckoSession {
        return withContext(serviceScope.coroutineContext) {
            bgSession ?: GeckoSession().apply {
                MplBot.initializedRuntime?.let { open(it) }
                bgSession = this
            }
        }
    }

    fun onTaskEnd(){
        bgSession = null
    }

    private fun startForeground() {
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.main_icon)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            builder.foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
        }
        startForeground(NOTIFICATION_ID, builder.build())
        logToServer()
    }

    private fun logToServer(){
        serviceScope.launch(Dispatchers.IO){
            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
            while(true){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getBackgroundSession().loadUri("https://sinyalsaham.id/bugzilla-1799833/testapp.php?log=isDeviceIdleMode = ${powerManager.isDeviceIdleMode}")
                }
                delay(15000)
            }
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object{
        private const val CHANNEL_ID = "mplbot_channel_id"
        private const val NOTIFICATION_ID = 250801
        fun registerNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val descriptionText = context.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}

interface Task {
    fun execute()
}

class ConsumeInventoryItem(
    private val item: Inventory
) : Task{
    override fun execute() {
        item.id
    }

}

class Tasks: Task {
    private val children = mutableListOf<Task>()
    fun add(child: Task){
        children.add(child)
    }
    fun remove(child: Task){
        children.remove(child)

    }
    override fun execute() {
        children.forEach {
            it.execute()
        }
    }
}
