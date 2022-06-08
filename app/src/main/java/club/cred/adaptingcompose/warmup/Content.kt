package club.cred.adaptingcompose.warmup

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    dataItems: List<DataItem>,
    onListItemClickListener: OnListItemClickListener? = null,
) {

    Box(modifier = modifier) {
        CollectionItem(dataItems = dataItems, onListItemClickListener = onListItemClickListener)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollectionItem(dataItems: List<DataItem>, onListItemClickListener: OnListItemClickListener?) {
    val lazyListState = rememberLazyListState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        cells = GridCells.Fixed(4),
        state = lazyListState,
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (dataItems.isNotEmpty()) {
            item(span = { GridItemSpan(4) }) {
                Text(
                    text = "Tiles",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 48.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            itemsIndexed(
                items = dataItems,
                spans = { _, item -> GridItemSpan(item.size) },
            ) { index, item ->
                ListItem(
                    data = item,
                    index = index,
                    onListItemClickListener = onListItemClickListener,
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    data: DataItem,
    index: Int,
    onListItemClickListener: OnListItemClickListener?,
) {
    Box(modifier = modifier
        .height(160.dp)
        .clickable(enabled = true) {
            onListItemClickListener?.onClick(index)
        }
    ) {

        AnimatedContent(
            targetState = data.url,
            transitionSpec = {
                val transform = slideInVertically { fullHeight -> fullHeight } + fadeIn() with
                                slideOutVertically { fullHeight -> -fullHeight } + fadeOut()

                transform.using(SizeTransform())
            }
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = data.url,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(Brush.verticalGradient(listOf(Color.Transparent, data.color)), alpha = 0.5f)
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "email",
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    text = data.text,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
