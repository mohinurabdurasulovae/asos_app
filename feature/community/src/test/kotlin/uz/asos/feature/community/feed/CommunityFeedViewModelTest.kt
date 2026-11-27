/*
 * CommunityFeedViewModelTest.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 */
package uz.asos.feature.community.feed

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.VoteState
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeCommunityRepository

class CommunityFeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val community = FakeCommunityRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun viewModel(articleNumber: Int = 0) = CommunityFeedViewModel(
        SavedStateHandle(mapOf("articleNumber" to articleNumber)),
        community,
        auth,
    )

    @Test
    fun `loads the feed with its topic chips`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(4, state.posts.size)
        assertEquals(5, state.topics.size)
        assertEquals(PostSort.POPULAR, state.sort)
    }

    @Test
    fun `popular is sorted by upvotes`() {
        val posts = viewModel().uiState.value.posts

        assertEquals(posts.map { it.upvotes }.sortedDescending(), posts.map { it.upvotes })
    }

    @Test
    fun `a topic chip narrows the feed`() {
        val vm = viewModel()

        vm.onAction(CommunityFeedAction.OnTopicSelect("talim"))

        assertEquals("talim", vm.uiState.value.selectedTopic)
        assertTrue(vm.uiState.value.posts.all { it.community?.slug == "talim" })
        assertEquals("talim", community.requestedCommunities.last())
    }

    @Test
    fun `the sort tabs reach the repository`() {
        val vm = viewModel()

        vm.onAction(CommunityFeedAction.OnSortSelect(PostSort.UNANSWERED))

        assertEquals(PostSort.UNANSWERED, vm.uiState.value.sort)
        assertEquals(PostSort.UNANSWERED, community.requestedSorts.last())
        assertTrue(vm.uiState.value.posts.all { it.bestAnswer == null })
    }

    @Test
    fun `an article number keeps the feed to that article`() {
        val state = viewModel(articleNumber = 31).uiState.value

        assertEquals(31, state.articleNumber)
        assertTrue(state.posts.all { it.article?.number == 31 })
    }

    @Test
    fun `no article number means the whole feed`() {
        assertNull(viewModel().uiState.value.articleNumber)
    }

    @Test
    fun `voting moves the count before the server answers`() {
        val vm = viewModel()
        val post = vm.uiState.value.posts.first { it.myVote == VoteState.NONE }
        val before = post.upvotes

        vm.onAction(CommunityFeedAction.OnVote(post.id, VoteState.UP))

        val updated = vm.uiState.value.posts.first { it.id == post.id }
        assertEquals(VoteState.UP, updated.myVote)
        assertEquals(before + 1, updated.upvotes)
        assertEquals(post.id to VoteState.UP, community.postVotes.last())
    }

    @Test
    fun `voting the same way again takes the vote back`() {
        val vm = viewModel()
        val post = vm.uiState.value.posts.first { it.myVote == VoteState.UP }

        vm.onAction(CommunityFeedAction.OnVote(post.id, VoteState.UP))

        assertEquals(VoteState.NONE, community.postVotes.last().second)
    }

    @Test
    fun `a failed vote puts the old count back`() {
        val vm = viewModel()
        val post = vm.uiState.value.posts.first { it.myVote == VoteState.NONE }
        community.voteResult = AsosResult.Error(message = "Internet aloqasi yo‘q")

        vm.onAction(CommunityFeedAction.OnVote(post.id, VoteState.UP))

        val updated = vm.uiState.value.posts.first { it.id == post.id }
        assertEquals(post.upvotes, updated.upvotes)
        assertEquals(VoteState.NONE, updated.myVote)
        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.notice)
    }

    @Test
    fun `guests are asked to sign in instead of voting`() {
        val vm = CommunityFeedViewModel(
            SavedStateHandle(mapOf("articleNumber" to 0)),
            community,
            FakeAuthRepository(signedIn = false),
        )
        val post = vm.uiState.value.posts.first()

        vm.onAction(CommunityFeedAction.OnVote(post.id, VoteState.UP))

        assertTrue(community.postVotes.isEmpty())
        assertNotNull(vm.uiState.value.notice)
    }

    @Test
    fun `a question is only sendable once it is long enough`() {
        val vm = viewModel()
        vm.onAction(CommunityFeedAction.OnAskOpen)
        assertFalse(vm.uiState.value.ask.canSend)

        vm.onAction(CommunityFeedAction.OnAskTitleChange("Uy egasi ruxsatsiz kiryapti"))
        assertFalse(vm.uiState.value.ask.canSend)

        vm.onAction(CommunityFeedAction.OnAskBodyChange("Shartnomada bu haqda hech narsa yo‘q."))
        assertTrue(vm.uiState.value.ask.canSend)
    }

    @Test
    fun `sending a question closes the sheet and clears the draft`() {
        val vm = viewModel()
        vm.onAction(CommunityFeedAction.OnAskOpen)
        vm.onAction(CommunityFeedAction.OnAskTitleChange("Uy egasi ruxsatsiz kiryapti"))
        vm.onAction(CommunityFeedAction.OnAskBodyChange("Shartnomada bu haqda hech narsa yo‘q."))

        vm.onAction(CommunityFeedAction.OnAskSend)

        assertFalse(vm.uiState.value.isAskSheetOpen)
        assertEquals("", vm.uiState.value.ask.title)
        assertEquals(listOf("Uy egasi ruxsatsiz kiryapti"), community.createdPosts)
        assertNotNull(vm.uiState.value.notice)
    }

    @Test
    fun `shows the error when nothing is loaded and retries`() {
        community.postsResult = AsosResult.Error(message = "Internet aloqasi yo‘q")
        val vm = viewModel()

        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.isEmpty)

        community.postsResult = null
        vm.onAction(CommunityFeedAction.OnRetry)

        assertNull(vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isEmpty)
    }
}
