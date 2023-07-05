package org.mozilla.fenix.mpl

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mozilla.fenix.ext.settings
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.MatcherHelper.itemWithResId
import org.mozilla.fenix.helpers.MatcherHelper.itemWithText
import org.mozilla.fenix.helpers.TestHelper.packageName
import org.mozilla.fenix.ui.robots.clickPageObject
import org.mozilla.fenix.ui.robots.navigationToolbar
import org.mozilla.fenix.ui.robots.setPageObjectText

@RunWith(AndroidJUnit4::class)
class MplBotTest {

    private lateinit var mDevice: UiDevice

    @get:Rule
    val activityTestRule = HomeActivityTestRule.withDefaultSettingsOverrides()

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext.settings()
            .shouldShowJumpBackInCFR = false
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun autoLoginWhenUserAlreadyLoginBefore(){
        val email = "muhamadhafiz2508@gmail.com"
        val password = "Xisme#2020"
        navigationToolbar {
        }.enterURLAndEnterToBrowser(Uri.parse("https://myprofitland.com/")){
            val loginForm = itemWithResId("form_login")
            val loginButton = itemWithText("Login")
            setPageObjectText(loginForm.getChild(UiSelector().focusable(true).index(0)), email)
            setPageObjectText(loginForm.getChild(UiSelector().focusable(true).index(1)), password)
            clickPageObject(loginButton)
            mDevice.waitForIdle()
            loginForm.waitUntilGone(60000)
            waitForPageToLoad()

            navigationToolbar {
            }.enterURLAndEnterToBrowser(Uri.parse("https://myprofitland.com/logout.php")){
                waitForPageToLoad()
                if(!itemWithText("muhfiz").waitForExists(15000)){
                    throw Exception("Auto login not happening")
                }
            }
        }
    }
}
