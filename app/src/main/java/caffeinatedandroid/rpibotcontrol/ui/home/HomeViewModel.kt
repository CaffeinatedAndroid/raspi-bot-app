package caffeinatedandroid.rpibotcontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val textConnectionStatus = MutableLiveData<String>().apply {
        value = "Not Connected"
    }
}