package mozilla.components.browser.engine.gecko.mplbot

import com.google.gson.JsonParser
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import mozilla.components.browser.engine.gecko.mplbot.helper.MessageHelper
import mozilla.components.browser.engine.gecko.mplbot.message.NVMessage
import mozilla.components.browser.engine.gecko.mplbot.message.NVMessenger
import mozilla.components.browser.engine.gecko.mplbot.message.NVMessengerListener
import mozilla.components.browser.engine.gecko.mplbot.message.UIMessage
import mozilla.components.concept.engine.webextension.MessageHandler
import mozilla.components.concept.engine.webextension.Port
import mozilla.components.concept.engine.webextension.WebExtension
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class NVMessengerTest {

    @MockK
    lateinit var webExtensionMock: WebExtension

    @RelaxedMockK
    lateinit var portMock: Port
    lateinit var messageHandlerSlot: CapturingSlot<MessageHandler>
    lateinit var nvMessenger: NVMessenger

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        messageHandlerSlot = slot()
        every {
            webExtensionMock.registerBackgroundMessageHandler(
                any(), capture(messageHandlerSlot),
            )
        } just Runs
        nvMessenger = NVMessenger(webExtensionMock)
        messageHandlerSlot.captured.onPortConnected(portMock)
    }

    @Test
    fun postMessage() {
        val exampleMessage =
            UIMessage.Login()
        nvMessenger.postMessage(exampleMessage)
        verify {
            portMock.postMessage(
                withArg {
                    try {
                        it.getInt("id")
                    } catch (e: Exception) {
                        fail("Expected no exception, but an exception was thrown: " + e.message)
                    }
                    assertEquals(
                        JsonParser.parseString(it.optJSONObject("request")?.toString()),
                        JsonParser.parseString(MessageHelper().toJson(exampleMessage).toString()),
                    )
                },
            )
        }
    }

    @Test
    fun setListener() {
        val listenerMock = mockk<NVMessengerListener>(relaxed = true)
        val messageId = 3
        val message = NVMessage.RetrievingUserLoginCredential()
        val jsonMessage = JSONObject().apply {
            put("id", messageId)
            put(
                "request",
                MessageHelper().toJson(message),
            )
        }
        val jsonResponse = JSONObject().apply { put("response", "myResponse") }
        val sendResponseSlot = slot<(JSONObject) -> Unit>()
        every {
            listenerMock.invoke(
                any(), capture(sendResponseSlot),
            )
        } just Runs

        nvMessenger.setListener(listenerMock)
        messageHandlerSlot.captured.onPortMessage(jsonMessage, portMock)
        verify {
            listenerMock.invoke(
                withArg {
                    assertTrue(it::class == message::class)
                },
                any(),
            )
        }
        sendResponseSlot.captured(jsonResponse)
        verify {
            portMock.postMessage(
                withArg {
                    try {
                        it.getInt("id")
                    } catch (e: Exception) {
                        fail("Expected no exception, but an exception was thrown: " + e.message)
                    }
                    assertEquals(
                        JsonParser.parseString(it.optJSONObject("response")?.toString()),
                        JsonParser.parseString(jsonResponse.toString()),
                    )
                },
            )
        }
    }
}