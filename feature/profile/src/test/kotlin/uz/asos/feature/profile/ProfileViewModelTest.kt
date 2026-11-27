package uz.asos.feature.profile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.data.ProfileSampleData
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeUserRepository

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val user = FakeUserRepository()

    private fun viewModel(signedIn: Boolean = true) =
        ProfileViewModel(user, FakeAuthRepository(signedIn = signedIn))

    @Test
    fun `loads the whole profile in one call`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Aziza Karimova", state.user?.name)
        assertEquals(18, state.quizzesDone)
        assertEquals(7, state.casesSolved)
        assertEquals(1, user.profileCalls)
    }

    @Test
    fun `formats the points the way the design writes them`() {
        assertEquals("1 240", viewModel().uiState.value.pointsLabel)
    }

    @Test
    fun `works out how far the next level is`() {
        val progress = viewModel().uiState.value.levelProgress

        assertNotNull(progress)
        assertEquals(4, progress?.nextLevel)
        assertEquals("1 240", progress?.pointsLabel)
        assertEquals("1 500", progress?.targetLabel)
        assertEquals(0.48f, progress?.fraction ?: 0f, 0.001f)
    }

    @Test
    fun `the last level has nothing left to climb to`() {
        val maxed = ProfileSampleData.profile.let { profile ->
            profile.copy(user = profile.user.copy(level = profile.user.level.copy(nextAt = null)))
        }
        user.profileResult = AsosResult.Success(maxed)

        assertNull(viewModel().uiState.value.levelProgress)
    }

    @Test
    fun `a guest sees the sign-in card and no request is made`() {
        val state = viewModel(signedIn = false).uiState.value

        assertFalse(state.isSignedIn)
        assertFalse(state.isLoading)
        assertNull(state.user)
        assertNull(state.errorMessage)
        assertEquals(0, user.profileCalls)
    }

    @Test
    fun `the daily reminder switch saves and keeps its new position`() {
        val vm = viewModel()
        assertTrue(vm.uiState.value.notifyDaily)

        vm.onAction(ProfileAction.OnNotifyDailyChange(false))

        assertFalse(vm.uiState.value.notifyDaily)
        assertEquals(listOf(false), user.savedNotifyDaily)
    }

    @Test
    fun `a failed save puts the switch back`() {
        val vm = viewModel()
        user.updateResult = AsosResult.Error(message = "Internet aloqasi yo‘q")

        vm.onAction(ProfileAction.OnNotifyDailyChange(false))

        assertTrue(vm.uiState.value.notifyDaily)
        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.notice)
    }

    @Test
    fun `choosing a text size closes the sheet and saves it`() {
        val vm = viewModel()
        vm.onAction(ProfileAction.OnTextSizeOpen)
        assertTrue(vm.uiState.value.isTextSizeSheetOpen)

        vm.onAction(ProfileAction.OnTextSizeSelect(ReaderTextSize.LARGE))

        assertFalse(vm.uiState.value.isTextSizeSheetOpen)
        assertEquals(ReaderTextSize.LARGE, vm.uiState.value.textSize)
        assertEquals(listOf(ReaderTextSize.LARGE), user.savedTextSizes)
    }

    @Test
    fun `choosing a language saves it and shows it on the row`() {
        val vm = viewModel()
        vm.onAction(ProfileAction.OnLanguageOpen)

        vm.onAction(ProfileAction.OnLanguageSelect("ru"))

        assertFalse(vm.uiState.value.isLanguageSheetOpen)
        assertEquals("ru", vm.uiState.value.user?.locale)
        assertEquals(listOf("ru"), user.savedLocales)
    }

    @Test
    fun `a failed language change puts the old one back`() {
        val vm = viewModel()
        user.updateResult = AsosResult.Error(message = "Internet aloqasi yo‘q")

        vm.onAction(ProfileAction.OnLanguageSelect("ru"))

        assertEquals("uz-Latn", vm.uiState.value.user?.locale)
        assertNotNull(vm.uiState.value.notice)
    }

    @Test
    fun `counts the saved items for the Saqlanganlar rows`() {
        val bookmarks = viewModel().uiState.value.bookmarks

        assertEquals(8, bookmarks.articles)
        assertEquals(12, bookmarks.terms)
        assertEquals(5, bookmarks.posts)
    }

    @Test
    fun `shows the error when nothing is loaded and retries`() {
        user.profileResult = AsosResult.Error(message = "Internet aloqasi yo‘q")
        val vm = viewModel()

        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.errorMessage)
        assertNull(vm.uiState.value.user)

        user.profileResult = AsosResult.Success(ProfileSampleData.profile)
        vm.onAction(ProfileAction.OnRetry)

        assertNull(vm.uiState.value.errorMessage)
        assertNotNull(vm.uiState.value.user)
    }
}
