package campus.tech.kakao.map

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.model.RecentSearchWord
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.viewModel.MapRepository
import campus.tech.kakao.map.viewModel.PlacesViewModel
import campus.tech.kakao.map.viewModel.PlacesViewModelFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var repository: MapRepository
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PlacesViewModel
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var prefs: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var stringPrefs: String? = null
    private var searchHistoryList = ArrayList<RecentSearchWord>()
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPrefs()
        setUpSearchHistoryAdapter()
        setUpPlacesAdapter()

        repository = MapRepository(this)
        val viewModelFactory = PlacesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlacesViewModel::class.java)

        setUpViewModelObservers()

        binding.searchInput.addTextChangedListener { text ->
            viewModel.filterPlace(text.toString())
        }

        binding.deleteInput.setOnClickListener {
            binding.searchInput.text.clear()
        }

        updateSearchHistoryVisibility()
    }

    private fun setUpSearchHistoryAdapter() {
        searchHistoryAdapter = SearchHistoryAdapter(
            searchHistoryList,
            onDeleteClick = { position: Int ->
                delSearch(position)
                updateSearchHistoryVisibility()
            },
            onTextClick = { position: Int ->
                val itemName = searchHistoryAdapter.getItemName(position)
                binding.searchInput.setText(itemName)
            })
        binding.searchHistory.adapter = searchHistoryAdapter
    }

    private fun setUpPlacesAdapter() {
        placesAdapter = PlacesAdapter { position: Int ->
            val itemName = placesAdapter.getItemName(position)
            insertSearch(position, itemName)
            binding.searchHistory.visibility = View.VISIBLE
        }
        binding.placesRView.adapter = placesAdapter
        binding.placesRView.layoutManager = LinearLayoutManager(this)
    }

    private fun setUpViewModelObservers() {
        viewModel.places.observe(this, Observer { places ->
            placesAdapter.updateList(places)
            placesAdapter.notifyDataSetChanged()
            binding.textView.visibility =
                if (placesAdapter.itemCount <= 0) View.VISIBLE else View.GONE
        })
    }

    private fun updateSearchHistoryVisibility() {
        binding.searchHistory.isVisible = searchHistoryList.isNotEmpty()
    }

    private fun searchHistoryContains(itemName: String): Int {
        return searchHistoryList.indexOfFirst { it.word == itemName }
    }

    private fun moveSearchToLast(foundIdx: Int, itemName: String) {
        searchHistoryList.removeAt(foundIdx)
        searchHistoryList.add(RecentSearchWord(itemName))
        searchHistoryAdapter.notifyItemMoved(foundIdx, searchHistoryList.size - 1)
    }

    private fun insertSearch(position: Int, itemName: String) {
        val foundIdx = searchHistoryContains(itemName)
        if (foundIdx != -1) {
            moveSearchToLast(foundIdx, itemName)
        } else {
            searchHistoryList.add(RecentSearchWord(itemName))
            searchHistoryAdapter.notifyItemInserted(searchHistoryList.size)
        }
        savePrefs()
    }

    private fun delSearch(position: Int) {
        searchHistoryList.removeAt(position)
        searchHistoryAdapter.notifyItemRemoved(position)
        savePrefs()
    }

    private fun setPrefs() {
        prefs = getSharedPreferences("app_data", MODE_PRIVATE)
        prefEditor = prefs.edit()
        stringPrefs = prefs.getString("search_history", null)

        if (stringPrefs != null && stringPrefs != "[]") {
            searchHistoryList = GsonBuilder().create().fromJson(
                stringPrefs, object : TypeToken<ArrayList<RecentSearchWord>>() {}.type
            )
        }
    }

    private fun savePrefs() {
        stringPrefs = GsonBuilder().create().toJson(
            searchHistoryList, object : TypeToken<ArrayList<RecentSearchWord>>() {}.type
        )
        prefEditor.putString("search_history", stringPrefs)
        prefEditor.apply()
    }
}
