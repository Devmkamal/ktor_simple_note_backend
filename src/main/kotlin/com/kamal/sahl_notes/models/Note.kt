package com.kamal.sahl_notes.models

import kotlinx.serialization.Serializable


@Serializable
data class Note(val id : String, val text: String, val userId: String, val createdAt : Long)


