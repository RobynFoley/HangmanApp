import controllers.AdminAPI
import controllers.Dictionary
import controllers.Game
import controllers.PlayerAPI
import models.Admin
import models.Player
import persistence.JSONSerializer
import utils.readNextInt
import utils.readNextLine
import java.io.File
import java.lang.System.exit


//private val dictionary = Dictionary(XMLSerializer(File("dictionary.xml")))
private val dictionary = Dictionary(JSONSerializer(File("dictionary.json")))
private val playerAPI = PlayerAPI(JSONSerializer(File("players.json")))
private val adminAPI = AdminAPI(JSONSerializer(File("admins.json")))
private val game = Game()
private var currentPlayer: Player? = null

fun main(){
    load()

    dictionaryStartUp()
    switchPlayer()
    runMenu()

}

fun mainMenu(): Int {
    print(""" 
         > ----------------------------------
         > |           HANGMAN              |
         > ----------------------------------
         > |  MENU                          |              PLAYER: ${currentPlayer?.playerName}
         > |   1) Play                      |              ${currentPlayer?.winCount} wins
         > |   2) Switch Player             |              ${currentPlayer?.gamesPlayed} games played
         > |   3) View Players              |                  
         > |   4) Add Player                | 
         > |   5) Best Players              |
         > |                                |                   
         > |   6)Admin Menu                 |                      
         > ----------------------------------
         > |   0) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">"))
    return readNextInt(" > ==>>")
}

fun adminMenu(): Int {
   print( """ 
         > ----------------------------------
         > |           Admin Menu           |
         > ----------------------------------
         > |  MENU                          |
         > |   1) Add admin                 |
         > |   2) Delete admin              |
         > |   3) Change Password           |                
         > |   4) Delete Player             |                      
         > |   5) Update Player             |                       
         > |   6) Word Dictionary           |
         > |   7) Update Dictionary         |
         > |                                |
         > ----------------------------------
         > |   0) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">"))
    return readNextInt(" > ==>>")
}

fun isAdmin(){

    if(adminAPI.isAdmin(currentPlayer)){
      val password = readNextLine("Enter Password:")
        if(password == adminAPI.getPasswordByPlayer(currentPlayer)) {
            runAdminMenu()
        }
    }else if(adminAPI.numberOfAdmins() == 0){
            val password = readNextLine("Enter Password:")
            if(password == "admin"){
                runAdminMenu()
            }
        }
}

fun switchPlayer(){
    if(playerAPI.numberOfPlayers() > 0){
    println("Enter index of your player")
    println(playerAPI.listPlayers())
    println()
    println("enter -1 to add new player")

        val choice = readNextInt(" > ==>>")
        currentPlayer = if (choice == -1){
            val name = readNextLine("Enter Player Name:  ")
            playerAPI.add(Player(name, 0, 0))
            val index = (playerAPI.numberOfPlayers()-1)
            playerAPI.getPlayer(index)
        }else{
            playerAPI.getPlayer(choice)
        }

    }else{
        val name = readNextLine("Enter Player Name:  ")
        playerAPI.add(Player(name, 0, 0))
        currentPlayer = playerAPI.getPlayer(0)
    }
}


fun runMenu() {
    do{
        val option = mainMenu()
        when (option) {
            1 -> play()
            2 -> switchPlayer()
            3 -> viewPlayer()
            4 -> addPlayer()
            5 -> playerAPI.listBestPlayers()
            6 -> isAdmin()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun runAdminMenu() {
    do{
        val option = adminMenu()
        when (option) {
            1 -> addAdmin()
            2 -> deleteAdmin()
            3 -> updatePassword()
            4 -> deletePlayer()
            5 -> updatePlayer()
            6 -> wordDictionary()
            7 -> updateDictionary()
            0 -> runMenu()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun addAdmin(){
    println(playerAPI.listPlayers())
    if (playerAPI.numberOfPlayers() > 0) {
        val index = readNextInt("Enter the index of the player you want to make admin: ")
        if(playerAPI.isValidIndex(index)){

          if(  adminAPI.add(Admin(playerAPI.getPlayer(index), "admin"))){
              println("A new admin was added")
              Thread.sleep(250)
              println("Default password is 'admin'")
          }
        }


    }

}

fun deleteAdmin(){
    println(adminAPI.listAdmins())

    if (adminAPI.numberOfAdmins() > 0) {

        val indexToDelete = readNextInt("Enter the index of the admin to delete: ")

        val playerToDelete = adminAPI.removeAdmin(indexToDelete)
        if (playerToDelete != null) {
            println("Delete Successful! ")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun updatePassword(){
    val currentPassword = readNextLine("Enter Current Password: ")
    if (currentPassword == adminAPI.getPasswordByPlayer(currentPlayer)){
        val newPassword = readNextLine("Enter New Password: ")
        val confirmPassword = readNextLine("Retype New Password: ")
        if (confirmPassword == newPassword){
        adminAPI.updatePasswordByPlayer(currentPlayer, newPassword)}else{
            println("Passwords do not match")
        }
    }
}

fun play(){
    val index = (0..(dictionary.numberOfWords()-1)).random()
    val word = dictionary.getWord(index)
    val win = game.run(word)
    if (win) playerAPI.increaseWinCount(currentPlayer)

    playerAPI.increaseGameCount(currentPlayer)

}

fun viewPlayer() {
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
        "lantern", "compass", "galaxy", "cathedral", "treacherous", "voyage", "island",
        "fortress", "mystery", "enchanted", "phantom", "treasure", "crystal", "adventure",
        "shadow", "labyrinth", "voyager", "serpent", "storm", "meadow", "oracle",
        "cavern", "oasis", "expedition", "artifact", "quicksand", "sanctuary",
        "wildfire", "guardian", "monsoon", "whirlpool", "beacon", "wilderness",
        "paradise", "armada", "nebula", "harbinger", "rift", "twilight", "cyclone",
        "aurora", "abyss", "labyrinth", "corsair", "tempest", "bastion", "expanse",
        "citadel", "artifact", "quasar", "horizon", "obsidian", "leviathan", "zephyr",
        "cascade", "ember", "raven", "eclipse", "mirage", "summit", "evergreen",
        "embers", "shimmer", "grotto", "vortex", "hollow", "specter", "glacier",
        "harbor", "solstice", "tundra", "crescent", "serenity", "traverse", "voyager",
        "labyrinthine", "thistle", "spirit", "expedition", "chimera", "nebulae")
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

            try {
                adminAPI.store()
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

            try {
                adminAPI.load()
            } catch (e: Exception) {
                System.err.println("Error reading from file: $e")
            }
        }