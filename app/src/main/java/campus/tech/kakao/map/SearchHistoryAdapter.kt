package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.model.RecentSearchWord
import campus.tech.kakao.map.databinding.SearchHistoryModuleBinding

class SearchHistoryAdapter(
    private var recentSearchWordList: List<RecentSearchWord>,
    private val onDeleteClick: (Int) -> Unit,
    private val onTextClick: (Int) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchHistoryModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recentSearchWordList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentSearchWordList[position])
    }

    inner class ViewHolder(private val binding: SearchHistoryModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteHistory.setOnClickListener {
                onDeleteClick(bindingAdapterPosition)
            }
            binding.seachRecord.setOnClickListener {
                onTextClick(bindingAdapterPosition)
            }
        }
        fun bind(sh : RecentSearchWord) {
            binding.data = sh
        }
    }
}