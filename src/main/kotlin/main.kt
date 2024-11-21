import utils.readNextInt
import utils.readNextLine
import java.lang.System.exit
import controllers.Dictionary
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File


//private val dictionary = Dictionary(XMLSerializer(File("dictionary.xml")))
private val dictionary = Dictionary(JSONSerializer(File("dictionary.json")))
fun main(){
   load()
runMenu()


}

fun mainMenu(): Int {
    print(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Play                      |
         > |   2) View Player               |                  
         > |   3) Add Player                |
         > |   4) Update Player             |
         > |   5) Delete Player             |
         > |   6) Word Dictionary           |
         > |   7) Update Dictionary         |
         > |                                |
         > ----------------------------------
         > |   0) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">"))
    return readNextInt(" > ==>>")
}

fun runMenu() {
    do{
        val option = mainMenu()
        when (option) {
            1 -> play()
            2 -> viewPlayer()
            3 -> addPlayer()
            4 -> updatePlayer()
            5 -> deletePlayer()
            6 -> wordDictionary()
            7 -> updateDictionary()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
        } while (true)
}

fun play(){

}

fun viewPlayer(){

}

fun addPlayer(){

}

fun updatePlayer(){

}

fun deletePlayer(){

}

fun wordDictionary(){
dictionary.listAllWords()
}

fun updateDictionary(){
val word = readNextLine("Enter a word: ")
dictionary.addWord(word)
    save()
}

fun exitApp() {
    println("Exiting...bye")
    exit(0)
}

fun save() {
    try {
        dictionary.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        dictionary.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}