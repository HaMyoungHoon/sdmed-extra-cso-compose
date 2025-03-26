package sdmed.extra.cso.views.dialog.message

import sdmed.extra.cso.interfaces.command.ICommand

data class MessageDialogData(
    var relayCommand: ICommand? = null,
    var title: String = "",
    var leftBtnText: String = "",
    var rightBtnText: String = "",
    var isCancel: Boolean = true,
) {
}