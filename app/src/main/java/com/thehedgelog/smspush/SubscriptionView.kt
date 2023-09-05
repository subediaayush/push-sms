package com.thehedgelog.smspush

import android.os.Build
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.internal.zzx
import com.thehedgelog.smspush.SmsApplication.Companion.TAG
import com.thehedgelog.smspush.model.LocalSmsMessageValue
import com.thehedgelog.smspush.ui.compose.ListPhoneItem
import com.thehedgelog.smspush.ui.state.ReadStatus
import com.thehedgelog.smspush.ui.state.SubscriptionState
import com.thehedgelog.smspush.viewmodel.SubscriptionViewModel
import java.time.Duration

@Composable
fun SubscriptionView(viewModel: SubscriptionViewModel = viewModel()) {

    val state by viewModel.uiState.collectAsState()
    val user by viewModel.user.collectAsState(null)

    Subscription(state = state, isLoggedIn = user != null, updateState = viewModel.updateState)

}

@Composable
fun Subscription(
    state: SubscriptionState,
    isLoggedIn: Boolean,
    updateState: (SubscriptionState) -> Unit
) {
    if (!isLoggedIn) {
        val signInLauncher = rememberLauncherForActivityResult(
            contract = FirebaseAuthUIActivityResultContract(),
            onResult = {  }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.round_cell_tower_24),
                contentDescription = "Not Logged In",
            )
            Text("Login to receive sms")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                val providers = listOf(
                    AuthUI.IdpConfig.PhoneBuilder().setDefaultNumber("+9779843659004").build()
                )
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()

                signInLauncher.launch(signInIntent)
            }) {
                Text(text = "Login")
            }
        }
    } else {
        val subscriptions = state.messages
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Log.i(TAG, "Received items $subscriptions")
            itemsIndexed(subscriptions) { index: Int, item: LocalSmsMessageValue ->
                ListPhoneItem(item.from, item.message, ReadStatus.READ) {
//                    if (item.status == ReadStatus.UNREAD) {
//                        subscriptions[index] = item.copy(status = ReadStatus.READ)
//                        updateState(state.copy(subscriptions = subscriptions))
//                    }
                    showAllMessages(item)
                }
            }
        }
    }
}

fun showAllMessages(item: LocalSmsMessageValue) {
    TODO("Not yet implemented")
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun SubscriptionPreview() {
    Box(Modifier.fillMaxHeight()) {
        Subscription(
            SubscriptionState(
                messages = listOf(
                    LocalSmsMessageValue(
                        System.currentTimeMillis() - Duration.ofDays(1).toMillis(),
                        "+9779860315019",
                        "Hello, how are you",
                    ),
                    LocalSmsMessageValue(
                        System.currentTimeMillis() - Duration.ofDays(1).toMillis(),
                        "+9779843659004",
                        "Hi, I am fine",
                    )
                )
            ), true, {}
        )
    }
}
