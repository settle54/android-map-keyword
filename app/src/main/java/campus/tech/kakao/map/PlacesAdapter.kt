package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.PlacemoduleBinding

class PlacesAdapter(
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var localList: List<Place> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlacemoduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(localList[position])
    }

    override fun getItemCount(): Int {
        return localList.size
    }

    fun updateList(newList: List<Place>) {
        localList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: PlacemoduleBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }

        fun bind(place: Place) {
            binding.data = place
        }

    }

}