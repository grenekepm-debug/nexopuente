package app.nexopuente.local.transfer

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

class TransferManager(private val context: Context) {

    companion object {
        const val PORT = 8888
    }

    suspend fun sendFileOverNetwork(serverIp: String, fileUri: Uri) = withContext(Dispatchers.IO) {
        try {
            val socket = Socket(serverIp, PORT)
            val outputStream = socket.getOutputStream()
            val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun startReceiverServer(onFileReceived: (File) -> Unit) = withContext(Dispatchers.IO) {
        try {
            val serverSocket = ServerSocket(PORT)
            while (true) {
                val clientSocket = serverSocket.accept()
                val downloadsDir = File(context.getExternalFilesDir(null), "Download")
                if (!downloadsDir.exists()) downloadsDir.mkdirs()

                val receivedFile = File(downloadsDir, "Nexo_${System.currentTimeMillis()}.bin")
                val inputStream = clientSocket.getInputStream()
                val outputStream = FileOutputStream(receivedFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                clientSocket.close()
                onFileReceived(receivedFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
