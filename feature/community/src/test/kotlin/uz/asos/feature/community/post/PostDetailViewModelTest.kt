/*
 * PostDetailViewModelTest.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 */
package uz.asos.feature.community.post

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.community.VoteState
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeCommunityRepository

class PostDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val community = FakeCommunityRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun viewModel(postId: Int = 1) =
        PostDetailViewModel(SavedStateHandle(mapOf("postId" to postId)), community, auth)

    @Test
    fun `loads the discussion and lifts the best answer out of the thread`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertNotNull(state.post)
        assertEquals(1, state.bestAnswer?.id)
        assertTrue(state.bestAnswer?.isLawyer ?: false)
        // The best answer is shown on its own, so it is not in the list below it.
        assertTrue(state.comments.none { it.isBest })
        assertEquals(1, state.comments.size)
    }

    @Test
    fun `keeps the nested reply under its parent`() {
        val state = viewModel().uiState.value
        val threaded = state.comments.first { it.replies.isNotEmpty() }

        assertEquals(1, threaded.replies.size)
        assertEquals(threaded.id, threaded.replies.first().parentId)
    }

    @Test
    fun `voting the post moves the count before the server answers`() {
        val vm = viewModel()
        val before = vm.uiState.value.post!!.post.upvotes

        vm.onAction(PostDetailAction.OnPostVote(VoteState.DOWN))

        // The sample post is already upvoted, so a downvote swings the count by two.
        assertEquals(before - 2, vm.uiState.value.post!!.post.upvotes)
        assertEquals(VoteState.DOWN, vm.uiState.value.post!!.post.myVote)
    }

    @Test
    fun `a failed vote puts the old count back`() {
        val vm = viewModel()
        val before = vm.uiState.value.post!!.post.upvotes
        community.voteResult = AsosResult.Error(message = "Internet aloqasi yo‘q")

        vm.onAction(PostDetailAction.OnPostVote(VoteState.DOWN))

        assertEquals(before, vm.uiState.value.post!!.post.upvotes)
        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.notice)
    }

    @Test
    fun `voting a nested reply finds it in the thread`() {
        val vm = viewModel()
        val reply = vm.uiState.value.comments.first { it.replies.isNotEmpty() }.replies.first()
        val before = reply.upvotes

        vm.onAction(PostDetailAction.OnCommentVote(reply.id, VoteState.UP))

        val updated = vm.uiState.value.comments
            .first { it.replies.isNotEmpty() }
            .replies
            .first { it.id == reply.id }
        assertEquals(before + 1, updated.upvotes)
        assertEquals(reply.id to VoteState.UP, community.commentVotes.last())
    }

    @Test
    fun `replying to a comment remembers who is being answered`() {
        val vm = viewModel()
        val comment = vm.uiState.value.comments.first()

        vm.onAction(PostDetailAction.OnReplyTo(comment.id, comment.author.name))

        assertEquals(comment.id, vm.uiState.value.reply.parentId)
        assertEquals(comment.author.name, vm.uiState.value.reply.parentAuthor)
    }

    @Test
    fun `a reply is only sendable once something is written`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.reply.canSend)

        vm.onAction(PostDetailAction.OnReplyChange("Rahmat"))

        assertTrue(vm.uiState.value.reply.canSend)
    }

    @Test
    fun `sending a reply clears the draft and reloads the thread`() {
        val vm = viewModel()
        val callsBefore = community.postCalls
        vm.onAction(PostDetailAction.OnReplyChange("Rahmat, tekshiraman"))

        vm.onAction(PostDetailAction.OnReplySend)

        assertEquals("", vm.uiState.value.reply.body)
        assertNull(vm.uiState.value.reply.parentId)
        assertEquals(1, community.addedComments.size)
        assertEquals(callsBefore + 1, community.postCalls)
    }

    @Test
    fun `guests are asked to sign in instead of replying`() {
        val vm = PostDetailViewModel(
            SavedStateHandle(mapOf("postId" to 1)),
            community,
            FakeAuthRepository(signedIn = false),
        )
        vm.onAction(PostDetailAction.OnReplyChange("Rahmat"))

        vm.onAction(PostDetailAction.OnReplySend)

        assertTrue(community.addedComments.isEmpty())
        assertNotNull(vm.uiState.value.notice)
    }

    @Test
    fun `joining the topic flips the button`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.post!!.post.community!!.isMember)

        vm.onAction(PostDetailAction.OnMembershipToggle)

        assertTrue(vm.uiState.value.post!!.post.community!!.isMember)
        assertEquals("uy-joy" to true, community.memberships.last())
    }

    @Test
    fun `shows the error when nothing is loaded and retries`() {
        community.postResult = AsosResult.Error(message = "Internet aloqasi yo‘q")
        val vm = viewModel()

        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.errorMessage)
        assertNull(vm.uiState.value.post)

        community.postResult = AsosResult.Success(
            uz.asos.core.testing.data.CommunitySampleData.rentPostDetail,
        )
        vm.onAction(PostDetailAction.OnRetry)

        assertNull(vm.uiState.value.errorMessage)
        assertNotNull(vm.uiState.value.post)
    }
}
