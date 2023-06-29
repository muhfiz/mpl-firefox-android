package org.mozilla.fenix.mpl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.mozilla.fenix.theme.FirefoxTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MplBotFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            FirefoxTheme {
                MplBot(
                    findNavController(),
                )
            }
        }
    }
}

@Composable
fun MplBot(
    navController: NavController,
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "MplBot Controller") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        "Back",
                    )
                }
            },
        )
        Content(
            Modifier.fillMaxHeight(),
        )
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        item {
            Row(Modifier.fillMaxWidth()) {

                val mplVM: MplBotViewModel = viewModel()
                val mplConf by mplVM.mplConfState.collectAsState()

                Text(text = "Auto Login")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Demo with icon" },
                    checked = mplConf.autoLogin,
                    onCheckedChange = { mplVM.setAutoLogin(it) },
                )

            }
        }
    }
}
