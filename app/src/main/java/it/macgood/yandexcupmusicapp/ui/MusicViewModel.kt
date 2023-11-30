package it.macgood.yandexcupmusicapp.ui

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.PlaybackParams
import android.media.audiofx.Visualizer
import android.net.Uri
import android.util.Log
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.macgood.yandexcupmusicapp.R
import it.macgood.yandexcupmusicapp.di.Size
import it.macgood.yandexcupmusicapp.domain.model.TrackModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val size: Size,
    private val context: Context
) : ViewModel() {

    var sampleMediaPlayer by mutableStateOf(
        MediaPlayer().apply {
            setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.misc_placeholder))
            prepare()
        }
    )
    private val lineMediaPlayer by mutableStateOf(
        MediaPlayer().apply {
            setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.misc_placeholder))
            prepare()
        }
    )
    private val lineMediaPlayer2 by mutableStateOf(MediaPlayer().apply {
        setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.misc_placeholder))
        prepare()
    })
    private val lineMediaPlayer3 by mutableStateOf(MediaPlayer().apply {
        setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.misc_placeholder))
        prepare()
    })
    private val lineMediaPlayer4 by mutableStateOf(MediaPlayer().apply {
        setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.misc_placeholder))
        prepare()
    })
    private val lineMediaPlayer5 by mutableStateOf(MediaPlayer().apply {
        setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.misc_placeholder))
        prepare()
    })

    private var pickedInstruments = mutableStateListOf(
            lineMediaPlayer to TrackModel(R.raw.misc_placeholder, isPicked = false),
            lineMediaPlayer2 to TrackModel(R.raw.misc_placeholder, isPicked = false),
            lineMediaPlayer3 to TrackModel(R.raw.misc_placeholder, isPicked = false),
            lineMediaPlayer4 to TrackModel(R.raw.misc_placeholder, isPicked = false),
            lineMediaPlayer5 to TrackModel(R.raw.misc_placeholder, isPicked = false)
    )

    val waveformState = mutableStateOf(listOf<Float>())

    private val _playerState: MutableStateFlow<PlayerScreenState> = MutableStateFlow(
        PlayerScreenState()
    )
    val playerState: StateFlow<PlayerScreenState> = _playerState.asStateFlow()

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    var curVolume by mutableFloatStateOf(0f)
    private var curSpeed by mutableFloatStateOf(0.5f)
    var maxVolume by mutableFloatStateOf(1f)

    init {
        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    }

    fun findFirstNotPickedPlayer() : Pair<MediaPlayer, TrackModel> {
        return pickedInstruments.find { !it.second.isPicked } ?: pickedInstruments.last()
    }

    fun setPlayerAsPicked(player: Pair<MediaPlayer, TrackModel>, sampleName: String, resId: Int) {
        val indexOf = pickedInstruments.indexOf(player)
        pickedInstruments[indexOf] = player.first to TrackModel(resId, sampleName = sampleName, isPicked = true)
    }
    fun setPlayerAsPickedStr(player: Pair<MediaPlayer, TrackModel>, sampleName: String, resId: String) {
        val indexOf = pickedInstruments.indexOf(player)

        pickedInstruments[indexOf] = player.first to TrackModel(R.raw.misc_placeholder, resId, sampleName = sampleName, isPicked = true)
    }

    fun setPlayerAsUnpicked(player: Pair<MediaPlayer, TrackModel>) {
        val indexOf = pickedInstruments.indexOf(player)
        if (player.second.resStr.isNotEmpty()) {
            pickedInstruments[indexOf] = player.first to TrackModel(player.second.resId, resStr = player.second.resStr, sampleName = player.second.sampleName, isPicked = false)
        } else {
            pickedInstruments[indexOf] = player.first to TrackModel(player.second.resId, sampleName = player.second.sampleName, isPicked = false)
        }
    }

    fun setPlayerAsMuted(player: Pair<MediaPlayer, TrackModel>) {
        val indexOf = pickedInstruments.indexOf(player)
        sampleMediaPlayer.setVolume(0f, 0f)
        if (player.second.resStr.isNotEmpty()) {
            pickedInstruments[indexOf] = player.first to TrackModel(player.second.resId, resStr = player.second.resStr,  sampleName = player.second.sampleName, isMuted = true, isPicked = true)
        } else {
            pickedInstruments[indexOf] = player.first to TrackModel(resId = player.second.resId, sampleName = player.second.sampleName, isMuted = true, isPicked = true)
        }

    }

    fun setPlayerAsUnMuted(player: Pair<MediaPlayer, TrackModel>) {
        val indexOf = pickedInstruments.indexOf(player)
        if (player.second.resStr.isNotEmpty()) {
            pickedInstruments[indexOf] = player.first to TrackModel(player.second.resId, resStr = player.second.resStr,  sampleName = player.second.sampleName, isMuted = false, isPicked = true)
        } else {
            pickedInstruments[indexOf] = player.first to TrackModel(resId = player.second.resId, sampleName = player.second.sampleName, isMuted = false, isPicked = true)
            playOne(player.second.resId)
        }
    }

    fun findListOfPickedPlayers() = pickedInstruments.filter { it.second.isPicked }.toList()

    var playingAll = mutableStateListOf<MediaPlayer>()

    fun playAll() {
        sampleMediaPlayer.pause()
        findListOfPickedPlayers().toList().forEach { pair ->
            if (!pair.second.isMuted) {
                if (pair.second.resStr.isNotEmpty()) {
                    val mediaPlayer = MediaPlayer().apply {
                        setDataSource(pair.second.resStr)
                        prepare()
                    }

                    mediaPlayer.setOnPreparedListener {
                        it.setVolume(curVolume, curVolume)
                        it.playbackParams = PlaybackParams().apply { speed = curSpeed }
                        it.isLooping = true
                        it.start()
                        playingAll.add(mediaPlayer)
                        _playerState.update { it.copy(isPrepared = true, isAllPlaying = true) }
                    }

                    mediaPlayer.setOnCompletionListener {
                        _playerState.update { it.copy(isPrepared = false, isAllPlaying = false) }
                    }
                } else {
                    val mediaPlayer = MediaPlayer().apply {
                        setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + pair.second.resId))
                        prepare()
                    }
                    mediaPlayer.setOnPreparedListener {
                        it.setVolume(curVolume, curVolume)
                        it.playbackParams = PlaybackParams().apply { speed = curSpeed }
                        it.isLooping = true
                        it.start()
                        link(it)
                        playingAll.add(mediaPlayer)
                        _playerState.update { it.copy(isPrepared = true, isAllPlaying = true) }
                    }

                    mediaPlayer.setOnCompletionListener {
                        _playerState.update { it.copy(isPrepared = false, isAllPlaying = false) }
                    }
                }
            }
        }
    }

    fun stopAll() {
        playingAll.forEach {
            it.stop()
            it.release()
        }
        _playerState.update { it.copy(isPrepared = false, isAllPlaying = false) }
        playingAll.clear()
    }

    fun setVolume(newVolume: Float) {
        curVolume = newVolume
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume.toInt(), 0)
    }

    fun setSpeed(speed: Float) {
        if (!_playerState.value.isPrepared) {
            val position = sampleMediaPlayer.currentPosition
            val params = PlaybackParams()
            curSpeed = speed
            params.speed = speed
            sampleMediaPlayer.playbackParams = params
            sampleMediaPlayer.seekTo(position)
        } else {
            playingAll.forEach {
                val position = it.currentPosition
                val params = PlaybackParams()
                curSpeed = speed
                params.speed = speed
                it.playbackParams = params
                it.seekTo(position)
            }
        }
    }

    fun playOne(@RawRes resId: Int) {

        sampleMediaPlayer.stop()
        sampleMediaPlayer.release()

        sampleMediaPlayer = MediaPlayer()
            .apply {
                setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + resId))
                prepare()
            }
        val params = PlaybackParams()
        params.speed = curSpeed
        sampleMediaPlayer.playbackParams = params
        sampleMediaPlayer.setVolume(curVolume, curVolume)
        sampleMediaPlayer.isLooping = false
        sampleMediaPlayer.start()
        link(sampleMediaPlayer)

    }

    fun playOneInLayer(player: Pair<MediaPlayer, TrackModel>) {

        if (!player.second.isMuted) {
            sampleMediaPlayer.stop()
            sampleMediaPlayer.release()

            if (player.second.resStr.isNotEmpty()) {
                sampleMediaPlayer = MediaPlayer()
                    .apply {
                        setDataSource(context, Uri.parse(player.second.resStr))
                        prepare()
                    }
                val params = PlaybackParams()
                params.speed = curSpeed
                sampleMediaPlayer.playbackParams = params

                sampleMediaPlayer.isLooping = true
                sampleMediaPlayer.start()
                link(sampleMediaPlayer)
            } else {
                sampleMediaPlayer = MediaPlayer()
                    .apply {
                        setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + player.second.resId))
                        Log.d("TAG", "playOneInLayer: ${player.second.resId}")
                        prepare()
                    }
                val params = PlaybackParams()
                params.speed = curSpeed
                sampleMediaPlayer.playbackParams = params


                sampleMediaPlayer.isLooping = true
                sampleMediaPlayer.start()

                link(sampleMediaPlayer)
            }
        }
    }

    fun stopOne() {
        sampleMediaPlayer.pause()
    }

    fun onPickInstrument(value: Boolean) {
        _playerState.update { it.copy(onSamplePick = value) }
    }

    private fun link(player: MediaPlayer?) {
        if (player == null) {
            throw NullPointerException("Cannot link to null MediaPlayer")
        }

        val mVisualizer = Visualizer(player.audioSessionId)
        mVisualizer.captureSize = Visualizer.getCaptureSizeRange()[1]

        val captureListener: Visualizer.OnDataCaptureListener =
            object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer, waveform: ByteArray,
                    samplingRate: Int
                ) {
                    waveformState.value = waveform.map { it.toFloat() }
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer, waveform: ByteArray,
                    samplingRate: Int
                ) {
                    waveformState.value = waveform.map { it.toFloat() }
                }
            }
        mVisualizer.setDataCaptureListener(
            captureListener,
            Visualizer.getMaxCaptureRate() / 2, true, true
        )

        mVisualizer.enabled = true
        player.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            mVisualizer.enabled = false
        })
    }

    val absolutePath = mutableStateOf("")
    var recorderPath = mutableStateOf("")
    var mediaRecorder = MediaRecorder()
    var mediaRecorderTrack = MediaRecorder()

    fun onRecorderStart() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        val outputFileName = "${System.currentTimeMillis()}_mic.3gp"
        val outputDir = context.filesDir
        val outputFile = File(outputDir, outputFileName)

        mediaRecorder.setOutputFile(outputFile.absolutePath)
        recorderPath.value = outputFile.absolutePath

        try {
            mediaRecorder.prepare()
            mediaRecorder.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onRecordStartTrack() {
        mediaRecorderTrack = MediaRecorder()
        mediaRecorderTrack.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorderTrack.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorderTrack.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        val outputFileName = "${System.currentTimeMillis()}_mic.3gp"
        val outputDir = context.filesDir
        val outputFile = File(outputDir, outputFileName)

        mediaRecorderTrack.setOutputFile(outputFile.absolutePath)

        try {
            mediaRecorderTrack.prepare()
            mediaRecorderTrack.start()
            absolutePath.value = outputFile.absolutePath

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onRecorderStop() {
        mediaRecorder.stop()
        mediaRecorder.release()

        val player = findFirstNotPickedPlayer()
        setPlayerAsPickedStr(player, "Микрофон ${Random(1L).nextInt()}", recorderPath.value)
//        player.second.resStr = absolutePath.value
        Log.d("TAG", "onRecorderStop: ${player.second.resStr}")
    }
    fun onRecorderTrackStop() {
        mediaRecorderTrack.stop()
        mediaRecorderTrack.release()

    }
}
