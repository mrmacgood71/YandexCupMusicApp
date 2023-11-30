package it.macgood.yandexcupmusicapp.ui

data class PlayerScreenState(
    val onSamplePick: Boolean = false,
    val isPrepared: Boolean = false,
    var isAllPlaying: Boolean = false
) {
}