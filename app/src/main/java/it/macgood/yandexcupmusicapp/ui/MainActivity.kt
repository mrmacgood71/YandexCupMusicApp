package it.macgood.yandexcupmusicapp.ui

import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import it.macgood.yandexcupmusicapp.R
import it.macgood.yandexcupmusicapp.domain.model.TrackModel
import it.macgood.yandexcupmusicapp.ui.component.GradientBox
import it.macgood.yandexcupmusicapp.ui.component.GradientBoxEmpty
import it.macgood.yandexcupmusicapp.ui.component.SamplesPickRow
import it.macgood.yandexcupmusicapp.ui.custom.WaveformView
import it.macgood.yandexcupmusicapp.ui.theme.YandexCupMusicAppTheme
import java.io.File

val YGreen = Color(0xFFA8DB10)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YandexCupMusicAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val recordAudioPermission =
                        rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

                    if (recordAudioPermission.status.isGranted) {
                        MainScreen()
                    } else {
                        AlertDialog(
                            text = { Text("Чтобы приложение работало, нужно дать согласие") },
                            onDismissRequest = {
                                recordAudioPermission.launchPermissionRequest()
                            }, confirmButton = {
                                recordAudioPermission.launchPermissionRequest()
                            })
                    }
                }
            }
        }
    }
}

// TODO
//  2. мьютится не сразу
//  5. в целом код говна
@Composable
fun MainScreen(
    musicViewModel: MusicViewModel = hiltViewModel()
) {
    val layersViewIsVisible = remember { mutableStateOf(false) }
    val rotateArrow = remember { mutableStateOf(0f) }
    val context = LocalContext.current
    val sendFileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                Toast.makeText(context, "Файл успешно отправлен!", Toast.LENGTH_SHORT).show()
            }
        }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box {
            SamplesPickRow()
            Spacer(modifier = Modifier.height(64.dp))
            Box(
                modifier = Modifier
                    .zIndex(-1f)
                    .offset(y = (116).dp)
            ) {

                if (!layersViewIsVisible.value) {
                    GradientBox {

                    }

                } else {
                    GradientBoxEmpty {
                        LayersView(visible = layersViewIsVisible.value)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (116).dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (!layersViewIsVisible.value) {
                Spacer(modifier = Modifier.height(4.dp))
                WaveformView(waveform = musicViewModel.waveformState.value)

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    modifier = Modifier
                        .height(48.dp)
                        .width(96.dp)
                        .background(
                            if (layersViewIsVisible.value) YGreen else Color.White,
                            RoundedCornerShape(8.dp)
                        ),
                    onClick = {
                        rotateArrow.value = if (rotateArrow.value == 0f) 180f else 0f
                        layersViewIsVisible.value = !layersViewIsVisible.value
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.layers))
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            modifier = Modifier
                                .size(12.dp)
                                .rotate(rotateArrow.value),
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = null
                        )
                    }
                }
                val state = musicViewModel.playerState.collectAsState().value

                var isRecording by remember { mutableStateOf(false) }
                var isRecordingTrack by remember { mutableStateOf(false) }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        onClick = {
                            if (isRecording) musicViewModel.onRecorderStop() else musicViewModel.onRecorderStart()
                            isRecording = !isRecording
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            tint = if (isRecording) Color.Red else Color.Black,
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = null
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        onClick = {

                            if (isRecordingTrack) {
                                musicViewModel.onRecorderTrackStop()
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.type = "application/*"
                                val file = File(musicViewModel.absolutePath.value)
                                val fileUri = FileProvider.getUriForFile(
                                    context,
                                    "it.macgood.yandexcupmusicapp.fileprovider",
                                    file
                                )
                                intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                                sendFileLauncher.launch(intent)
                            } else {
                                musicViewModel.onRecordStartTrack()
                            }

                            isRecordingTrack = !isRecordingTrack
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            tint = if (isRecordingTrack) Color.Red else Color.Black,
                            painter = painterResource(id = R.drawable.ic_record_red),
                            contentDescription = null
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        onClick = {
                            if (state.isAllPlaying) {
                                musicViewModel.stopAll()
                            } else musicViewModel.playAll()
                            state.isAllPlaying = !state.isAllPlaying
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = if (state.isAllPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LayersView(
    visible: Boolean = false,
    musicViewModel: MusicViewModel = hiltViewModel()
) {
    if (!visible) return

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        musicViewModel.findListOfPickedPlayers().forEach {
            LayerRow(player = it)
        }

        Log.d("TAG", "LayersView: ${musicViewModel.findListOfPickedPlayers()}")

    }
}

@Composable
fun LayerRow(
    player: Pair<MediaPlayer, TrackModel>,
    musicViewModel: MusicViewModel = hiltViewModel()
) {
    var isPlayed by remember {
        mutableStateOf(false)
    }
    var isSoundOn by remember {
        mutableStateOf(true)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(if (isPlayed) YGreen else Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .width(200.dp)
                .padding(start = 8.dp),
            maxLines = 1,
            text = player.second.sampleName
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    isPlayed = !isPlayed
                    if (isPlayed) {
                        musicViewModel.playOneInLayer(player)
                    } else {
                        musicViewModel.stopOne()
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = if (isPlayed) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                    if (isSoundOn) {
                        musicViewModel.setPlayerAsMuted(player)
                    } else {
                        musicViewModel.setPlayerAsUnMuted(player)
                    }
                    isSoundOn = !isSoundOn
                }
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = if (isSoundOn) R.drawable.ic_sound_on else R.drawable.ic_sound_off),
                    contentDescription = null
                )
            }
            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp)),
                onClick = {
                    musicViewModel.setPlayerAsUnpicked(player)
                }
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null
                )
            }
        }
    }
}

