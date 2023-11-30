package it.macgood.yandexcupmusicapp.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.macgood.yandexcupmusicapp.ui.MusicViewModel
import it.macgood.yandexcupmusicapp.R
import it.macgood.yandexcupmusicapp.ui.custom.DropdownButtonBox

@Composable
fun SamplesPickRow(
    musicViewModel: MusicViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DropdownButtonBox(
            drawableId = R.drawable.ic_guitar,
            stringId = R.string.guitar,
            sampleNames = listOf("Гитара 1", "Гитара 2", "Гитара 3"),
            rawIds = listOf(R.raw.misc_guitar_1, R.raw.misc_guitar_2, R.raw.misc_guitar_3)
        ) { resId, sampleName ->
            Log.d("TAG", "SamplesPickRow: $sampleName")
            Log.d("TAG", "SamplesPickRow: $resId")

                when(sampleName) {
                    "Гитара 1" -> {
                        val player = musicViewModel.findFirstNotPickedPlayer()
                        musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_guitar_1)
                        musicViewModel.playOne(R.raw.misc_guitar_1)
                    }
                    "Гитара 2" -> {
                        val player = musicViewModel.findFirstNotPickedPlayer()
                        musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_guitar_2)
                        musicViewModel.playOne(R.raw.misc_guitar_2)
                    }
                    "Гитара 3" -> {
                        val player = musicViewModel.findFirstNotPickedPlayer()
                        musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_guitar_3)
                        musicViewModel.playOne(R.raw.misc_guitar_3)
                    }
                    else -> {
                        musicViewModel.playOne(R.raw.misc_guitar_1)
                    }
                }
        }

        DropdownButtonBox(
            drawableId = R.drawable.ic_drums,
            stringId = R.string.drums,
            sampleNames = listOf("Ударные 1", "Ударные 2", "Ударные 3"),
            rawIds = listOf(R.raw.misc_kick_1, R.raw.misc_kick_2, R.raw.misc_kick_3)
        ) { resId, sampleName ->
            Log.d("TAG", "SamplesPickRow: $sampleName")
            Log.d("TAG", "SamplesPickRow: $resId")
            when(sampleName) {
                "Ударные 1" -> {
                    val player = musicViewModel.findFirstNotPickedPlayer()
                    musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_kick_1)
                    musicViewModel.playOne(R.raw.misc_kick_1)
                }
                "Ударные 2" -> {
                    val player = musicViewModel.findFirstNotPickedPlayer()
                    musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_kick_2)
                    musicViewModel.playOne(R.raw.misc_kick_2)
                }
                "Ударные 3" -> {
                    val player = musicViewModel.findFirstNotPickedPlayer()
                    musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_kick_3)
                    musicViewModel.playOne(R.raw.misc_kick_3)
                }
                else -> {
                    musicViewModel.playOne(R.raw.misc_kick_1)
                }
            }
        }

        DropdownButtonBox(
            drawableId = R.drawable.ic_horn,
            stringId = R.string.horns,
            sampleNames = listOf("Духовые 1", "Духовые 2", "Духовые 3"),
            rawIds = listOf(R.raw.misc_horn_1, R.raw.misc_horn_2, R.raw.misc_horn_3)
        ) { resId, sampleName ->
            Log.d("TAG", "SamplesPickRow: $sampleName")
            Log.d("TAG", "SamplesPickRow: $resId")
            when(sampleName) {
                "Духовые 1" -> {
                    val player = musicViewModel.findFirstNotPickedPlayer()
                    musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_horn_1)
                    musicViewModel.playOne(R.raw.misc_horn_1)
                }
                "Духовые 2" -> {
                    val player = musicViewModel.findFirstNotPickedPlayer()
                    musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_horn_2)
                    musicViewModel.playOne(R.raw.misc_horn_2)
                }
                "Духовые 3" -> {
                    val player = musicViewModel.findFirstNotPickedPlayer()
                    musicViewModel.setPlayerAsPicked(player, sampleName, R.raw.misc_horn_3)
                    musicViewModel.playOne(R.raw.misc_horn_3)
                }
                else -> {
                    musicViewModel.playOne(R.raw.misc_horn_1)
                }
            }
        }

//        DisposableEffect(Unit) {
//            onDispose { mediaPlayer.release() }
//            onDispose { mediaPlayer.release() }
//            onDispose { mediaPlayer.release() }
//        }
    }
}