/*
 * CommunityFeedViewModelTest.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Unit tests for CommunityFeedViewModel using fakes from core:testing (no network).
 *
 * PLACEHOLDER: not implemented yet.
 */
package uz.asos.feature.community.feed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityFeedViewModelTest {

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
    fun filtersByTopicAndIncrementsVotes() {
        val viewModel = CommunityFeedViewModel()
        viewModel.onAction(CommunityFeedAction.SelectTopic("housing"))
        val state = viewModel.uiState.value as CommunityFeedUiState.Success
        assertEquals(1, state.posts.size)

        val initialVotes = state.posts.first().votes
        viewModel.onAction(CommunityFeedAction.UpvotePost(state.posts.first().id))
        val updated = viewModel.uiState.value as CommunityFeedUiState.Success
        assertEquals(initialVotes + 1, updated.posts.first().votes)
    }
}
