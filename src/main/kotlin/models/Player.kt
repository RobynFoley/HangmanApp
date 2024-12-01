package models

data class Player (
 var playerName: String,
    var winCount: Int,
    var gamesPlayed: Int
){

    override fun toString(): String {
        return """NAME:${playerName.toUpperCase()}     WINS: $winCount TOTAL GAMES: $gamesPlayed""".trimMargin()
    }
}