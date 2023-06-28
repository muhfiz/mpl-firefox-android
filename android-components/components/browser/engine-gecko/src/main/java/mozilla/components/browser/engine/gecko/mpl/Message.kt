package mozilla.components.browser.engine.gecko.mpl

object Message {
    private var lastId = 0

    fun generateMessageId(): Int {
        return ++lastId
    }
}