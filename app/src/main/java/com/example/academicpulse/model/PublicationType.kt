package com.example.academicpulse.model

import com.example.academicpulse.utils.useCast

data class PublicationType(val id: String, val label: String) {
	companion object {
		fun fromMap(id: String, map: Map<String, Any?>?): PublicationType {
			return PublicationType(
				id = id,
				label = useCast(map, "label", ""),
			)
		}
	}
}