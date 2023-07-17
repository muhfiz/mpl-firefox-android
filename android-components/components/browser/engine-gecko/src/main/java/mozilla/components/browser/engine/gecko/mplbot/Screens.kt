package mozilla.components.browser.engine.gecko.mplbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import mozilla.components.browser.engine.gecko.R
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun MplBotScreen(
    onLoadPage: (url: String) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painterResource(id = R.drawable.main_icon),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Waiting for you to login.")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Go to login page",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    onLoadPage(MplBot.MPL_ORIGIN)
                },
            )
        }
    }
}

