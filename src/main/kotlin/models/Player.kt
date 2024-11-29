package models

data class Player (
 var playerName: String,
    var winCount: Int,
    var gamesPlayed: Int
){

    override fun toString(): String {
        return """NAME:$playerName,     WINS: $winCount, TOTAL GAMES: $gamesPlayed""".trimMargin()
    }
}