package mozilla.components.browser.engine.gecko.mplbot

import mozilla.components.browser.engine.gecko.mplbot.helper.MessageHelper
import mozilla.components.browser.engine.gecko.mplbot.message.NVMessage
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MessageHelperTest {

    val messageHelper = MessageHelper()

    @Test
    fun shouldBeAbleToConvertJsonObjectToMessage() {
        val messageType = NVMessage.RetrievingUserLoginCredential.TYPE
        val messageJson = JSONObject().apply {
            put("type", messageType)
        }
        val deserializedMessage = messageHelper.fromJson(messageJson)
        assertNotNull(deserializedMessage)
        assertTrue(deserializedMessage!!::class == NVMessage.RetrievingUserLoginCredential::class)
    }

    @Test
    fun messageData() {
        val messageJson = JSONObject().apply {
            put("type", NVMessage.UserLoggedIn.TYPE)
            put("data", JSONObject().apply { put("key", "userKey") })
        }
        assertTrue(NVMessage.UserLoggedIn::class == messageHelper.fromJson(messageJson)!!::class)
    }
}