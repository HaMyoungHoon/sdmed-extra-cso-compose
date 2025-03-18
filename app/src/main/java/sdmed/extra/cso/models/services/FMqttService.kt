package sdmed.extra.cso.models.services

import android.app.Service
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe
import android.content.Context
import android.content.Intent
import android.os.IBinder
import sdmed.extra.cso.R
import sdmed.extra.cso.interfaces.repository.IMqttRepository
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.mqtt.MqttConnectModel
import sdmed.extra.cso.models.mqtt.MqttContentModel
import sdmed.extra.cso.models.mqtt.MqttContentType
import sdmed.extra.cso.models.services.FNotificationService.NotifyType
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FMqttService(private val context: Context): Service()  {
    @Inject private lateinit var notificationService: FNotificationService
    @Inject private lateinit var mqttRepository: IMqttRepository
    var client: Mqtt5Client? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    suspend fun mqttInit() {
        val ret = mqttRepository.getSubscribe()
        if (ret.result != true) {
            return
        }
        mqttConnect(ret.data)
    }
    suspend fun mqttSend(topic: String, mqttContentModel: MqttContentModel) = mqttRepository.postPublish(topic, mqttContentModel)
    suspend fun mqttEDIRequest(thisPK: String, content: String) = mqttRepository.postEDIRequest(thisPK, content)
    suspend fun mqttEDIFileAdd(thisPK: String, content: String) = mqttRepository.postEDIFileAdd(thisPK, content)
    suspend fun mqttQnA(thisPK: String, content: String) = mqttRepository.postQnA(thisPK, content)
    fun mqttConnect(mqttConnectModel: MqttConnectModel?) {
        mqttConnectModel ?: return
        if (client?.state?.isConnected == true) {
            return
        }
        val clientId = "aos-extra-cso/${FExtensions.getUUID()}"
        val brokerBuff = mqttConnectModel.brokerUrl.filter { it.contains("tc") || it.contains("tcp") }
        if (brokerBuff.isEmpty()) {
            return
        }
        var protocol = ""
        var brokerUrl = ""
        var brokerPort = 0
        brokerBuff[0].split("://").run {
            if (this.size > 1) {
                protocol = this[0]
                brokerUrl = this[1]
            }
        }
        brokerUrl.split(":").run {
            if (this.size > 1) {
                brokerUrl = this[0]
                brokerPort = this[1].toIntOrNull() ?: 0
            }
        }
        if (brokerUrl.isEmpty() || brokerPort == 0) {
            return
        }

        try {
            client = Mqtt5Client.builder()
                .identifier(clientId)
                .serverHost(brokerUrl)
                .serverPort(brokerPort)
                .simpleAuth()
                .username(mqttConnectModel.userName)
                .password(mqttConnectModel.password.toByteArray())
                .applySimpleAuth()
                .build()
            val ack = client?.toBlocking()?.connect()
            mqttConnectModel.topic.forEach {
                val mqtt5Subscribe = Mqtt5Subscribe.builder()
                    .topicFilter(it)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .build()
                client?.toAsync()?.subscribe(mqtt5Subscribe) { x -> parsePublish(x) }
            }
            val mqtt5Subscribe = Mqtt5Subscribe.builder()
                .topicFilter("test/1234/test/1234")
                .qos(MqttQos.AT_LEAST_ONCE)
                .build()
            client?.toAsync()?.subscribe(mqtt5Subscribe) { x -> parsePublish(x) }
        } catch (e: Exception) {
        }
    }
    fun parsePublish(data: Mqtt5Publish) {
        try {
            val mqttContentModel = MqttContentModel().parseThis("topic", data.payloadAsBytes)
            if (mqttContentModel.senderPK == FAmhohwa.getThisPK(context)) {
                return
            }
            val title = when (mqttContentModel.contentType) {
                MqttContentType.None -> context.getString(R.string.mqtt_title_none)
                MqttContentType.QNA_REQUEST -> context.getString(R.string.mqtt_title_qnaRequest)
                MqttContentType.QNA_REPLY -> context.getString(R.string.mqtt_title_qnaReply)
                MqttContentType.EDI_REQUEST -> context.getString(R.string.mqtt_title_ediRequest)
                MqttContentType.EDI_REJECT -> context.getString(R.string.mqtt_title_ediReject)
                MqttContentType.EDI_OK -> context.getString(R.string.mqtt_title_ediOK)
                MqttContentType.EDI_RECEP -> context.getString(R.string.mqtt_title_ediRecep)
                MqttContentType.EDI_FILE_ADD -> context.getString(R.string.mqtt_title_ediFileAdd)
                MqttContentType.EDI_FILE_DELETE -> context.getString(R.string.mqtt_title_ediDelete)
            }
            when (mqttContentModel.contentType) {
                MqttContentType.None -> notificationService.sendNotify(context, title, mqttContentModel.content, NotifyType.WITH_VIBRATE)
                MqttContentType.QNA_REQUEST -> { }
                MqttContentType.QNA_REPLY -> notificationService.sendNotify(context, NotifyIndex.QNA_RESPONSE, title, mqttContentModel.content, NotifyType.WITH_VIBRATE, isCancel = true, thisPK = mqttContentModel.targetItemPK)
                MqttContentType.EDI_REQUEST -> { }
                MqttContentType.EDI_REJECT -> notificationService.sendNotify(context, NotifyIndex.EDI_RESPONSE, title, mqttContentModel.content, NotifyType.WITH_VIBRATE, isCancel = true, thisPK = mqttContentModel.targetItemPK)
                MqttContentType.EDI_OK -> notificationService.sendNotify(context, NotifyIndex.EDI_RESPONSE, title, mqttContentModel.content, NotifyType.WITH_VIBRATE, isCancel = true, thisPK = mqttContentModel.targetItemPK)
                MqttContentType.EDI_RECEP -> notificationService.sendNotify(context, NotifyIndex.EDI_RESPONSE, title, mqttContentModel.content, NotifyType.WITH_VIBRATE, isCancel = true, thisPK = mqttContentModel.targetItemPK)
                MqttContentType.EDI_FILE_ADD -> { }
                MqttContentType.EDI_FILE_DELETE -> notificationService.sendNotify(context, NotifyIndex.EDI_FILE_REMOVE, title, mqttContentModel.content, NotifyType.WITH_VIBRATE, isCancel = true, thisPK = mqttContentModel.targetItemPK)
            }
        } catch (_: Exception) {
        }
    }
}