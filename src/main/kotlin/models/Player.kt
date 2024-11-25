package models

data class Player (
 var playerName: String,
    var winCount: Int,
    var gamesPlayed: Int
){
fun setPlayerNAme(playerName: String){
    this.playerName = playerName
}

}