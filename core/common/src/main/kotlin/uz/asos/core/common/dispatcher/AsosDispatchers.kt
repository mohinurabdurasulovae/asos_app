package uz.asos.core.common.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: AsosDispatchers)

enum class AsosDispatchers {
    Default,
    IO,
}
