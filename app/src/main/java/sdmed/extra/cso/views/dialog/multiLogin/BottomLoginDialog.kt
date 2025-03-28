package sdmed.extra.cso.views.dialog.multiLogin

import androidx.compose.runtime.Composable
import sdmed.extra.cso.bases.fBaseScreen

@Composable
fun bottomLoginDialog(data: BottomLoginDialogData) {
    val dataContext = fBaseScreen<BottomLoginDialogVM>({ data, dataContext ->

    }, { dataContext -> })
}