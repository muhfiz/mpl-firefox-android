/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.mpl

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mozilla.components.browser.engine.gecko.mplbot.MplBot

data class MplConfigurationsState(
    val autoLogin: Boolean = true
)

class MplBotViewModel: ViewModel() {

    private val _mplConfState = MutableStateFlow(
        MplConfigurationsState(MplBot.conf.autoLogin)
    )
    val mplConfState = _mplConfState.asStateFlow()

    fun setAutoLogin(newValue: Boolean){
        MplBot.conf.autoLogin = newValue
        _mplConfState.update {
            it.copy(autoLogin = newValue)
        }
    }
}
