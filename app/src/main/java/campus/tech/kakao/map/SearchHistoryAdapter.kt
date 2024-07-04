package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchHistoryModuleBinding

class SearchHistoryAdapter(
    private var searchHistoryList: List<SearchHistory>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchHistoryModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchHistoryList[position])
    }

    inner class ViewHolder(private val binding: SearchHistoryModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteHistory.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }
        fun bind(sh : SearchHistory) {
            binding.data = sh
        }
    }
}