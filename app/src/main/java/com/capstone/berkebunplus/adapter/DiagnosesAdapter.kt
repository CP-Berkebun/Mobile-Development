package com.capstone.berkebunplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.data.remote.response.DiagnosesItem
import com.capstone.berkebunplus.databinding.ItemsResultScanBinding

class DiagnosesAdapter : ListAdapter<DiagnosesItem, DiagnosesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemsResultScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val getDiagnosesSaved = getItem(position)
        holder.bind(getDiagnosesSaved)

    }

    class MyViewHolder(val binding: ItemsResultScanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diagnosesItem: DiagnosesItem) {
            Glide.with(binding.ivDiagnosisItem.context)
                .load(diagnosesItem.imageUrl)
                .into(binding.ivDiagnosisItem)

            binding.tvDiagnosisPlantNameItem.text = diagnosesItem.tumbuhan
            binding.tvDiagnosisIdItem.text = diagnosesItem.penyakitId
            binding.tvDiagnosisDescriptionItem.text = diagnosesItem.deskripsi
        }
    }

    companion object {

//        const val IMAGE_STORY = "IMAGE_STORY"
//        const val TITLE_STORY = "TITLE_STORY"
//        const val DESC_STORY = "DESC_STORY"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiagnosesItem>() {
            override fun areItemsTheSame(oldItem: DiagnosesItem, newItem: DiagnosesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DiagnosesItem, newItem: DiagnosesItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}