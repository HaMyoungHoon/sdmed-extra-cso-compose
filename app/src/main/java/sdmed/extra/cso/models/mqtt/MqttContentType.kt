package sdmed.extra.cso.models.mqtt

enum class MqttContentType(var index: Int) {
    None(0),
    QNA_REQUEST(1),
    QNA_REPLY(2),
    EDI_REQUEST(3),
    EDI_REJECT(4),
    EDI_OK(5),
    EDI_RECEP(6),
    EDI_FILE_ADD(7),
    EDI_FILE_DELETE(8),
}