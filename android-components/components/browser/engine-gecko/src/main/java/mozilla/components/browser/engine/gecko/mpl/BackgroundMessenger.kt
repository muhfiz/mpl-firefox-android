package mozilla.components.browser.engine.gecko.mpl

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
            object: MessageHandler{
                override fun onPortConnected(port: Port) {
                    this@BackgroundMessenger.port = port
                }

                override fun onPortMessage(message: Any, port: Port) {
                    if(message !is JSONObject) return
                    val messageId = message.optInt("id", -1)
                    if(messageId == -1) return

                    message.optJSONObject("request")?.let { unbundledMessage ->
                        listener?.invoke(unbundledMessage){response ->
                            this@BackgroundMessenger.port.postMessage(
                                bundleResponseMessage(response, messageId)
                            )
                        }
                    }
                    message.optJSONObject("response")?.let {unbundledMessage ->
                        sentMessageResponses[messageId]?.invoke(unbundledMessage)
                    }
                }
            }
        )
    }

    fun postMessage(message: JSONObject, onResponse: ((response: JSONObject) -> Unit)? = null){
        val messageId = Message.generateMessageId()
        port.postMessage(bundleRequestMessage(message, messageId))
        if(onRsentMessageResponses[messageId] = onResponse
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