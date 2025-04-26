package com.moneyreader

import android.annotation.SuppressLint
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.regex.Pattern

class NotificationListener : NotificationListenerService(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private var keywords = mutableSetOf<String>()
    private var regexPatterns = mutableSetOf<String>()

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
        loadPreferences()
    }

    private fun loadPreferences() {
        keywords = PreferenceManager.getKeywords(this)
        regexPatterns = PreferenceManager.getRegexPatterns(this)

        if (regexPatterns.isEmpty()) {
            // Fallback nếu user chưa add regex nào
            regexPatterns.add("([+-]?[0-9]{1,3}(?:,[0-9]{3})*)")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("vi", "VN")
        }
    }

    @SuppressLint("NewApi")
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName ?: ""
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        if (keywords.any { keyword -> packageName.contains(keyword, ignoreCase = true) || title.contains(keyword, ignoreCase = true) }) {
            val amount = extractAmount(text)
            amount?.let {
                speakAmount(it)
            }
        }
    }

    private fun extractAmount(text: String): Int? {
        regexPatterns.forEach { patternString ->
            val pattern = Pattern.compile(patternString)
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                return matcher.group(1)?.replace(",", "")?.toIntOrNull()
            }
        }
        return null
    }

    private fun speakAmount(amount: Int) {
        val message = "Số tiền giao dịch là ${amount} đồng"
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

override fun onCreate() {
    super.onCreate()
    tts = TextToSpeech(this, this)
    loadPreferences()
    startForegroundService()
}

private fun startForegroundService() {
    val channelId = "money_reader_channel"
    val channelName = "Money Reader Notifications"
    
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val chan = android.app.NotificationChannel(channelId, channelName, android.app.NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(android.app.NotificationManager::class.java)
        manager.createNotificationChannel(chan)
    }

    val notification = androidx.core.app.NotificationCompat.Builder(this, channelId)
        .setContentTitle("MoneyReader đang chạy")
        .setContentText("Đang lắng nghe thông báo...")
        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
        .setPriority(androidx.core.app.NotificationCompat.PRIORITY_LOW)
        .build()

    startForeground(1, notification)
}
override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
        tts.language = Locale("vi", "VN")
        tts.setSpeechRate(PreferenceManager.getSpeechRate(this))
        setVoiceGender()
    }
}

private fun setVoiceGender() {
    val type = PreferenceManager.getVoiceType(this)
    val voices = tts.voices
    voices?.let {
        val selected = it.find { voice ->
            (type == "male" && voice.name.contains("male", true)) ||
            (type == "female" && voice.name.contains("female", true))
        }
        selected?.let { tts.voice = it }
    }
}
}