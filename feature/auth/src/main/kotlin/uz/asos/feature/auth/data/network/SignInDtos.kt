package uz.asos.feature.auth.data.network

import kotlinx.serialization.Serializable

@Serializable
internal data class RequestCodeBodyDto(
    val email: String,
    val purpose: String,
)

@Serializable
internal data class RequestCodeResponseDto(
    val ok: Boolean = true,
    val email: String = "",
    val expiresIn: Int = 0,
    val resendIn: Int = 0,
    val devCode: String? = null,
)

@Serializable
internal data class VerifyCodeBodyDto(
    val email: String,
    val code: String,
    val purpose: String,
)

@Serializable
internal data class OAuthBodyDto(
    val provider: String,
    val subject: String,
    val email: String? = null,
    val name: String? = null,
)

@Serializable
internal data class TokenDto(
    val accessToken: String = "",
    val expiresAt: String? = null,
    val tokenType: String = "Bearer",
)

@Serializable
internal data class AuthUserDto(
    val id: Int = 0,
    val name: String = "",
    val initials: String = "",
)

@Serializable
internal data class AuthResponseDto(
    val ok: Boolean = true,
    val token: TokenDto = TokenDto(),
    val user: AuthUserDto? = null,
    val isNew: Boolean = false,
)

@Serializable
internal data class UpdateMeBodyDto(
    val name: String? = null,
)

@Serializable
internal data class MeResponseDto(
    val ok: Boolean = true,
    val user: AuthUserDto? = null,
)
