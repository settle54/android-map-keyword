package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var repository: MapRepository
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: PlacesViewModelFactory
    private lateinit var viewModel: PlacesViewModel
    private lateinit var adapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = MapRepository(this)
        viewModelFactory = PlacesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlacesViewModel::class.java)

        adapter = PlacesAdapter { position: Int ->
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
        }
        binding.placesRView.adapter = adapter
        binding.placesRView.layoutManager = LinearLayoutManager(this)

        viewModel.places.observe(this, Observer { places ->
            adapter.updateList(places)
            if (adapter.itemCount <= 0) {
                binding.textView.visibility =  View.VISIBLE
            } else {
                binding.textView.visibility = View.GONE
            }
        })

        binding.searchInput.addTextChangedListener {text ->
            viewModel.filterPlace(text.toString())
        }
    }
}
