package club.cred.adaptingcompose.warmup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import club.cred.adaptingcompose.ui.theme.AdaptingComposeTheme

class WarmUpActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val onListItemClickListener by lazy {
        object : OnListItemClickListener {
            override fun onClick(index: Int) {
                viewModel.updateListItemAt(index)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()

        setContent {
            val data = viewModel.contentLiveData.observeAsState(initial = emptyList())

            AdaptingComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainContent(dataItems = data.value, onListItemClickListener = onListItemClickListener)
                }
            }
        }
    }
}
