package me.vitalir.kresume.server.model

import kotlinx.serialization.Serializable

@Serializable
data class Resume(
    val basics: Basics? = null,
    val work: List<WorkEntry>? = null
)

@Serializable
data class Basics(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val summary: String? = null,
    val location: String? = null,
    val title: String? = null,
    val url: String? = null
)

@Serializable
data class WorkEntry(
    val company: String? = null,
    val position: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val description: String? = null,
    val highlights: List<String>? = null
)
