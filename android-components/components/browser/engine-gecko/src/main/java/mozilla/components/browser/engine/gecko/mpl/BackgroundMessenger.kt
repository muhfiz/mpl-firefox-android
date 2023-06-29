package mozilla.components.browser.engine.gecko.mpl

import android.util.Log
import com.google.gson.Gson
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.webextension.MessageHandler
import mozilla.components.concept.engine.webextension.Port
import mozilla.components.concept.engine.webextension.WebExtension
import org.json.JSONObject

typealias BackgroundMessengerListener = (message: JSONObject, sendResponse: (response: JSONObject) -> Unit) -> Unit

class BackgroundMessenger(webExtension: WebExtension) {
    private lateinit var port: Port
    private var listener: BackgroundMessengerListener? = null
    private val sentMessageResponses = mutableMapOf<Int, (JSONObject) -> Unit>()

    init {
        webExtension.registerBackgroundMessageHandler(
            MplBot.MPLBOT_PORT_NAME,
            object : MessageHandler {
                override fun onPortConnected(port: Port) {
                    Log.d("rr333", "onPortConnected: $port")
                    this@BackgroundMessenger.port = port
                }

                override fun onPortMessage(message: Any, port: Port) {
                    Log.d("rr333", "onPortMessage: $message")

                    if (message !is JSONObject) return
                    val messageId = message.optInt("id", -1)
                    if (messageId == -1) return

                    message.optJSONObject("request")?.let { unbundledMessage ->
                        listener?.invoke(unbundledMessage) { response ->
                            this@BackgroundMessenger.port.postMessage(
                                bundleResponseMessage(response, messageId)
                            )
                        }
                    }
                    message.optJSONObject("response")?.let { unbundledMessage ->
                        sentMessageResponses[messageId]?.invoke(unbundledMessage)
                    }
                }

                override fun onMessage(message: Any, source: EngineSession?): Any? {
                    if(message !is JSONObject) return null
                    return when(message.getString("type")){
                        MessageType.BGtoNV.GENERATE_MESSAGE_ID -> { Message.generateMessageId()}
                        else -> null    
                    }
                }
            },
        )
    }

    fun postMessage(message: JSONObject, onResponse: ((response: JSONObject) -> Unit)? = null){
        val messageId = Message.generateMessageId()
        port.postMessage(bundleRequestMessage(message, messageId))
        if(onResponse != null) sentMessageResponses[messageId] = onResponse
    }

    fun setListener(listener: BackgroundMessengerListener){
        this.listener = listener
    }

    private fun bundleRequestMessage(message: JSONObject, id: Int = Message.generateMessageId()): JSONObject{
        return JSONObject().apply {
            put("id", id)
            put("request", message)
        }
    }

    private fun bundleResponseMessage(message: JSONObject, id: Int): JSONObject{
        return JSONObject().apply {
            put("id", id)
            put("response", message)
        }
    }


}