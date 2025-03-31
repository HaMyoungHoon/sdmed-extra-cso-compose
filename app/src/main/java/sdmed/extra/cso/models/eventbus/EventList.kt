package sdmed.extra.cso.models.eventbus

sealed class EventList {
    data class UserFileUploadEvent(var thisPK: String = ""): EventList()
    data object MultiLoginEvent: EventList()
}