package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel

interface IQnAListRepository {
    suspend fun getList(page: Int = 0, size: Int = 10): RestResultT<RestPage<MutableList<QnAHeaderModel>>>
    suspend fun getLike(searchString: String, page: Int = 0, size: Int = 10): RestResultT<RestPage<MutableList<QnAHeaderModel>>>
    suspend fun getHeaderData(thisPK: String): RestResultT<QnAHeaderModel>
    suspend fun getContentData(thisPK: String): RestResultT<QnAContentModel>
    suspend fun postData(title: String, qnaContentModel: QnAContentModel): RestResultT<QnAHeaderModel>
    suspend fun postReply(thisPK: String, qnaReplyModel: QnAReplyModel): RestResultT<QnAReplyModel>
    suspend fun putData(thisPK: String): RestResultT<QnAHeaderModel>
}