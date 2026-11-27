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

    @GET("v1/help")
    suspend fun getHelp(): HelpResponseDto

    @GET("v1/hotline")
    suspend fun getHotline(): HotlineResponseDto

    @GET("v1/lawyers")
    suspend fun getLawyers(
        @Query("online") online: String? = null,
        @Query("specialty") specialty: String? = null,
    ): LawyersResponseDto

    @POST("v1/lawyers/{id}/conversations")
    suspend fun openConversation(
        @Path("id") lawyerId: Int,
        @Body body: OpenConversationRequestDto,
    ): ConversationResponseDto

    @GET("v1/conversations")
    suspend fun getConversations(): ConversationsResponseDto

    @GET("v1/conversations/{id}")
    suspend fun getConversation(
        @Path("id") conversationId: Int,
        @Query("limit") limit: Int = 50,
    ): ConversationDetailResponseDto

    @GET("v1/conversations/{id}/messages")
    suspend fun getMessages(
        @Path("id") conversationId: Int,
        @Query("before") before: Int? = null,
        @Query("limit") limit: Int = 50,
    ): MessagesResponseDto

    @POST("v1/conversations/{id}/messages")
    suspend fun sendMessage(
        @Path("id") conversationId: Int,
        @Body body: SendMessageRequestDto,
    ): SendMessageResponseDto

    @POST("v1/conversations/{id}/read")
    suspend fun markRead(@Path("id") conversationId: Int): MarkReadResponseDto

    @GET("v1/ai/suggestions")
    suspend fun getAiSuggestions(): AiSuggestionsResponseDto

    @POST("v1/ai/ask")
    suspend fun askAi(@Body body: AiAskRequestDto): AiAskResponseDto

    @POST("v1/callback-requests")
    suspend fun requestCallback(@Body body: CallbackRequestDto): CallbackResponseDto

    @GET("v1/bookings")
    suspend fun getBookings(): BookingsResponseDto

    @POST("v1/bookings")
    suspend fun createBooking(@Body body: BookingRequestDto): BookingResponseDto

    @DELETE("v1/bookings/{id}")
    suspend fun cancelBooking(@Path("id") bookingId: Int): CancelBookingResponseDto
}
