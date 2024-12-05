package com.capstone.berkebunplus.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.data.remote.response.DiagnosesItem
import com.capstone.berkebunplus.databinding.ItemsResultScanBinding
import com.capstone.berkebunplus.ui.detail.DetailDiagnosisActivity

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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailDiagnosisActivity::class.java).apply {
                putExtra(IMAGE_EXTRA, getDiagnosesSaved.imageUrl)
                putExtra(DIAGNOSES_ID_EXTRA, getDiagnosesSaved.id)
                putExtra(TUMBUHAN_EXTRA, getDiagnosesSaved.tumbuhan)
                putExtra(DISEASE_ID_EXTRA, getDiagnosesSaved.penyakitId)
                putExtra(DESCRIPTION_EXTRA, getDiagnosesSaved.deskripsi)
                putExtra(TREATMENT_EXTRA, getDiagnosesSaved.treatment)
            }
            holder.itemView.context.startActivity(intent)
        }
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

            const val USER_ID_EXTRA = "userId_extra"
            const val DIAGNOSES_ID_EXTRA = "diagnosesId_extra"
            const val IMAGE_EXTRA = "image_extra"
            const val PLANT_EXTRA = "plant_extra"
            const val TUMBUHAN_EXTRA = "tumbuhan_extra"
            const val DISEASE_ID_EXTRA = "disease_extra"
            const val DESCRIPTION_EXTRA = "description_extra"
            const val TREATMENT_EXTRA = "treatment_extra"

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