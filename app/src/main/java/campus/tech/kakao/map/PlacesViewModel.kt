package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlacesViewModel(context: Context) : ViewModel() {
    private val repository: MapRepository = MapRepository(context)

    private val _places: MutableLiveData<List<Place>> by lazy {
        MutableLiveData<List<Place>>()
    }
    val places: LiveData<List<Place>> = _places

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            val placesFromRepo = repository.getAllPlaces()
            _places.postValue(placesFromRepo)
        }
    }

    fun insertPlace(name: String, address: String, category: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPlace(name, address, category)
        }
    }

    fun deletePlace(name: String, address: String, category: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlace(name, address, category)
        }
    }
}