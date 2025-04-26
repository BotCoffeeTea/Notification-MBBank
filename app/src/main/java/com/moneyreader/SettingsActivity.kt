package com.moneyreader

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moneyreader.adapter.KeywordAdapter
import com.moneyreader.adapter.RegexAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var keywordAdapter: KeywordAdapter
    private lateinit var regexAdapter: RegexAdapter

    private var keywords = mutableListOf<String>()
    private var regexPatterns = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        keywords = PreferenceManager.getKeywords(this).toMutableList()
        regexPatterns = PreferenceManager.getRegexPatterns(this).toMutableList()

        keywordAdapter = KeywordAdapter(keywords) { editKeyword(it) }
        regexAdapter = RegexAdapter(regexPatterns) { editRegex(it) }

        findViewById<RecyclerView>(R.id.rvKeywords).apply {
            adapter = keywordAdapter
            layoutManager = LinearLayoutManager(this@SettingsActivity)
        }

        findViewById<RecyclerView>(R.id.rvRegex).apply {
            adapter = regexAdapter
            layoutManager = LinearLayoutManager(this@SettingsActivity)
        }

        findViewById<android.widget.Button>(R.id.btnAddKeyword).setOnClickListener {
            showInputDialog(true)
        }

        findViewById<android.widget.Button>(R.id.btnAddRegex).setOnClickListener {
            showInputDialog(false)
        }

        setupSwipe()
    }

    private fun setupSwipe() {
        val keywordTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                keywords.removeAt(vh.adapterPosition)
                PreferenceManager.saveKeywords(this@SettingsActivity, keywords.toSet())
                keywordAdapter.updateData(keywords)
            }
        })

        val regexTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                regexPatterns.removeAt(vh.adapterPosition)
                PreferenceManager.saveRegexPatterns(this@SettingsActivity, regexPatterns.toSet())
                regexAdapter.updateData(regexPatterns)
            }
        })

        keywordTouchHelper.attachToRecyclerView(findViewById(R.id.rvKeywords))
        regexTouchHelper.attachToRecyclerView(findViewById(R.id.rvRegex))
    }

    private fun showInputDialog(isKeyword: Boolean) {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(if (isKeyword) "Thêm Từ Khoá" else "Thêm Regex")
            .setView(editText)
            .setPositiveButton("Lưu") { _, _ ->
                val text = editText.text.toString()
                if (text.isNotEmpty()) {
                    if (isKeyword) {
                        keywords.add(text)
                        PreferenceManager.saveKeywords(this, keywords.toSet())
                        keywordAdapter.updateData(keywords)
                    } else {
                        regexPatterns.add(text)
                        PreferenceManager.saveRegexPatterns(this, regexPatterns.toSet())
                        regexAdapter.updateData(regexPatterns)
                    }
                }
            }
            .setNegativeButton("Huỷ", null)
            .show()
    }

    private fun editKeyword(position: Int) {
        val editText = EditText(this)
        editText.setText(keywords[position])
        AlertDialog.Builder(this)
            .setTitle("Sửa Từ Khoá")
            .setView(editText)
            .setPositiveButton("Cập nhật") { _, _ ->
                val newKeyword = editText.text.toString()
                keywords[position] = newKeyword
                PreferenceManager.saveKeywords(this, keywords.toSet())
                keywordAdapter.updateData(keywords)
            }
            .setNegativeButton("Huỷ", null)
            .show()
    }

    private fun editRegex(position: Int) {
        val editText = EditText(this)
        editText.setText(regexPatterns[position])
        AlertDialog.Builder(this)
            .setTitle("Sửa Regex")
            .setView(editText)
            .setPositiveButton("Cập nhật") { _, _ ->
                val newPattern = editText.text.toString()
                regexPatterns[position] = newPattern
                PreferenceManager.saveRegexPatterns(this, regexPatterns.toSet())
                regexAdapter.updateData(regexPatterns)
            }
            .setNegativeButton("Huỷ", null)
            .show()
    }

}override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.settings_menu, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.menu_export -> ExportImportManager.exportData(this)
        R.id.menu_import -> ExportImportManager.importData(this)
        R.id.menu_voice -> showVoiceSettingsDialog()
    }
    return super.onOptionsItemSelected(item)
}

private fun showVoiceSettingsDialog() {
    val options = arrayOf("Nam", "Nữ")
    val speeds = arrayOf("0.5x", "1.0x", "1.5x")

    val builder = AlertDialog.Builder(this)
    builder.setTitle("Chọn giọng đọc")
    builder.setSingleChoiceItems(options, -1) { dialog, which ->
        val type = if (which == 0) "male" else "female"
        PreferenceManager.saveVoiceType(this, type)
        dialog.dismiss()
    }
    builder.setNeutralButton("Chọn tốc độ") { dialog, _ ->
        showSpeedDialog()
    }
    builder.show()
}

private fun showSpeedDialog() {
    val speeds = arrayOf("0.5x", "1.0x", "1.5x")
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Chọn tốc độ đọc")
    builder.setSingleChoiceItems(speeds, -1) { dialog, which ->
        val rate = when (which) {
            0 -> 0.5f
            1 -> 1.0f
            2 -> 1.5f
            else -> 1.0f
        }
        PreferenceManager.saveSpeechRate(this, rate)
        dialog.dismiss()
    }
    builder.show()
}