package app.nexopuente.local.transfer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.nexopuente.local.R

class TransferService : Service() {

    companion object {
        const val ACTION_SEND_FILE = "app.nexopuente.local.action.SEND_FILE"
        const val ACTION_SEND_MULTIPLE_FILES = "app.nexopuente.local.action.SEND_MULTIPLE_FILES"
        const val EXTRA_FILE_URI = "app.nexopuente.local.extra.FILE_URI"
        const val EXTRA_FILE_URIS = "app.nexopuente.local.extra.FILE_URIS"
        private const val CHANNEL_ID = "TransferServiceChannel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Iniciando servicio de transferencia...")
        startForeground(NOTIFICATION_ID, notification)

        when (intent?.action) {
            ACTION_SEND_FILE -> {
                val uri = intent.getParcelableExtra<Uri>(EXTRA_FILE_URI)
                if (uri != null) {
                    processSingleFile(uri)
                }
            }
            ACTION_SEND_MULTIPLE_FILES -> {
                val uris = intent.getParcelableArrayListExtra<Uri>(EXTRA_FILE_URIS)
                if (!uris.isNullOrEmpty()) {
                    processMultipleFiles(uris)
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Canal de Transferencia NexoPuente",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    private fun createNotification(message: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("NexoPuente")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .build()
    }

    private fun processSingleFile(uri: Uri) {
        // Lógica de procesamiento para un solo archivo
    }

    private fun processMultipleFiles(uris: ArrayList<Uri>) {
        // Lógica de procesamiento para múltiples archivos
    }
}
