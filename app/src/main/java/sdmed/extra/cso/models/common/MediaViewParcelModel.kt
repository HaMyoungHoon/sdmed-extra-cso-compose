package sdmed.extra.cso.models.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.qna.QnAFileModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyFileModel
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel

@Parcelize
data class MediaViewParcelModel(
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
): Parcelable {
    fun parse(data: EDIUploadPharmaFileModel): MediaViewParcelModel {
        this.blobUrl = data.blobUrl
        this.originalFilename = data.originalFilename
        this.mimeType = data.mimeType
        return this
    }
    fun parse(data: QnAFileModel): MediaViewParcelModel {
        this.blobUrl = data.blobUrl
        this.originalFilename = data.originalFilename
        this.mimeType = data.mimeType
        return this
    }
    fun parse(data: QnAReplyFileModel): MediaViewParcelModel {
        this.blobUrl = data.blobUrl
        this.originalFilename = data.originalFilename
        this.mimeType = data.mimeType
        return this
    }
    fun parse(data: UserTrainingModel): MediaViewParcelModel {
        this.blobUrl = data.blobUrl
        this.originalFilename = data.originalFilename
        this.mimeType = data.mimeType
        return this
    }
}