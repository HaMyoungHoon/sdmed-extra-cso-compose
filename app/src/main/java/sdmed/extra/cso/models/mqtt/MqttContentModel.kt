package sdmed.extra.cso.models.mqtt

import sdmed.extra.cso.utils.FObjectMapper

data class MqttContentModel(
    var senderPK: String = "",
    var senderID: String = "",
    var senderName: String = "",
    var content: String = "",
    var contentType: MqttContentType = MqttContentType.None,
    var targetItemPK: String = ""
) {
    fun parseThis(payload: ByteArray): MqttContentModel {
        try {
            val buff = FObjectMapper.mqttRead(payload)
            this.senderPK = buff.senderPK
            this.senderID = buff.senderID
            this.senderName = buff.senderName
            this.content = buff.content
            this.contentType = buff.contentType
            this.targetItemPK = buff.targetItemPK
        } catch (_: Exception) {
        }
        return this
    }
}