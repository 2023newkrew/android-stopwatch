package com.survivalcoding.stopwatch.presentation.stopwatch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.LabTimeItemBinding
import com.survivalcoding.stopwatch.domain.model.DeltaLabTime
import com.survivalcoding.stopwatch.domain.util.milSecFormat

class LabTimeAdapter(
) :
    ListAdapter<DeltaLabTime, LabTimeAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(val binding: LabTimeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lab_time_item, parent, false)

        return ViewHolder(LabTimeItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val labText = "랩 ${getItem(position).labNumber}"
        holder.binding.tvLab.text = labText
        holder.binding.tvDeltaTime.text = milSecFormat(getItem(position).deltaTime)
        holder.binding.tvNowTime.text = milSecFormat(getItem(position).nowTime)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DeltaLabTime>() {
            override fun areItemsTheSame(oldItem: DeltaLabTime, newItem: DeltaLabTime): Boolean {
                return oldItem.nowTime == newItem.nowTime
            }

            override fun areContentsTheSame(oldItem: DeltaLabTime, newItem: DeltaLabTime): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}