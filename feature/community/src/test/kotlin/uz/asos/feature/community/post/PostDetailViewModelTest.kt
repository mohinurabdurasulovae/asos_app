/*
 * PostDetailViewModelTest.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Unit tests for PostDetailViewModel using fakes from core:testing (no network).
 *
 * PLACEHOLDER: not implemented yet.
 */
package uz.asos.feature.community.post

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun sendsReplyAndIncrementsCount() {
        val viewModel = PostDetailViewModel(SavedStateHandle())
        val before = viewModel.uiState.value as PostDetailUiState.Success
        val initialCount = before.post.commentCount

        viewModel.onAction(PostDetailAction.UpdateReplyText("Foydali maslahat uchun rahmat"))
        viewModel.onAction(PostDetailAction.SendReply)

        val after = viewModel.uiState.value as PostDetailUiState.Success
        assertEquals(initialCount + 1, after.post.commentCount)
        assertTrue(after.replies.last().content.contains("Foydali maslahat"))
    }
}
