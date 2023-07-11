package mozilla.components.browser.engine.gecko.mplbot

import mozilla.components.browser.engine.gecko.mplbot.helper.MessageHelper
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*

@RunWith(JUnit4::class)
class MessageHelperTest {

    val messageHelper = MessageHelper()

    @Test
    fun shouldBeAbleToConvertJsonObjectToMessage() {
        val messageType = NVMessageType.RETRIEVING_USER_LOGIN_CREDENTIAL.toString()
        val messageJson = JSONObject().apply {
            put("type", messageType)
        }
        assertEquals(messageHelper.fromJson(messageJson), Message(messageType))
    }
}