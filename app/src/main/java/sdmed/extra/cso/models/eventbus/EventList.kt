package sdmed.extra.cso.models.eventbus

sealed class EventList {
    data class EDIUploadEvent(var thisPK: String = ""): EventList()
    data class QnAUploadEvent(var thisPK: String = ""): EventList()
    data class UserFileUploadEvent(var thisPK: String = ""): EventList()
    data object MultiLoginEvent: EventList()
}