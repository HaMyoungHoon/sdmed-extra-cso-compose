package sdmed.extra.cso.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow

object FEventBus {
    val events = MutableSharedFlow<Any>(replay = 0, extraBufferCapacity = 10)
    suspend fun emit(event: Any) {
        events.emit(event)
    }
    inline fun <reified T> createEventChannel(): Channel<T> {
        val channel = Channel<T>(Channel.UNLIMITED)
        FCoroutineUtil.coroutineScope({
            events.collect { event ->
                if (event is T) {
                    channel.send(event)
                }
            }
        })
        return channel
    }
}