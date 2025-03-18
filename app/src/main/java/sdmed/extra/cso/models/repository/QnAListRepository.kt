package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.interfaces.services.IQnAListService
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QnAListRepository @Inject constructor(private val service: IQnAListService): IQnAListRepository {
    override suspend fun getList(page: Int, size: Int) = FExtensions.restTryT { service.getList(page, size) }
    override suspend fun getLike(searchString: String, page: Int, size: Int) = FExtensions.restTryT { service.getLike(searchString, page, size) }
    override suspend fun getHeaderData(thisPK: String) = FExtensions.restTryT { service.getHeaderData(thisPK) }
    override suspend fun getContentData(thisPK: String) = FExtensions.restTryT { service.getContentData(thisPK) }
    override suspend fun postData(title: String, qnaContentModel: QnAContentModel) = FExtensions.restTryT { service.postData(title, qnaContentModel) }
    override suspend fun postReply(thisPK: String, qnaReplyModel: QnAReplyModel) = FExtensions.restTryT { service.postReply(thisPK, qnaReplyModel) }
    override suspend fun putData(thisPK: String) = FExtensions.restTryT { service.putData(thisPK) }
}