import utils.readNextInt
import utils.readNextLine
import java.lang.System.exit
import controllers.Dictionary


val dictionary = Dictionary()
fun main(){
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
}

fun exitApp() {
    println("Exiting...bye")
    exit(0)
}