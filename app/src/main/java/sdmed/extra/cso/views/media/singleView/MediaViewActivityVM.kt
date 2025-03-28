package sdmed.extra.cso.views.media.singleView

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.utils.FContentsType

class MediaViewActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val title = MutableStateFlow<String>("")
    val item = MutableStateFlow(MediaViewModel())

    fun setItemData(data: MediaViewParcelModel?) {
        data ?: return
        item.value = MediaViewModel().parse(data)
    }

    override fun fakeInit() {
        item.value = MediaViewModel().apply {
            blobUrl.value = FConstants.TEST_BLOB_URL
            mimeType.value = FContentsType.type_pdf
            originalFilename.value = "파일 이름 asd lk fj aasadfsdf safssdasfsldfkjsdfkljl sdkfjslkdjasjdflkasd.xlsx"
        }
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0)
    }
}