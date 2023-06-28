package mozilla.components.browser.engine.gecko.mpl

object MessageType {
    object BGtoNV{
        const val GENERATE_MESSAGE_ID = "bg-nv-generate-message-id"
    }
    object NVtoBG{
        const val VERIFY_LOGIN = "nv-verify-login"
        const val ON_LOGIN_PAGE = "nv-on-login-page"
    }
}