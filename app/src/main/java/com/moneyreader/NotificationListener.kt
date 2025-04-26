package com.moneyreader

import android.annotation.SuppressLint
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.regex.Pattern

class NotificationListener : NotificationListenerService(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("vi", "VN")
        }
    }

    @SuppressLint("NewApi")
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        if (packageName.contains("mbbank", true) || title.contains("MBBank", true)) {
            val amount = extractAmount(text)
            amount?.let {
                speakAmount(it)
            }
        }
    }

    private fun extractAmount(text: String): Int? {
        val pattern = Pattern.compile("([+-]?[0-9]{1,3}(?:,[0-9]{3})*)")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            matcher.group(1)?.replace(",", "")?.toIntOrNull()
        } else null
    }

    private fun speakAmount(amount: Int) {
        val message = "Số tiền giao dịch là ${amount} đồng"
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}