/*
 * AsosDispatchers.kt
 * Module: :core:common   ·   Work package: WP-00
 *
 * Qualifier annotations for IO / Default dispatchers.
 *   class Repo @Inject constructor(@Dispatcher(AsosDispatchers.IO) private val io: CoroutineDispatcher)
 */
package uz.asos.core.common.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: AsosDispatchers)

enum class AsosDispatchers {
    Default,
    IO,
}
