package sdmed.extra.cso.bases

import android.app.Service
import android.content.Context
import android.content.Intent
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.DIContext
import org.kodein.di.diContext

abstract class FBaseService(applicationContext: Context): Service(), DIAware {
    override val diContext: DIContext<Context> = diContext(applicationContext)
    override val di: DI by lazy { (diContext.value as FMainApplication).di }
    override fun onBind(intent: Intent?) = null
    val context get() = diContext.value
}