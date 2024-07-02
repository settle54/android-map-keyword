package campus.tech.kakao.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PlacesViewModel(private val repository: MapRepository) : ViewModel() {

    private val _places: MutableLiveData<List<Place>> by lazy {
        MutableLiveData<List<Place>>()
    }
    val places: LiveData<List<Place>> = _places

    init {
        loadPlaces()
    }

    private val viewModelScope = MainScope()

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