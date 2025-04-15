package sdmed.extra.cso.models.retrofit.hospitals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HospitalTempFileModel(
    var thisPK: String = "",
    var hospitalTempPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var fileType: HospitalTempFileType = HospitalTempFileType.TAXPAYER,
    var regDate: String = "",
): Parcelable {
}