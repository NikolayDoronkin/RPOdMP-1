package app.com.feedcat.ui.results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.feedcat.R
import app.com.feedcat.data.entity.GameResult

class ResultListAdapter : ListAdapter<GameResult, ResultListAdapter.ResultViewHolder>(RESULTS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val satietyView: TextView = itemView.findViewById(R.id.satietyView)
        private val dateView: TextView = itemView.findViewById(R.id.dateView)

        fun bind(gameResult: GameResult?) {
            gameResult?.apply {
                satietyView.text = itemView.context.getString(R.string.text_satiety, satiety)
                dateView.text = datetime
            }
        }

        companion object {
            fun create(parent: ViewGroup): ResultViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ResultViewHolder(view)
            }
        }
    }

    companion object {
        private val RESULTS_COMPARATOR = object : DiffUtil.ItemCallback<GameResult>() {
            override fun areItemsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
                return oldItem.satiety == newItem.satiety
            }
        }
    }
}
