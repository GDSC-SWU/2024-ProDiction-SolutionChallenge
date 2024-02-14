package com.example.pro_diction

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioRecord.MetricsConstants.CHANNELS
import android.media.MediaCodecInfo
import android.media.MediaRecorder
import android.os.CountDownTimer
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.experimental.and
import kotlin.experimental.or

class AudioRecordService : Service() {
    private var toast: Toast? = null
    private val RECORDER_SAMPLERATE = 44100
    private val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
    private val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
    private var record: AudioRecord? = null
    private var BufferElements2Rec = 1024
    private val BytesPerElement = 2
    private var recordingThread: Thread? = null
    private var isRecording = false
    private var buffsize = 0


    override fun onBind(intent: Intent): IBinder? {
        // Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // 퍼미션 체크
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 퍼미션이 허용되지 않은 경우 서비스를 시작하지 않음
            Toast.makeText(this, "Record audio permission not granted", Toast.LENGTH_SHORT).show()
            stopSelf()
            return Service.START_NOT_STICKY
        }

        try {

            buffsize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLERATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            record = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                buffsize
            )

            record?.startRecording()

            val countDownTimer: CountDownTimer = object : CountDownTimer(15000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    toast = Toast.makeText(this@AudioRecordService, "Recording", Toast.LENGTH_SHORT)
                    toast?.show()
                    isRecording = true
                    recordingThread = Thread {
                        writeAudioDataToFile()
                    }
                    recordingThread?.start()
                }

                override fun onFinish() {
                    try {
                        toast?.cancel()
                        Toast.makeText(this@AudioRecordService, "Done Recording ", Toast.LENGTH_SHORT).show()
                        isRecording = false
                        record?.stop()
                        record?.release()
                        record = null
                        recordingThread = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            countDownTimer.start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Service.START_STICKY
    }

    private fun short2byte(sData: ShortArray): ByteArray {
        val shortArrsize = sData.size
        val bytes = ByteArray(shortArrsize * 2)
        for (i in 0 until shortArrsize) {
            bytes[i * 2] = (sData[i] and 0x00FF).toByte()
            bytes[i * 2 + 1] = (sData[i].toInt() shr 8).toByte()
            sData[i] = 0
        }
        return bytes
    }

    private fun writeAudioDataToFile() {
        try {
            val extState = Environment.getExternalStorageState()
            val path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC + "/test").absolutePath
            val fileName = "audio.pcm"
            val externalStorage = Environment.getExternalStorageDirectory().absolutePath
            val file = File(externalStorage + File.separator + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            val sData = ShortArray(BufferElements2Rec)
            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            while (isRecording) {
                record?.read(sData, 0, BufferElements2Rec)
                try {
                    val bData = short2byte(sData)
                    os?.write(bData, 0, BufferElements2Rec * BytesPerElement)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            os?.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun wavFileHeader(
        totalAudioLen: Long,
        totalDataLen: Long,
        longSampleRate: Long,
        channels: Int,
        byteRate: Long,
        bitsPerSample: Byte
    ): ByteArray {
        val header = ByteArray(44)
        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16 // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // format = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (channels * (bitsPerSample / 8)).toByte() // block align
        header[33] = 0
        header[34] = bitsPerSample // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        return header
    }
/*
    private fun createAdtsHeader(length: Int): ByteArray {
        val frameLength = length + 7
        val adtsHeader = ByteArray(7)

        adtsHeader[0] = 0xFF.toByte() // Sync Word
        adtsHeader[1] = 0xF1.toByte() // MPEG-4, Layer (0), No CRC
        adtsHeader[2] = ((MediaCodecInfo.CodecProfileLevel.AACObjectLC - 1) shl 6).toByte()
        adtsHeader[2] = (adtsHeader[2] or ((SAMPLE_RATE_INDEX shl 2).toByte())).toByte()
        adtsHeader[2] = (adtsHeader[2] or ((CHANNELS shr 2).toByte())).toByte()
        adtsHeader[3] = (((CHANNELS and 3) shl 6).toByte() or ((frameLength shr 11) and 0x03).toByte())
        adtsHeader[4] = ((frameLength shr 3) and 0xFF).toByte()
        adtsHeader[5] = (((frameLength and 0x07) shl 5).toByte() or 0x1f).toByte()
        adtsHeader[6] = 0xFC.toByte()

        return adtsHeader
    }*/
}