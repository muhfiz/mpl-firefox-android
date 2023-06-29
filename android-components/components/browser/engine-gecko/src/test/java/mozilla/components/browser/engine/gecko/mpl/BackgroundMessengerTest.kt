package mozilla.components.browser.engine.gecko.mpl

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonParser
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.webextension.MessageHandler
import mozilla.components.concept.engine.webextension.Port
import mozilla.components.concept.engine.webextension.WebExtension
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import kotlin.reflect.typeOf

@RunWith(AndroidJUnit4::class)
class BackgroundMessengerTest {

    private val generatedMessageId = 3
    private val message = JSONObject("""
        {
            realMessage: true
        }
    """.trimIndent())

    val bundledRequestMessage = JSONObject("""
        {   
            id: $generatedMessageId,
            request: $message
        }
    """.trimIndent())

    val bundledResponseMessage = JSONObject("""
        {
            id: $generatedMessageId,
            response: $message
        }
    """.trimIndent())
    val requestGeneratedMessageIdMessage = JSONObject("""
        {
            type: ${MessageType.BGtoNV.GENERATE_MESSAGE_ID}
        }
    """.trimIndent())

    @MockK
    private lateinit var webExtensionMock: WebExtension
    @RelaxedMockK
    private lateinit var portMock: Port
    private lateinit var messageHandlerSlot: CapturingSlot<MessageHandler>
    private lateinit var backgroundMessenger: BackgroundMessenger

    @Before
    fun setUp(){
        MockKAnnotations.init(this)

        //return the same id for all the test message
        mockkObject(Message)
        every { Message.generateMessageId() } returns generatedMessageId

        messageHandlerSlot = slot()
        every {
            webExtensionMock.registerBackgroundMessageHandler(
                any(), capture(messageHandlerSlot)
            )
        } just Runs
        backgroundMessenger = BackgroundMessenger(webExtensionMock)
        messageHandlerSlot.captured.onPortConnected(portMock)
    }

    @After
    fun tearDown(){
        unmockkObject(Message)
    }

    @Test
    fun `constructor register background message handler`(){
        val webExtensionMock =  mockk<WebExtension>(relaxed = true)
        BackgroundMessenger(webExtensionMock)

        verify {
            webExtensionMock.registerBackgroundMessageHandler(
                MplBot.MPLBOT_PORT_NAME, ofType()
            )
        }
    }

    @Test
    fun `when one-called message receiving requestGeneratedMessageIdMessage, the answer should be generatedId`(){
        assertEquals(
            messageHandlerSlot.captured.onMessage(requestGeneratedMessageIdMessage, mockk()),
            generatedMessageId
        )
    }

    @Test
    fun `postMessage_should bundle postedMessage when send it through latest connected port`(){
        val onResponseMock = mockk<(JSONObject) -> Unit>(relaxed = true)
        backgroundMessenger.postMessage(message, onResponseMock)
        messageHandlerSlot.captured.onPortMessage(bundledResponseMessage, portMock)
        verify {
            onResponseMock.invoke(withArg {
                assertEquals(
                    JsonParser.parseString(it.toString()),
                    JsonParser.parseString(message.toString())
                )
            })
        }
    }

    @Test
    fun `postMessage_call onResponse if port receiving bundledResponseMessage with the same id as sent bundledRequestMessage`(){
        val onResponseMock = mockk<(response: JSONObject) -> Unit>(relaxed = true)
        backgroundMessenger.postMessage(message, onResponseMock)
        messageHandlerSlot.captured.onPortMessage(bundledResponseMessage, portMock)

        verify {
            onResponseMock.invoke(withArg {
                assertEquals(
                    JsonParser.parseString(it.toString()),
                    JsonParser.parseString(message.toString())
                )
            })
        }
    }

    @Test
    fun `setListener_triggers listener if port receiving bundledRequestMessage, unbundled message passes as listener's parameter`(){

        val listenerMock = mockk<BackgroundMessengerListener>(relaxed = true)
        every {
            webExtensionMock.registerBackgroundMessageHandler(
                any(), capture(messageHandlerSlot)
            )
        } just Runs

        val backgroundMessenger = BackgroundMessenger(webExtensionMock)
        val messageHandler = messageHandlerSlot.captured
        backgroundMessenger.setListener(listenerMock)
        messageHandler.onPortConnected(portMock)
        messageHandler.onPortMessage(bundledRequestMessage, portMock)

        verify {
            listenerMock.invoke(withArg {
                assertEquals(
                    JsonParser.parseString(message.toString()),
                    JsonParser.parseString(it.toString())
                )
            }, any())
        }
    }

    @Test
    fun `setListener_if sendResponse called, passed response bundled and send via Port`(){
        val listener: BackgroundMessengerListener = { message, sendResponse ->
            sendResponse(message)
        }

        val backgroundMessenger = BackgroundMessenger(webExtensionMock)
        backgroundMessenger.setListener(listener)
        val messageHandler = messageHandlerSlot.captured
        messageHandler.onPortConnected(portMock)
        messageHandler.onPortMessage(bundledRequestMessage, portMock)

        verify {
            portMock.postMessage(withArg {
                assertEquals(
                    JsonParser.parseString(it.toString()),
                    JsonParser.parseString(bundledResponseMessage.toString())
                )
            })
        }
    }
}