package xyz.pcrab.smaniszk.data

import kotlinx.serialization.Serializable

data class Admin(
    val id: String,
    val username: String,
    val token: String,
)

@Serializable
data class LoginAdminRequest(
    val id: String,
    val password: String,
)