package app.igormatos.botaprarodar.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.Exception

object EditTextFormatMask {
    const val FORMAT_CPF = "###.###.###-##"
    const val FORMAT_FONE = "(###)####-#####"
    const val FORMAT_CEP = "#####-###"
    const val FORMAT_DATE = "##/##/####"
    const val FORMAT_HOUR = "##:##"

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return
     */
    fun textMask(ediTxt: EditText, mask: String): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var oldInputTextUnmask = ""
            var oldInputText = ""

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val inputTextUnmask = textUnmask(s.toString())
                var textMask = ""

                if (isUpdating) {
                    oldInputTextUnmask = inputTextUnmask
                    oldInputText = s.toString()
                    isUpdating = false
                    return
                }
                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && inputTextUnmask.length > oldInputTextUnmask.length) {
                        textMask += m
                        continue
                    }
                    textMask += try {
                        inputTextUnmask[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }
                if ( s.length <= oldInputText.length){
                    textMask = oldInputText.removeRange(oldInputText.length-1, oldInputText.length)
                }
                isUpdating = true
                ediTxt.setText(textMask)
                ediTxt.setSelection(textMask.length)
            }

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        }
    }

    fun textUnmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }
}