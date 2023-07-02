package mozilla.components.browser.engine.gecko.mpl.helper

import com.google.gson.Gson
import mozilla.components.browser.engine.gecko.mpl.Message
import org.json.JSONObject

class MessageHelper {
    private val gson by lazy {Gson()}

    fun fromJson(messageJson: JSONObject) : Message?{
        return fromJson(messageJson.toString())
    }

    fun fromJson(messageJson: String) : Message?{
        return gson.fromJson(messageJson, Message::class.java)
    }

    fun toJson(message: Message) : JSONObject {
        return JSONObject(gson.toJson(message))
    }
}