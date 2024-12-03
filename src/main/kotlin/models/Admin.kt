package models

class Admin(
    var player: Player,
    var password: String
) {
    override fun toString(): String {
    return "${player.playerName}"
    }
}