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

// 효율성을 위해 DiffUtil 사용 (갱신 필요한 것만 갱신)
val diffUtil = object : DiffUtil.ItemCallback<LaptimeRecord>() {
    // 아이디가 같은가? => 같으면 areContentsTheSame 으로, 다르면 갱신
    override fun areItemsTheSame(oldItem: LaptimeRecord, newItem: LaptimeRecord): Boolean {
        return oldItem.rid == newItem.rid
    }

    // 모든 속성이 같은가? => 다르면 갱신
    override fun areContentsTheSame(oldItem: LaptimeRecord, newItem: LaptimeRecord): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

class LaptimeRecordAdapter(
    private val dataSet: ArrayList<LaptimeRecord>,
    private val context: Context
) :
    ListAdapter<LaptimeRecord, LaptimeRecordAdapter.ViewHolder>(diffUtil) {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recordIdTextview: TextView
        val elapsedTimeTextView: TextView
        val endTimeTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            recordIdTextview = view.findViewById(R.id.record_id)
            elapsedTimeTextView = view.findViewById(R.id.elapsed_time)
            endTimeTextView = view.findViewById(R.id.end_time)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.laptime_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recordIdTextview.text =
            context.getString(R.string.laptime_record_id_format, dataSet[position].rid)
        viewHolder.elapsedTimeTextView.text = context.getString(
            R.string.laptime_record_format,
            dataSet[position].elapsedTime / 6000 % 60,
            dataSet[position].elapsedTime / 100 % 60,
            dataSet[position].elapsedTime % 100
        )
        viewHolder.endTimeTextView.text = context.getString(
            R.string.laptime_record_format,
            dataSet[position].endTime / 6000 % 60,
            dataSet[position].endTime / 100 % 60,
            dataSet[position].endTime % 100
        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}