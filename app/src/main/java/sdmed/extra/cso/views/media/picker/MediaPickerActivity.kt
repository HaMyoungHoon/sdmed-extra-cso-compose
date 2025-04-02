package sdmed.extra.cso.views.media.picker

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import kotlinx.coroutines.withTimeoutOrNull
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.SelectListModel
import sdmed.extra.cso.utils.FCoil
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.utils.FLog
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.theme.FThemeUtil

class MediaPickerActivity: FBaseActivity<MediaPickerActivityVM>() {
    override val dataContext: MediaPickerActivityVM by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initCommand()
        init()
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                calcScreen(windowSize, displayFeatures)
            }
        }
    }

    @Composable
    override fun phone(dataContext: MediaPickerActivityVM) {
        mediaPickerScreenPhone(dataContext)
    }
    @Composable
    override fun tablet(dataContext: MediaPickerActivityVM) {
        mediaPickerScreenTablet(dataContext)
    }
    @Composable
    override fun twoPane(dataContext: MediaPickerActivityVM, displayFeatures: List<DisplayFeature>) {
        mediaPickerScreenTwoPane(dataContext, displayFeatures)
    }

    private fun init() {
        FCoil.clearCache()
        FCoroutineUtil.coroutineScopeIO({
            withTimeoutOrNull(3000) {
                val mediaList = getImageList()
//        getVideoList(mediaList)
                getFileList(mediaList)
                dataContext.setItems(mediaList)
                dataContext.selectItem(0)
                dataContext.mediaTargetPK = intent.getStringExtra(FConstants.MEDIA_TARGET_PK) ?: ""
                val buffList = intent.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST)
                dataContext.ableSelectCountStringSuffix = getString(R.string.media_able_click_suffix_desc)
                dataContext.setPreviousMedia(buffList)
                dataContext.setMediaMaxCount(intent.getIntExtra(FConstants.MEDIA_MAX_COUNT, -1))
//        binding?.playerView?.player = ExoPlayer.Builder(this).build()
            }
        })
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setMediaItemCommand(data)
        setDialogCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? MediaPickerActivityVM.ClickEvent ?: return
        when (eventName) {
            MediaPickerActivityVM.ClickEvent.CLOSE -> {
                setResult(RESULT_CANCELED)
                finish()
            }
            MediaPickerActivityVM.ClickEvent.CONFIRM -> {
                if (!dataContext.confirmEnable.value) {
                    return
                }
                setResult(RESULT_OK, Intent().apply {
                    putExtra(FConstants.MEDIA_TARGET_PK, dataContext.mediaTargetPK)
                    putParcelableList(FConstants.MEDIA_LIST, dataContext.getClickItems())
                })
                finish()
            }
        }
    }
    private fun setMediaItemCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaPickerSourceModel ?: return
        when (eventName) {
            MediaPickerSourceModel.ClickEvent.SELECT -> {
                val findItem = dataContext.clickItemBuff.firstOrNull { it.mediaPath == dataBuff.mediaPath }
                if (findItem != null) {
                    if (dataContext.mediaPath.value == findItem.mediaPath) {
                        dataContext.removeClickedItem(dataBuff)
                        dataContext.confirmEnable.value = dataContext.getClickItemCount() > 0
                        val lastItem = dataContext.clickItemBuff.lastOrNull()
                        if (lastItem != null) {
                            dataContext.setLastClickedItem(lastItem)
                            dataContext.mediaPath.value = lastItem.mediaPath
                            dataContext.mediaFileType.value = lastItem.mediaFileType
                            dataContext.mediaName.value = lastItem.mediaName
                        } else {
                            dataContext.mediaPath.value = null
                        }
                    } else {
                        dataContext.setLastClickedItem(dataBuff)
                        dataContext.mediaPath.value = findItem.mediaPath
                        dataContext.mediaFileType.value = findItem.mediaFileType
                        dataContext.mediaName.value = findItem.mediaName
                    }
                    playerPlay()
                    return
                }
                if (!dataContext.isPossibleSelect() && !dataBuff.clickState.value) {
                    return
                }

                dataContext.setLastClickedItem(dataBuff)
                dataContext.appendClickedItem(dataBuff)
                dataContext.mediaPath.value = dataBuff.mediaPath
                dataContext.mediaFileType.value = dataBuff.mediaFileType
                dataContext.mediaName.value = dataBuff.mediaName
                playerPlay()
                dataContext.confirmEnable.value = dataContext.getClickItemCount() > 0
            }
            MediaPickerSourceModel.ClickEvent.SELECT_LONG -> {
//                val items = mutableListOf<SelectListModel>()
//                items.add(SelectListModel().apply {
//                    itemIndex = 1
//                    iconResId = R.drawable.vector_delete_def
//                    stringResId = R.string.remove_desc
//                    this.data = dataBuff
//                })
//                SelectDialog(items, 0, dataContext.relayCommand).show(supportFragmentManager, "")
            }
        }
    }
    private fun setVideoCommand(data: Any?) {
//        if (data !is ArrayList<*> || data.size <= 1) return
//        val view = data[0] as? PlayerView ?: return
//        val dataBuff = data[1] as? MediaPickerSourceModel ?: return
//        dataBuff.clickState = !dataBuff.clickState
//        if (dataBuff.clickState) {
//            dataContext.appendClickedItem(dataBuff)
//        } else {
//            dataContext.removeClickedItem(dataBuff)
//        }
//        previousView?.player?.stop()
//        previousView = view
//        previousView?.player?.prepare()
//        previousView?.player?.play()
//        dataContext.confirmEnable.value = dataContext.getClickItemCount() > 0
    }
    private fun setDialogCommand(data: Any?) {
        if (data !is java.util.ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? SelectListModel.ClickEvent ?: return
        val dataBuff = data[1] as? SelectListModel ?: return
        val modelBuff = dataBuff.data as? MediaPickerSourceModel ?: return
        when (eventName) {
            SelectListModel.ClickEvent.SELECT -> {
                val uri = modelBuff.mediaPath
                if (uri != null) {
                    dataContext.removeClickedItem(modelBuff)
                    dataContext.removeItems(modelBuff)
                    try {
                        FImageUtils.fileDelete(this, uri)
                    } catch (_: Exception) {

                    }
                }
            }
        }
    }
    private fun getFileList(buff: MutableList<Pair<String, MutableList<MediaPickerSourceModel>>>?): MutableList<Pair<String, MutableList<MediaPickerSourceModel>>> {
        val mediaList = buff ?: mutableListOf()
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.MIME_TYPE)
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?, ?, ?, ?)"
        val selectionArgs = arrayOf(FContentsType.type_pdf, FContentsType.type_xlsx, FContentsType.type_xls, FContentsType.type_zip)
        try {
            this.contentResolver.call(MediaStore.Files.getContentUri("external"), "releasePersistableUriPermission", null, null)
        } catch (e: Exception) {
            FLog.debug("mhha", "${e.message}")
        }

        val query = this.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            val dateTimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            while (cursor.moveToNext()) {
                val uri = cursor.getString(dataColumn)
                val name = cursor.getString(nameColumn)
                val folderName = cursor.getString(folderColumn)
                val dateTime = cursor.getString(dateTimeColumn)
                val mimeString = cursor.getString(mimeTypeColumn)
                if (mimeString != FContentsType.type_pdf && mimeString != FContentsType.type_xlsx && mimeString != FContentsType.type_xls && mimeString != FContentsType.type_zip) {
                    continue
                }
                val findFolder = mediaList.find { x -> x.first == folderName }
                if (findFolder != null) {
                    findFolder.second.add(
                        MediaPickerSourceModel().apply {
                            mediaPath = uri.toUri()
                            mediaName = name
                            mediaFileType = MediaFileType.fromMimeType(mimeString)
                            mediaDateTime = dateTime
                            mediaMimeType = mimeString
                        }
                    )
                } else {
                    mediaList.add(Pair(folderName, arrayListOf(
                        MediaPickerSourceModel().apply {
                            mediaPath = uri.toUri()
                            mediaName = name
                            mediaFileType = MediaFileType.fromMimeType(mimeString)
                            mediaDateTime = dateTime
                            mediaMimeType = mimeString
                        }
                    )))
                }
            }
        }
        return mediaList
    }
    private fun getImageList(): MutableList<Pair<String, MutableList<MediaPickerSourceModel>>> {
        val mediaList = mutableListOf<Pair<String, MutableList<MediaPickerSourceModel>>>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.MIME_TYPE)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        try {
            this.contentResolver.call(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "releasePersistableUriPermission", null, null)
        } catch (e: Exception) {
            FLog.debug("mhha", "${e.message}")
        }

        val query = this.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dateTimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val folderName = cursor.getString(folderColumn)
                val dateTime = cursor.getString(dateTimeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                val findFolder = mediaList.find { x -> x.first == folderName }
                if (findFolder != null) {
                    findFolder.second.add(
                        MediaPickerSourceModel().apply {
                            mediaPath = uri
                            mediaName = name
                            mediaFileType = MediaFileType.IMAGE
                            mediaDateTime = dateTime
                            mediaMimeType = mimeType
                        }
                    )
                } else {
                    mediaList.add(Pair(folderName, arrayListOf(
                        MediaPickerSourceModel().apply {
                            mediaPath = uri
                            mediaName = name
                            mediaFileType = MediaFileType.IMAGE
                            mediaDateTime = dateTime
                            mediaMimeType = mimeType
                        }
                    )))
                }
            }
        }
        return mediaList
    }
    private fun getVideoList(buff: MutableList<Pair<String, MutableList<MediaPickerSourceModel>>>?): MutableList<Pair<String, MutableList<MediaPickerSourceModel>>> {
        val mediaList = buff ?: mutableListOf()
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.DURATION)
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        try {
            this.contentResolver.call(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "releasePersistableUriPermission", null, null)
        } catch (e: Exception) {
            FLog.debug("mhha", "${e.message}")
        }

        val query = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val dateTimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val folderName = cursor.getString(folderColumn)
                val dateTime = cursor.getString(dateTimeColumn)
                val duration = cursor.getLong(durationColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                val findFolder = mediaList.find { x -> x.first == folderName }
                if (findFolder != null) {
                    findFolder.second.add(
                        MediaPickerSourceModel().apply {
                            mediaPath = uri
                            mediaName = name
                            mediaFileType = MediaFileType.VIDEO
                            mediaDateTime = dateTime
                            mediaMimeType = this@MediaPickerActivity.contentResolver.getType(uri) ?: ""
                            this.duration.value = duration
                        }.generateData())
                } else {
                    mediaList.add(Pair(folderName, arrayListOf(
                        MediaPickerSourceModel().apply {
                            mediaPath = uri
                            mediaName = name
                            mediaFileType = MediaFileType.VIDEO
                            mediaDateTime = dateTime
                            mediaMimeType = this@MediaPickerActivity.contentResolver.getType(uri) ?: ""
                            this.duration.value = duration
                        }.generateData())))
                }
            }
        }
        return mediaList
    }
    private fun playerPlay() {
        if (dataContext.mediaPath.value == null) {
            return
        }
        if (dataContext.mediaFileType.value != MediaFileType.VIDEO) {
            playerStop()
            return
        }
        dataContext.videoPath.value = dataContext.mediaPath.value
    }
    private fun playerStop() {
//        val player = binding?.playerView?.player ?: return
//        player.stop()
        dataContext.videoPath.value = null
    }
    private fun playerRelease() {
//        val player = binding?.playerView?.player ?: return
//        player.stop()
//        player.release()
        dataContext.videoPath.value = null
    }
}