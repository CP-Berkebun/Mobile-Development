package com.capstone.berkebunplus.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Diagnoses(

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("tumbuhan")
	val tumbuhan: String? = null,

	@field:SerializedName("penyakit_id")
	val penyakitId: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("diagnosedId")
	val diagnosedId: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("diagnoses")
	val diagnoses: Diagnoses? = null
)
