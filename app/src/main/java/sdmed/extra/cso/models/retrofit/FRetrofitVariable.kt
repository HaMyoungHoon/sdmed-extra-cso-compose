package sdmed.extra.cso.models.retrofit

import kotlinx.coroutines.flow.MutableStateFlow

object FRetrofitVariable {
    var token = MutableStateFlow<String?>(null)
}