/*
 * LegalHelpApi.kt
 * Module: :core:network   ·   Work package: WP-02 (legal-help slice written with WP-10)
 *
 * Retrofit interface for the "Yordam" tag of docs/api/openapi.yaml: /v1/help, /v1/hotline,
 * /v1/lawyers, /v1/conversations, /v1/ai, /v1/callback-requests, /v1/bookings.
 * Endpoints marked "token" return 401 without a signed-in user.
 */
package uz.asos.core.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.asos.core.network.dto.AiAskRequestDto
import uz.asos.core.network.dto.AiAskResponseDto
import uz.asos.core.network.dto.AiSuggestionsResponseDto
import uz.asos.core.network.dto.BookingRequestDto
import uz.asos.core.network.dto.BookingResponseDto
import uz.asos.core.network.dto.BookingsResponseDto
import uz.asos.core.network.dto.CallbackRequestDto
import uz.asos.core.network.dto.CancelBookingResponseDto
import uz.asos.core.network.dto.CallbackResponseDto
import uz.asos.core.network.dto.ConversationDetailResponseDto
import uz.asos.core.network.dto.ConversationResponseDto
import uz.asos.core.network.dto.ConversationsResponseDto
import uz.asos.core.network.dto.HelpResponseDto
import uz.asos.core.network.dto.HotlineResponseDto
import uz.asos.core.network.dto.LawyersResponseDto
import uz.asos.core.network.dto.MarkReadResponseDto
import uz.asos.core.network.dto.MessagesResponseDto
import uz.asos.core.network.dto.OpenConversationRequestDto
import uz.asos.core.network.dto.SendMessageRequestDto
import uz.asos.core.network.dto.SendMessageResponseDto

interface LegalHelpApi {

    /** Whole Huquqiy yordam screen: quote, AI texts, hotline, lawyers, specialties. Public. */
    @GET("v1/help")
    suspend fun getHelp(): HelpResponseDto

    @GET("v1/hotline")
    suspend fun getHotline(): HotlineResponseDto

    /** Online lawyers first. [online] = "1" for online only; [specialty] e.g. "mehnat". Public. */
    @GET("v1/lawyers")
    suspend fun getLawyers(
        @Query("online") online: String? = null,
        @Query("specialty") specialty: String? = null,
    ): LawyersResponseDto

    /** Opens (or returns the existing) chat with a lawyer. Token. */
    @POST("v1/lawyers/{id}/conversations")
    suspend fun openConversation(
        @Path("id") lawyerId: Int,
        @Body body: OpenConversationRequestDto,
    ): ConversationResponseDto

    /** Token. */
    @GET("v1/conversations")
    suspend fun getConversations(): ConversationsResponseDto

    /** Conversation and its latest messages. Token. */
    @GET("v1/conversations/{id}")
    suspend fun getConversation(
        @Path("id") conversationId: Int,
        @Query("limit") limit: Int = 50,
    ): ConversationDetailResponseDto

    /** Newest first; [before] = message id for the previous page. Token. */
    @GET("v1/conversations/{id}/messages")
    suspend fun getMessages(
        @Path("id") conversationId: Int,
        @Query("before") before: Int? = null,
        @Query("limit") limit: Int = 50,
    ): MessagesResponseDto

    /** Text and/or article links (max 5). Token. */
    @POST("v1/conversations/{id}/messages")
    suspend fun sendMessage(
        @Path("id") conversationId: Int,
        @Body body: SendMessageRequestDto,
    ): SendMessageResponseDto

    /** Token. */
    @POST("v1/conversations/{id}/read")
    suspend fun markRead(@Path("id") conversationId: Int): MarkReadResponseDto

    /** Ready-made questions for the AI card. Public. */
    @GET("v1/ai/suggestions")
    suspend fun getAiSuggestions(): AiSuggestionsResponseDto

    /** Situation text (10–2000 chars) → answer + related articles. Token. */
    @POST("v1/ai/ask")
    suspend fun askAi(@Body body: AiAskRequestDto): AiAskResponseDto

    /** "Menga qo‘ng‘iroq qiling". Works without a token (then [CallbackRequestDto.phone] is needed). */
    @POST("v1/callback-requests")
    suspend fun requestCallback(@Body body: CallbackRequestDto): CallbackResponseDto

    /** Token. */
    @GET("v1/bookings")
    suspend fun getBookings(): BookingsResponseDto

    /** 409 when the slot is taken. Token. */
    @POST("v1/bookings")
    suspend fun createBooking(@Body body: BookingRequestDto): BookingResponseDto

    /** Token. */
    @DELETE("v1/bookings/{id}")
    suspend fun cancelBooking(@Path("id") bookingId: Int): CancelBookingResponseDto
}
