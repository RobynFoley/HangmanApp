package controllers

import utils.isValidListIndex

class Dictionary
{
    private var words = ArrayList<String>()

    fun addWord(word: String){
        words.add(word)

    }

    fun listAllWords(): Any =
        if  (words.isEmpty()) "No words stored"
        else for (word in words) {
            println(word)
        }

    fun deleteWord(indexToDelete: Int): String? {
        return if (isValidListIndex(indexToDelete, words)) {
            words.removeAt(indexToDelete)
        } else null
    }

    private fun formatListString(wordsToFormat : List<String>) : String =
        wordsToFormat
            .joinToString (separator = "\n") { word ->
                words.indexOf(word).toString() + ": " + word }





}