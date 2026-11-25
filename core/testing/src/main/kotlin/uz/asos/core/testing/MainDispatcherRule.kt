/*
 * MainDispatcherRule.kt
 * Module: :core:testing   ·   Work package: WP-02
 *
 * JUnit rule that swaps Dispatchers.Main for a test dispatcher, so ViewModels that use
 * viewModelScope can run in plain JVM tests.
 *
 *     @get:Rule val mainDispatcherRule = MainDispatcherRule()
 */
package uz.asos.core.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
