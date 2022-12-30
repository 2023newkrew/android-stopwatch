package com.survivalcoding.stopwatch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.database.LaptimeRecord

class LaptimeRecordAdapter(val context: Context) :
    ListAdapter<LaptimeRecord, LaptimeRecordAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LaptimeRecord>() {
            // 아이디가 같은가? => 같으면 areContentsTheSame 으로, 다르면 갱신
            override fun areItemsTheSame(oldItem: LaptimeRecord, newItem: LaptimeRecord): Boolean {
                return oldItem.endTime == newItem.endTime
            }

            // 모든 속성이 같은가? => 다르면 갱신
            override fun areContentsTheSame(
                oldItem: LaptimeRecord,
                newItem: LaptimeRecord
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recordIdTextview: TextView
        val elapsedTimeTextView: TextView
        val endTimeTextView: TextView

        init {
            recordIdTextview = view.findViewById(R.id.record_id)
            elapsedTimeTextView = view.findViewById(R.id.elapsed_time)
            endTimeTextView = view.findViewById(R.id.end_time)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.laptime_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.recordIdTextview.text =
            context.getString(R.string.laptime_record_id_format, position + 1)
        viewHolder.elapsedTimeTextView.text = context.getString(
            R.string.laptime_record_format,
            currentList[position].elapsedTime / 6000 % 60,
            currentList[position].elapsedTime / 100 % 60,
            currentList[position].elapsedTime % 100
        )
        viewHolder.endTimeTextView.text = context.getString(
            R.string.laptime_record_format,
            currentList[position].endTime / 6000 % 60,
            currentList[position].endTime / 100 % 60,
            currentList[position].endTime % 100
        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = currentList.size

//    override fun submitList(list: List<LaptimeRecord>?) {
//        println("[디버그 Adapter] current: $currentList  new: $list")
//        super.submitList(list?.let { ArrayList(it) })
//    }
}