package controllers

import persistence.Serializer
import utils.isValidListIndex

class Dictionary(serializerType: Serializer)
{
    private var serializer: Serializer = serializerType
    private var words = ArrayList<String>()


    @Throws(Exception::class)
    fun load() {
        words = serializer.read() as ArrayList<String>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(words)
    }


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