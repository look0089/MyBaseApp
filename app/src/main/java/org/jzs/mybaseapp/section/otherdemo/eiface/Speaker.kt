package org.jzs.mybaseapp.section.otherdemo.eiface

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import org.jzs.mybaseapp.common.Applications
import java.util.*

class Speaker constructor(context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech = TextToSpeech(context, this)
    fun speak(content: String): Boolean {
        return if (!textToSpeech.isSpeaking) {
            textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null)
            true
        } else {
            false
        }
    }

    fun stop() {
        this.textToSpeech.stop()
        this.textToSpeech.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result: Int = textToSpeech!!.setLanguage(Locale.CHINESE)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(Applications.context(), "不支持中文", Toast.LENGTH_SHORT).show()
            }
            result = textToSpeech!!.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(Applications.context(), "do not support English", Toast.LENGTH_SHORT).show()
            }
        }
    }
}