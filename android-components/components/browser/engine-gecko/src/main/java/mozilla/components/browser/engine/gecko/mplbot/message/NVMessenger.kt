package mozilla.components.browser.engine.gecko.mplbot.message

import mozilla.components.browser.engine.gecko.mplbot.MplBot
import mozilla.components.browser.engine.gecko.mplbot.helper.MessageHelper
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.webextension.MessageHandler
import mozilla.components.concept.engine.webextension.Port
import mozilla.components.concept.engine.webextension.WebExtension
import org.json.JSONObject

typealias NVMessengerListener = (message: Message, sendResponse: (response: JSONObject) -> Unit) -> Unit

class NVMessenger(webExtension: WebExtension) {
    private lateinit var port: Port
    private var listener: NVMessengerListener? = null
    private val sentMessageResponses by lazy { mutableMapOf<Int, (JSONObject) -> Unit>() }
    private val messageHelper by lazy { MessageHelper() }

    init {
        setUpMessageHandler(webExtension)
    }

    private fun setUpMessageHandler(webExtension: WebExtension) {
        webExtension.registerBackgroundMessageHandler(
            MplBot.MPLBOT_PORT_NAME,
            object : MessageHandler {
                override fun onPortConnected(port: Port) {
                    this@NVMessenger.port = port
                }

                override fun onPortMessage(message: Any, port: Port) {
                    if (message !is JSONObject) return
                    val messageId = message.optInt("id", -1)
                    if (messageId == -1) return

                    message.optJSONObject("request")?.let { unbundledMessage ->
                        onRequestMessageReceived(unbundledMessage, messageId)
                    }
                    message.optJSONObject("response")?.let { unbundledMessage ->
                        onResponseMessageReceived(unbundledMessage, messageId)
                    }
                }

                override fun onMessage(message: Any, source: EngineSession?): Any? {
                    return when (message) {
                        COMMAND_GENERATE_MESSAGE_ID -> {
                            generateMessageId()
                        }

                        else -> null
                    }
                }
            },
        )
    }

    fun onRequestMessageReceived(message: JSONObject, messageId: Int) {
        messageHelper.fromJson(message)?.let {
            listener?.invoke(it) { response ->
                this@NVMessenger.port.postMessage(
                    bundleResponseMessage(response, messageId),
                )
            }
        }
    }

    fun onResponseMessageReceived(message: JSONObject, messageId: Int) {
        sentMessageResponses[messageId]?.invoke(message)
        //response only supposed to be called once
        sentMessageResponses.remove(messageId)
    }

    fun postMessage(message: Message, onResponse: ((response: JSONObject) -> Unit)? = null) {
        val messageId = generateMessageId()
        port.postMessage(bundleRequestMessage(message, messageId))
        if (onResponse != null) sentMessageResponses[messageId] = onResponse
    }

    fun setListener(listener: NVMessengerListener) {
        this.listener = listener
    }

    private fun bundleRequestMessage(
        message: Message,
        id: Int = generateMessageId(),
    ): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("request", messageHelper.toJson(message))
        }
    }

    private fun bundleResponseMessage(message: JSONObject, id: Int): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("response", message)
        }
    }

    companion object {
        private var lastID = 0
        private fun generateMessageId(): Int {
            return ++lastID
        }

        private const val COMMAND_GENERATE_MESSAGE_ID = "nvc-generate-message-id"
    }
}