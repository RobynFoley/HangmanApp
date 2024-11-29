import controllers.Dictionary
import controllers.Game
import controllers.PlayerAPI
import models.Player
import persistence.JSONSerializer
import utils.readNextInt
import utils.readNextLine
import java.io.File
import java.lang.System.exit


//private val dictionary = Dictionary(XMLSerializer(File("dictionary.xml")))
private val dictionary = Dictionary(JSONSerializer(File("dictionary.json")))
private val playerAPI = PlayerAPI(JSONSerializer(File("players.json")))
private val game = Game()

fun main(){
    load()
    dictionaryStartUp()
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
    val index = (0..(dictionary.numberOfWords()-1)).random()
    val word = dictionary.getWord(index)
    game.run(word)

}

fun viewPlayer(){
println(playerAPI.listPlayers())
}

fun addPlayer(){
    val name = readNextLine("Enter Player Name:  ")

    val isAdded = playerAPI.add(Player(name, 0, 0))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
    save()
}

fun updatePlayer(){
    println(playerAPI.listPlayers())

    if (playerAPI.numberOfPlayers() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the player to update: ")
        if (playerAPI.isValidIndex(indexToUpdate)) {
            val name = readNextLine("Enter new player name: ")
            playerAPI.updatePlayer(indexToUpdate, name)
        } else {
            println("Invalid index")
        }
    }
    save()
    }

    fun deletePlayer(){
        println(playerAPI.listPlayers())

        if (playerAPI.numberOfPlayers() > 0) {
            //only ask the user to choose the player to delete if player exist
            val indexToDelete = readNextInt("Enter the index of the player to delete: ")

            val playerToDelete = playerAPI.removePlayer(indexToDelete)
            if (playerToDelete != null) {
                println("Delete Successful! Deleted Player: ${playerToDelete.playerName}")
            } else {
                println("Delete NOT Successful")
            }
        }
        }

        fun wordDictionary(){
            dictionary.listAllWords()
        }

        fun updateDictionary(){
            print(""" 
         > ----------------------------------
         > |   Do you want to               |
         > |   1) Add word                  |
         > |   2) Update word               |
         > |   3) Delete word               |
         > ---------------------------------- 
         >""".trimMargin(">"))
            val response = readNextInt(" > ==>>")

            if(response == 1){
                val word = readNextLine("Enter a word: ")
                dictionary.addWord(word)
                save()
            }else if(response == 2){
                dictionary.listAllWords()
                val index = readNextInt("Enter the index of the word you want to update: ")
                val updatedWord = readNextLine("Enter the new word: ")
                dictionary.updateWord(index, updatedWord)}
                else if (response == 3){
                dictionary.listAllWords()
                val index = readNextInt("Enter the index of the word you want to delete: ")
                dictionary.deleteWord(index)
                }else{println("Invalid response")}



            }


fun dictionaryStartUp(){
    val words = arrayOf("manifest", "puzzle", "pirate", "desert", "journey", "thunder", "library",
        "lantern", "compass", "galaxy", "cathedral", "treacherous")
    for (word in words) {
        dictionary.addWord(word!!)
    }

}

        fun exitApp() {
            save()
            println("Exiting...bye")
            exit(0)
        }

        fun save() {
            try {
                dictionary.store()
            } catch (e: Exception) {
                System.err.println("Error writing to file: $e")
            }

            try {
                playerAPI.store()
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

            try {
                playerAPI.load()
            } catch (e: Exception) {
                System.err.println("Error reading from file: $e")
            }
        }