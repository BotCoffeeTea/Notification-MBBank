package com.moneyreader

import android.content.Context
import android.os.Environment
import android.widget.Toast
import org.json.JSONObject
import java.io.File

object ExportImportManager {

    private const val BACKUP_FILE_NAME = "MoneyReaderBackup.json"

    fun exportData(context: Context) {
        try {
            val keywords = PreferenceManager.getKeywords(context)
            val regexPatterns = PreferenceManager.getRegexPatterns(context)

            val json = JSONObject().apply {
                put("keywords", keywords.toTypedArray())
                put("regexPatterns", regexPatterns.toTypedArray())
            }

            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, BACKUP_FILE_NAME)
            file.writeText(json.toString(4)) // Pretty Print

            Toast.makeText(context, "Đã lưu cấu hình tại Downloads/$BACKUP_FILE_NAME", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Xuất thất bại: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun importData(context: Context) {
        try {
            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, BACKUP_FILE_NAME)
            if (!file.exists()) {
                Toast.makeText(context, "Không tìm thấy file Backup!", Toast.LENGTH_SHORT).show()
                return
            }

            val content = file.readText()
            val json = JSONObject(content)

            val keywords = mutableSetOf<String>()
            val regexPatterns = mutableSetOf<String>()

            json.getJSONArray("keywords").let { arr ->
                for (i in 0 until arr.length()) {
                    keywords.add(arr.getString(i))
                }
            }

            json.getJSONArray("regexPatterns").let { arr ->
                for (i in 0 until arr.length()) {
                    regexPatterns.add(arr.getString(i))
                }
            }

            PreferenceManager.saveKeywords(context, keywords)
            PreferenceManager.saveRegexPatterns(context, regexPatterns)

            Toast.makeText(context, "Khôi phục thành công!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Khôi phục thất bại: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}