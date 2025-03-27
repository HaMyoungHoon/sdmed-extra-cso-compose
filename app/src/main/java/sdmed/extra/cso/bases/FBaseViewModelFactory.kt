package sdmed.extra.cso.bases

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class FBaseViewModelFactory<T: FBaseViewModel>(private val dataContextClass: KClass<T>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
            ?: throw IllegalStateException("Application is required to create ${modelClass.simpleName}")

        if (modelClass.isAssignableFrom(dataContextClass.java)) {
            @Suppress("UNCHECKED_CAST")
            return dataContextClass.java.getConstructor(Application::class.java)
                .newInstance(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
    }
}