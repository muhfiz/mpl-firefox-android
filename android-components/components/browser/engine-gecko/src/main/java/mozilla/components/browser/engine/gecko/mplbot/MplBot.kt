package mozilla.components.browser.engine.gecko.mplbot

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import mozilla.components.concept.engine.webextension.WebExtension
import mozilla.components.concept.engine.webextension.WebExtensionRuntime
import mozilla.components.support.base.log.logger.Logger
import mozilla.components.support.ktx.android.content.PreferencesHolder
import mozilla.components.support.ktx.android.content.booleanPreference
import org.json.JSONObject
import org.mozilla.geckoview.Autocomplete
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSession.PromptDelegate.AutocompleteRequest


object MplBot {
    private val logger = Logger("mplbot_ext")

    private const val MPL_ORIGIN = "https://myprofitland.com"
    private const val MPLBOT_EXTENSION_ID = "mpl_bot@gmail.com"
    private const val MPLBOT_EXTENSION_URL = "resource://android/assets/extensions/mpl/"
    const val MPLBOT_PORT_NAME = "mplbot"

    private var webExtension: WebExtension? = null
        set(value) {
            field = value
            value?.let {
                nvMessenger = NVMessenger(it)
                nvMessenger.setListener(::onMessage)
            }
        }
    private lateinit var loginCredentialStore: LoginCredentialStore
    private lateinit var nvMessenger: NVMessenger

    lateinit var conf: MplConfigurations
        private set

    private lateinit var mainSession: GeckoSession

    private fun installBuiltInMplBotExtension(runtime: WebExtensionRuntime) {
        runtime.installWebExtension(
            MPLBOT_EXTENSION_ID,
            MPLBOT_EXTENSION_URL,
            onSuccess = {
                webExtension = it
                logger.debug("Installed WebCompat webextension: ${it.id}")
            },
            onError = { ext, throwable ->
                logger.error("Failed to install WebCompat webextension: $ext", throwable)
            },
        )
    }

    fun initialize(context: Context, runtime: GeckoRuntime, webExtRuntime: WebExtensionRuntime) {
        loginCredentialStore = LoginCredentialStore(context.applicationContext)
        conf = MplConfigurations(context)
        mainSession = GeckoSession().apply {
            open(runtime)
            loadUri("https://myprofitland.com?index.php#init_mpl_bot")
        }
        installBuiltInMplBotExtension(webExtRuntime)
    }

    /**
     * returns true if handled false otherwise
     */
    fun handleLogin(details: AutocompleteRequest<Autocomplete.LoginSaveOption>): Boolean {
        //only use the first entry
        val detail = details.options[0].value
        if (detail.origin != MPL_ORIGIN) return false

        val currentCredential = loginCredentialStore.getSavedLoginCredential()
        val newCredential = LoginCredential(detail.username, detail.password)
        if (currentCredential != newCredential) {
            loginCredentialStore.saveLoginCredential(newCredential)
            mainSession.reload()
        }
        return true
    }

    private fun onMessage(message: Message, sendResponse: (response: JSONObject) -> Unit) {
        when (message.type) {
            NVMessageType.RETRIEVING_USER_LOGIN_CREDENTIAL.toString() -> {
                val loginCredential = loginCredentialStore.getSavedLoginCredential()
                if (loginCredential != null) {
                    sendResponse(
                        JSONObject().apply {
                            put("email", loginCredential.email)
                            put("password", loginCredential.password)
                        },
                    )
                }
            }
        }
    }
}

class LoginCredentialStore(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    fun getSavedLoginCredential(): LoginCredential? {
        val email = preferences.getString(PREF_EMAIL_KEY, "")!!
        val password = preferences.getString(PREF_PASSWORD_KEY, "")!!
        if (email.isEmpty() || password.isEmpty()) return null
        return LoginCredential(email, password)
    }

    fun saveLoginCredential(credential: LoginCredential) {
        preferences.edit {
            putString(PREF_EMAIL_KEY, credential.email)
            putString(PREF_PASSWORD_KEY, credential.password)
        }
    }

    companion object {
        private const val PREF_NAME = "mplbot"
        private const val PREF_PASSWORD_KEY = "mpl_pass"
        private const val PREF_EMAIL_KEY = "mpl_email"
    }
}

data class LoginCredential(
    val email: String,
    val password: String,
)

class MplConfigurations(context: Context) : PreferencesHolder {
    override val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var autoLogin by booleanPreference(PREF_AUTO_LOGIN_KEY, true)

    companion object {
        private const val PREF_NAME = "mpl_conf"
        private const val PREF_AUTO_LOGIN_KEY = "auto-login"
    }
}
