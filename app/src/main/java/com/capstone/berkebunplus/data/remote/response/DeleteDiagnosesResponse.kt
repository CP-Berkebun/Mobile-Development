package com.capstone.berkebunplus.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteDiagnosesResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
