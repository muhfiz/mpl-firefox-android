package mozilla.components.browser.engine.gecko.mplbot.helper

import com.google.gson.Gson
import mozilla.components.browser.engine.gecko.mplbot.message.Message
import mozilla.components.browser.engine.gecko.mplbot.message.NVMessage
import org.json.JSONObject

class MessageHelper {
    private val gson by lazy { Gson() }

    fun fromJson(messageJson: JSONObject): Message? {
        val message = when(messageJson.getString("type")){
            NVMessage.UserLoggedIn.TYPE -> {
                NVMessage.UserLoggedIn(getPayload(messageJson))
            }
            NVMessage.RetrievingUserLoginCredential.TYPE -> NVMessage.RetrievingUserLoginCredential()
            NVMessage.WrongCredentialInputted.TYPE -> NVMessage.WrongCredentialInputted()
            else -> null
        }
        return message
    }

    private inline fun <reified T>getPayload(message: JSONObject): T {
        return gson.fromJson(message.getJSONObject("data").toString(), T::class.java)
    }

    fun toJson(message: Message): JSONObject {
        return JSONObject(gson.toJson(message))
    }
}