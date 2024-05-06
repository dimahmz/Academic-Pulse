package com.example.academicpulse.model

data class Publication(
	val id: String,
	val title: String,
	val date: String,
	val reads: Int = 0,
	val uploads: Int = 0,
	val contributors: ArrayList<User> = arrayListOf(),
)
