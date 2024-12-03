package controllers

import persistence.Serializer
import utils.isValidListIndex
import java.util.Locale


import kotlin.collections.ArrayList

class Dictionary(serializerType: Serializer)
{
    private var serializer: Serializer = serializerType
    private var words = ArrayList<String>()


    fun getWord(index: Int): String{
        return words[index]
    }

    fun updateWord(index: Int, word: String){
        words[index] = word.lowercase()
    }

    @Throws(Exception::class)
    fun load() {
        words = serializer.read() as ArrayList<String>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(words)
    }


    fun addWord(word: String){
        if (!words.contains(word.lowercase(Locale.getDefault()))){
        words.add(word.lowercase(Locale.getDefault()))}

    }


//https://stackoverflow.com/questions/37609071/array-list-iteration-without-extra-object-allocations
// I took the for loop from this website
    fun listAllWords() : Any =

        if (words.isEmpty()) "No words stored"
        else for (i in words.indices) {
            println("$i:  ${words[i]}")
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


    fun numberOfWords() = words.size


}