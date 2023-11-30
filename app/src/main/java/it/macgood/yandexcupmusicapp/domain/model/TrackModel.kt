package it.macgood.yandexcupmusicapp.domain.model

data class TrackModel (
    val resId: Int,
    var resStr: String = "",
    val sampleName: String = "Ударные 1",
    var isMuted: Boolean = false,
    val isPicked: Boolean = false
){
}