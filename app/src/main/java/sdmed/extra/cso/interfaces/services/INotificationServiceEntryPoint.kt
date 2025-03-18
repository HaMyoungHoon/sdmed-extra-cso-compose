package sdmed.extra.cso.interfaces.services

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sdmed.extra.cso.models.services.FNotificationService

@EntryPoint
@InstallIn(SingletonComponent::class)
interface INotificationServiceEntryPoint {
    fun getNotificationService(): FNotificationService
}