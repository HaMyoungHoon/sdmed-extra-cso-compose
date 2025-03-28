package sdmed.extra.cso.models.retrofit

import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicBoolean

object FRetrofitVariable {
    var token = MutableStateFlow<String?>(null)
    var refreshing = AtomicBoolean(false)
}