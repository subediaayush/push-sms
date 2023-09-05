package com.thehedgelog.smspush.ui.compose

import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thehedgelog.smspush.ui.state.ReadStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPhoneItem(number: String, lastMessage: String, status: ReadStatus, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formatted = PhoneNumberUtils.formatNumber(number, "NP")
            Text(text = formatted, fontWeight = FontWeight.Bold)
            Text(text = lastMessage, fontWeight = if (status == ReadStatus.UNREAD) FontWeight.Bold else FontWeight.Normal)
        }
    }
}
