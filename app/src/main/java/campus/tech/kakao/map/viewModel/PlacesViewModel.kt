package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.launch

class PlacesViewModel(private val repository: MapRepository) : ViewModel() {

    private val _places: MutableLiveData<List<Place>> by lazy {
        MutableLiveData<List<Place>>()
    }
    val places: LiveData<List<Place>> = _places

    init {
        _places.postValue(emptyList())
    }

    private fun loadPlaces() {
        viewModelScope.launch() {
            val placesFromRepo = repository.getAllPlaces()
            _places.postValue(placesFromRepo)
        }
    }

    fun insertPlace(name: String, address: String, category: String = "") {
        viewModelScope.launch() {
            repository.insertPlace(name, address, category)
            loadPlaces()
        }
    }

    fun deletePlace(name: String, address: String, category: String = "") {
        viewModelScope.launch() {
            repository.deletePlace(name, address, category)
            loadPlaces()
        }
    }

    fun getAllPlaces(): List<Place> {
        return repository.getAllPlaces()
    }

    fun filterPlace(search: String) {
        viewModelScope.launch() {
            val allPlaces = repository.getAllPlaces()
            val filtered = if (search.isEmpty()) {
                emptyList()
            } else {
                allPlaces.filter { it.name.contains(search, ignoreCase = true) }
            }
            _places.postValue(filtered)
        }
    }
}