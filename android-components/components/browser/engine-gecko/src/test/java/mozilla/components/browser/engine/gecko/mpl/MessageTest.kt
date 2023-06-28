package mozilla.components.browser.engine.gecko.mpl

import org.junit.Assert
import org.junit.Test

class MessageTest {

    @Test
    fun `generateMessageId return increment id from the lastId`(){
        val firstId = Message.generateMessageId()
        val secondId = Message.generateMessageId()
        val thirdId = Message.generateMessageId()
        Assert.assertEquals(firstId, secondId - 1)
        Assert.assertEquals(secondId, thirdId - 1)
    }
}