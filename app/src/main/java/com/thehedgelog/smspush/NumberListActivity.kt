package com.thehedgelog.smspush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thehedgelog.smspush.ui.state.NumberListPageType
import com.thehedgelog.smspush.ui.state.NumberListState
import com.thehedgelog.smspush.ui.theme.SmsPushTheme
import com.thehedgelog.smspush.viewmodel.NumberListViewModel

class NumberListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView()
        }
    }
}

@Composable
fun ContentView(modifier: Modifier = Modifier, viewModel: NumberListViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    Content(modifier = modifier, state = uiState, updateState = viewModel.updateState)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    state: NumberListState,
    updateState: (NumberListState) -> Unit
) {

    val phoneNumbers = state.phoneNumbers
    val currentPage = state.currentPage

    val setCurrentPage = { page: NumberListPageType ->
        updateState(state.copy(currentPage = page))
    }

    SmsPushTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { TopAppBar(title = { Text(text = "SMS") }) }, bottomBar = {
            NavigationBar() {
                NavigationItem(
                    NumberListPageType.SUBSCRIPTION,
                    currentPage,
                    setCurrentPage,
                    R.drawable.round_link_24,
                    "Subscription"
                )
                NavigationItem(
                    NumberListPageType.BROADCAST,
                    currentPage,
                    setCurrentPage,
                    R.drawable.round_cell_tower_24,
                    "Broadcast"
                )
            }
        }, floatingActionButton = {
            when (currentPage) {
                NumberListPageType.SUBSCRIPTION -> ExtendedFloatingActionButton(
                    text = { Text("New Subscription") },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.round_add_link_24),
                            "Create"
                        )
                    },
                    onClick = { /*TODO*/ }
                )

                else -> ExtendedFloatingActionButton(
                    text = { Text("Broadcast") },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.round_add_link_24),
                            "Create"
                        )
                    },
                    onClick = { /*TODO*/ }
                )
            }
        }) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = it.calculateBottomPadding(),
                        top = it.calculateTopPadding()
                    ).padding(bottom = 16.dp)
            ) {
                when (currentPage) {
                    NumberListPageType.SUBSCRIPTION -> SubscriptionView()
                    NumberListPageType.BROADCAST -> BroadcastView()
                }
            }

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RowScope.NavigationItem(
    type: NumberListPageType,
    currentPage: NumberListPageType,
    setCurrentPage: (NumberListPageType) -> Unit,
    @DrawableRes resId: Int,
    name: String
) {
    NavigationBarItem(
        selected = currentPage == type,
        onClick = { setCurrentPage(type) },
        icon = { Icon(ImageVector.vectorResource(id = resId), name) },
        label = { Text(text = name) })
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun GreetingPreview() {
    var state = NumberListState(
        phoneNumbers = listOf("+9779860315019", "+9779843659004"), messages = mapOf(
            "+9779860315019" to listOf("Hello", "How are your"),
            "+9779843659004" to listOf("Nice to see you", "I am fine")
        )
    )


    Content(
        state = state, updateState = { ns -> println("Obtained $ns") }
    )
}