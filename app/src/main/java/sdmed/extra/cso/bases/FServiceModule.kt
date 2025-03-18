package sdmed.extra.cso.bases

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sdmed.extra.cso.models.services.FBackgroundEDIFileUpload
import sdmed.extra.cso.models.services.FBackgroundEDIRequestNewUpload
import sdmed.extra.cso.models.services.FBackgroundEDIRequestUpload
import sdmed.extra.cso.models.services.FBackgroundQnAUpload
import sdmed.extra.cso.models.services.FBackgroundUserFileUpload
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.models.services.FNotificationService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FServiceModule {
    @Provides @Singleton fun notificationService(@ApplicationContext context: Context) = FNotificationService(context)
    @Provides @Singleton fun mqttService(@ApplicationContext context: Context) = FMqttService(context)
    @Provides @Singleton fun backgroundEDIFileUpload(@ApplicationContext context: Context) = FBackgroundEDIFileUpload(context)
    @Provides @Singleton fun backgroundEDIRequestNewUpload(@ApplicationContext context: Context) = FBackgroundEDIRequestNewUpload(context)
    @Provides @Singleton fun backgroundEDIRequestUpload(@ApplicationContext context: Context) = FBackgroundEDIRequestUpload(context)
    @Provides @Singleton fun backgroundQnAUpload(@ApplicationContext context: Context) = FBackgroundQnAUpload(context)
    @Provides @Singleton fun backgroundUserFileUpload(@ApplicationContext context: Context) = FBackgroundUserFileUpload(context)
}