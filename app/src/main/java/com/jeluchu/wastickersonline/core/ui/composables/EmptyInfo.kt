package com.jeluchu.wastickersonline.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ktx.compose.toStringRes
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.ui.theme.darkness

@Composable
fun EmptyInfo() = Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.emptyInfoTitle.toStringRes(),
            textAlign =  TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = darkness
        )
        Text(
            text = R.string.emptyInfoDescription.toStringRes(),
            color = darkness.copy(.6f),
            textAlign =  TextAlign.Center,
            lineHeight = 20.sp,
            fontSize = 16.sp
        )
    }
}