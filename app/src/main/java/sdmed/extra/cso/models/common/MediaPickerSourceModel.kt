package sdmed.extra.cso.models.common

import android.net.Uri
import android.os.Parcelable
import android.view.View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.utils.FExtensions
import java.util.UUID

@Parcelize
data class MediaPickerSourceModel(
    var thisPK: String = UUID.randomUUID().toString(),
    var mediaUrl: String? = null,
    var mediaName: String = "",
    var mediaFileType: MediaFileType = MediaFileType.UNKNOWN,
    var mediaDateTime: String = "",
    var mediaMimeType: String = "",
): FDataModelClass<MediaPickerSourceModel.ClickEvent>(), Parcelable {
    @IgnoredOnParcel
    val duration = MutableStateFlow(-1L)
    @IgnoredOnParcel
    var clickState = MutableStateFlow(false)
    @IgnoredOnParcel
    var num: MutableStateFlow<Int?> = MutableStateFlow(null)
    @IgnoredOnParcel
    var solid: MutableStateFlow<ULong?> = MutableStateFlow(null)
    @IgnoredOnParcel
    var lastClick = MutableStateFlow(false)
    @IgnoredOnParcel
    var durationString: String = ""
    fun generateData(): MediaPickerSourceModel {
        durationString = FExtensions.getDurationToTime(duration.value)
        return this
    }
    fun onVideoClick(view: View) {
        relayCommand?.execute(arrayListOf(view, this))
    }
    enum class ClickEvent(var index: Int) {
        SELECT(0),
        SELECT_LONG(1),
    }
}