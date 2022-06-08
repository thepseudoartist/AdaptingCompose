package club.cred.adaptingcompose.warmup

import androidx.compose.ui.graphics.Color

data class DataItem(
    val id: String,
    val color: Color,
    val text: String,
    val url: String,
    val size: Int = 1,
)
