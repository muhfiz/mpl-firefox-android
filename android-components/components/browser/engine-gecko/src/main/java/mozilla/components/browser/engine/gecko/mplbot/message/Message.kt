package mozilla.components.browser.engine.gecko.mplbot.message

interface Message {
    val type: String
}
interface Payload<T>{
    val data: T
}

object NVMessage{
    class RetrievingUserLoginCredential: Message {
        override val type: String = TYPE
        companion object {
            const val TYPE = "nv-retrieve-user-login-credential"
        }
    }
    class UserLoggedIn(override val data: Data.UserLoginInfo) : Message, Payload<Data.UserLoginInfo> {
        override val type: String = TYPE
        companion object {
            const val TYPE = "nv-user-logged-in"
        }
    }

    class WrongCredentialInputted: Message {
        override val type: String = TYPE
        companion object {
            const val TYPE = "nv-user-input-wrong-credential"
        }
    }
}

object UIMessage {
    class Login: Message{
        override val type = TYPE
        companion object {
            const val TYPE = "ui-login"
        }
    }
}

object Data {
    data class UserLoginInfo(
        val key: String
    )
}

