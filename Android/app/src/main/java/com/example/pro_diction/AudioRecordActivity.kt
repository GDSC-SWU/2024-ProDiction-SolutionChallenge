package com.example.pro_diction

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.pro_diction.data.AiApiPool
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class AudioRecordActivity : AppCompatActivity() {
    private val RECORDER_BPP = 16
    private val AUDIO_RECORDER_FILE_EXT_WAV = ".wav"
    private val AUDIO_RECORDER_FOLDER = "AudioRecorder"
    private val AUDIO_RECORDER_TEMP_FILE = "record_temp.raw"
    private val RECORDER_SAMPLERATE = 44100
    private val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO
    private val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
    private var audioData: ShortArray? = null
    private var recorder: AudioRecord? = null
    private var bufferSize = 0
    private var recordingThread: Thread? = null
    private var isRecording = false

    val postSttSyllable = AiApiPool.sttSyllables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)
        setButtonHandlers()
        enableButtons(false)
        bufferSize = AudioRecord.getMinBufferSize(
            RECORDER_SAMPLERATE,
            RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING
        ) * 3
        audioData = ShortArray(bufferSize)
    }

    private fun setButtonHandlers() {
        findViewById<Button>(R.id.btnStart).setOnClickListener(btnClick)
        findViewById<Button>(R.id.btnStop).setOnClickListener(btnClick)
    }

    private fun enableButton(id: Int, isEnable: Boolean) {
        findViewById<Button>(id).isEnabled = isEnable
    }

    private fun enableButtons(isRecording: Boolean) {
        enableButton(R.id.btnStart, !isRecording)
        enableButton(R.id.btnStop, isRecording)
    }

    private fun getFilename(): String {
        val filepath = Environment.getExternalStorageDirectory().path
        val file = File(filepath, AUDIO_RECORDER_FOLDER)
        if (!file.exists()) {
            file.mkdirs()
        }
        return (file.absolutePath + "/" + System.currentTimeMillis() +
                AUDIO_RECORDER_FILE_EXT_WAV)
    }

    private fun getTempFilename(): String {
        val filepath = Environment.getExternalStorageDirectory().path
        val file = File(filepath, AUDIO_RECORDER_FOLDER)
        if (!file.exists()) {
            file.mkdirs()
        }
        val tempFile = File(filepath, AUDIO_RECORDER_TEMP_FILE)
        if (tempFile.exists()) tempFile.delete()
        return (file.absolutePath + "/" + AUDIO_RECORDER_TEMP_FILE)
    }

    private fun startRecording() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLERATE,
            RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING,
            bufferSize
        )
        val i = recorder!!.state
        if (i == 1) recorder!!.startRecording()
        isRecording = true
        recordingThread = Thread(Runnable { writeAudioDataToFile() }, "AudioRecorder Thread")
        recordingThread!!.start()
    }

    private fun writeAudioDataToFile() {
        val data = ByteArray(bufferSize)
        val filename = getTempFilename()
        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(filename)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        var read: Int
        if (os != null) {
            while (isRecording) {
                read = recorder!!.read(data, 0, bufferSize)
                if (read > 0) {
                }
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            try {
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        if (recorder != null) {
            isRecording = false
            val i = recorder!!.state
            if (i == 1) recorder!!.stop()
            recorder!!.release()
            recorder = null
            recordingThread = null
        }
        copyWaveFile(getTempFilename(), getFilename())
        deleteTempFile()
    }

    private fun deleteTempFile() {
        val file = File(getTempFilename())
        file.delete()
    }

    private fun copyWaveFile(inFilename: String, outFilename: String) {
        var `in`: FileInputStream? = null
        var out: FileOutputStream? = null
        var totalAudioLen: Long = 0
        val totalDataLen: Long
        val longSampleRate = RECORDER_SAMPLERATE.toLong()
        val channels = 2
        val byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8.toLong()
        val data = ByteArray(bufferSize)
        try {
            `in` = FileInputStream(inFilename)
            out = FileOutputStream(outFilename)
            totalAudioLen = `in`.channel.size()
            totalDataLen = totalAudioLen + 36
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate)
            while (`in`.read(data) != -1) {
                out.write(data)
            }
            `in`.close()
            out.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun WriteWaveFileHeader(
        out: FileOutputStream, totalAudioLen: Long, totalDataLen: Long, longSampleRate: Long,
        channels: Int, byteRate: Long
    ) {
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
        header[32] = (2 * 16 / 8).toByte() // block align
        header[33] = 0
        header[34] = RECORDER_BPP.toByte() // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        out.write(header, 0, 44)
    }

    private val btnClick = View.OnClickListener { v ->
        when (v.id) {
            R.id.btnStart -> {
                Toast.makeText(this, "Start Recording", Toast.LENGTH_SHORT)
                enableButtons(true)
                startRecording()
            }
            R.id.btnStop -> {
                Toast.makeText(this, "Stop Recording", Toast.LENGTH_SHORT)
                enableButtons(false)
                stopRecording()


                val fileN = getFilename()
                Log.e("fileN", fileN)
                val file = File(fileN)
                Log.e("file", file.toString())

                val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
                val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
                val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

                postSttSyllable.postSttSyllable(fileToUpload ).enqueue(object: Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response != null) {
                            if (response.isSuccessful) {
                                Log.e("response.isSuccessful   response.body()", response.toString())
                                Log.e("response.isSuccessful   response.body()", response.body().toString())
                            } else {
                                Log.e("response.isNotSuccessful   response.body()", response.toString())
                            }
                        } else {
                            response.body()?.let { Log.e("recoed     Response", it.toString()) }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        t.message?.let { Log.e("recoed     error", it) } ?: "서버통신 실패(응답값 x)"
                    }
                })
            }
        }
    }

}