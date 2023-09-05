package com.thehedgelog.smspush

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.auth.FirebaseAuth
import com.thehedgelog.smspush.ui.state.BroadcastState
import com.thehedgelog.smspush.viewmodel.BroadcastViewModel


@Composable
fun BroadcastView(viewModel: BroadcastViewModel = viewModel()) {

    val state by viewModel.uiState.collectAsState()
    Broadcast(state, updateBroadcast = viewModel.toggleBroadcast)

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Broadcast(state: BroadcastState, updateBroadcast: (Context, Boolean) -> Unit) {
    val isBroadcasting = state.isBroadcasting
    val context = LocalContext.current

    if (!isBroadcasting) {
        val queryConfirmation = remember { mutableStateOf(false) }
        val queryAuthentication = remember { mutableStateOf(false) }

        val signInLauncher = rememberLauncherForActivityResult(
            contract = FirebaseAuthUIActivityResultContract(),
            onResult = { res ->
                if (res.resultCode == RESULT_OK) {
                    updateBroadcast(context, true)
                }
            })

        val receiveSmsPermission = (context as? Activity)?.run {
            rememberMultiplePermissionsState(
                listOf(
                    "android.permission.RECEIVE_SMS",
                    "android.permission.READ_SMS"
                )
            )
        }

        if (queryConfirmation.value && !queryAuthentication.value && receiveSmsPermission?.allPermissionsGranted == true) {
            queryConfirmation.value = false
            queryAuthentication.value = true
        }

        val dismissRequest = {
            queryAuthentication.value = false
            queryConfirmation.value = false
        }

        val confirmRequest: () -> Unit = {
            if (receiveSmsPermission?.allPermissionsGranted == true) {
                queryConfirmation.value = false
                queryAuthentication.value = true
            } else {
                (context as? Activity)?.run {
                    receiveSmsPermission?.launchMultiplePermissionRequest()
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.round_portable_wifi_off_24),
                contentDescription = "Not Broadcasting",
                colorFilter = ColorFilter.tint(Color.Gray)
            )
            Text("Not Broadcasting", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { queryConfirmation.value = true }) {
                Text(text = "Start Broadcasting")
            }

            if (queryConfirmation.value) {
                AlertDialog(onDismissRequest = dismissRequest,
                    title = { Text("Confirm") },
                    text = { Text("You will be broadcasting all SMS received in this phone number to a remote server") },
                    confirmButton = { Button(onClick = confirmRequest) { Text("Confirm") } },
                    dismissButton = { TextButton(onClick = dismissRequest) { Text("Cancel") } })
            }

            if (queryAuthentication.value) {
                if (FirebaseAuth.getInstance().currentUser == null) {
                    val providers = listOf(
                        AuthUI.IdpConfig.PhoneBuilder().setDefaultNumber("+9779843659004").build()
                    )
                    val signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()

                    signInLauncher.launch(signInIntent)
                    queryAuthentication.value = false
                } else {
                    updateBroadcast(context, true)
                }
            }

        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.round_cell_tower_24),
                contentDescription = "Broadcasting",
            )
            Text("Broadcasting")
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                updateBroadcast(context, false)
            }) {
                Text(text = "Stop Broadcasting")
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun BroadcastPreview() {
    Box(Modifier.fillMaxHeight()) {
        Broadcast(state = BroadcastState(false)) {c, b -> }
    }
}