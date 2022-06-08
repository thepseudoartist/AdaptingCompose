package club.cred.adaptingcompose.warmup

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val imageUrls = listOf(
        "https://images.pexels.com/photos/2065650/pexels-photo-2065650.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260",
        "https://images.pexels.com/photos/12194751/pexels-photo-12194751.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
        "https://images.pexels.com/photos/903907/pexels-photo-903907.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/302899/pexels-photo-302899.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/159108/light-lamp-electricity-power-159108.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/383673/pexels-photo-383673.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
    )

    private val texts = listOf(
        "Hello!",
        "Awesome!",
        "Something!",
        "Some very long random text!",
    )

    private val _contentLiveData: MutableLiveData<SnapshotStateList<DataItem>> = MutableLiveData(mutableStateListOf())
    val contentLiveData: LiveData<SnapshotStateList<DataItem>> = _contentLiveData

    fun init() {
        _contentLiveData.postValue(generateData().toMutableStateList())
        triggerUrlChange()
    }

    fun updateListItemAt(index: Int) {
        val currentList = contentLiveData.value

        currentList?.let { list ->
            list[index] = when (list.getOrNull(index)?.size) {

                1 -> list[index].copy(size = 2)

                2 -> list[index].copy(size = 1)

                else -> list[index]
            }
        }

        _contentLiveData.postValue(currentList)
    }

    private fun triggerUrlChange() {
        viewModelScope.launch {
            while (true) {
                delay(3_000)
                val currentList = contentLiveData.value

                currentList?.let { list ->
                    repeat(8) {
                        val index = Random.nextInt(0, list.size)
                        list[index] = list[index].copy(url = imageUrls.random())
                    }
                }

                _contentLiveData.postValue(currentList)
            }
        }
    }

    private fun generateData(): List<DataItem> {
        return buildList {
            repeat(50) {
                val color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat(),
                )

                add(
                    DataItem(
                        id = UUID.randomUUID().toString(),
                        color = color,
                        text = texts.random(),
                        url = imageUrls.random(),
                        size = Random.nextInt(1, 4)
                    )
                )
            }
        }
    }
}
