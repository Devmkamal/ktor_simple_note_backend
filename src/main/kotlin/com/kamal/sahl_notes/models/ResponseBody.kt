package com.kamal.sahl_notes.models

import kotlinx.serialization.Serializable


@Serializable
data class ResponseBody<T>(val status : Boolean, val data : T?) {
}