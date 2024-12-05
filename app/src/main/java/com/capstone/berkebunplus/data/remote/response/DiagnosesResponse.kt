package com.capstone.berkebunplus.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiagnosesResponse(

	@field:SerializedName("data")
	val data: DataDiagnoses? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DiagnosesItem(

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tumbuhan")
	val tumbuhan: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("penyakit_id")
	val penyakitId: String? = null
)

data class DataDiagnoses(

	@field:SerializedName("diagnoses")
	val diagnoses: List<DiagnosesItem> = emptyList()
)
