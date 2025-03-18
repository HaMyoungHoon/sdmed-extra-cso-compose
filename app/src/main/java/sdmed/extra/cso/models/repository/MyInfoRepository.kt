package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.interfaces.services.IMyInfoService
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.common.UserFileType
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepository @Inject constructor(private val service: IMyInfoService): IMyInfoRepository {
    override suspend fun getData(relationView: Boolean) = FExtensions.restTryT { service.getData(relationView) }
    override suspend fun putPasswordChange(currentPW: String, afterPW: String, confirmPW: String) = FExtensions.restTryT { service.putPasswordChange(currentPW, afterPW, confirmPW) }
    override suspend fun putUserFileImageUrl(thisPK: String, blobModel: BlobUploadModel, userFileType: UserFileType) = FExtensions.restTryT { service.putUserFileImageUrl(thisPK, blobModel, userFileType) }
}