package mozilla.components.browser.engine.gecko.mpl

enum class BGMessageType(private val value: String) {
    REQUEST_USER_LOGIN_CREDENTIAL("bg-request-user-login-credential");

    override fun toString(): String {
        return value
    }
}

enum class UIMessageType(private val value: String) {
    LOGIN("ui-login");
    override fun toString(): String {
        return value
    }
}
enum class NVMessageType(private val value: String) {
    RETRIEVING_USER_LOGIN_CREDENTIAL("nv-retrieve-user-login-credential");
    override fun toString(): String {
        return value
    }
}

data class Message (
    val type: String,
    val data: Any? = null
)


