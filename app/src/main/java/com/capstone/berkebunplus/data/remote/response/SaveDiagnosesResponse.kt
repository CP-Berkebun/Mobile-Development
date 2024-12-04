package com.capstone.berkebunplus.data.remote.response

import com.google.gson.annotations.SerializedName

data class SaveDiagnosesRequest(
	val diagnosedId: String,
	val imageUrl: String,
	val plant: String,
	val tumbuhan: String,
	val penyakit_id: String,
	val deskripsi: String,
	val treatment: String
)

data class SaveDiagnosesResponse(
	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
