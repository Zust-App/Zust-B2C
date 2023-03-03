package `in`.opening.area.zustapp.helper

object LanguageManager {
    var currentLanguage = LANGUAGE.ENGLISH
    const val HINDI = "hi"
    const val ENG = "en"

    fun setLanguage(key: String) {
        currentLanguage = if (key == HINDI) {
            LANGUAGE.HINDI
        } else {
            LANGUAGE.ENGLISH
        }
    }

    fun getUserSelectedLang(): String {
        return if (currentLanguage == LANGUAGE.HINDI) {
            HINDI
        } else {
            ENG
        }
    }

}

enum class LANGUAGE {
    HINDI, ENGLISH
}