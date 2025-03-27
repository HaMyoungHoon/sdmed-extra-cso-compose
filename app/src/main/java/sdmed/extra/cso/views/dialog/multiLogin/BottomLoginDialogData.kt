package sdmed.extra.cso.views.dialog.multiLogin

import sdmed.extra.cso.interfaces.command.ICommand

data class BottomLoginDialogData(
    var relayCommand: ICommand? = null,
    var isAddVisible: Boolean = true
) {
}