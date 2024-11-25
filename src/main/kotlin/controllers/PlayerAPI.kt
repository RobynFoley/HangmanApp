package controllers

import jdk.jfr.TransitionTo
import models.Player
import persistence.Serializer
import utils.isValidListIndex

class PlayerAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var players = ArrayList<Player>()


    fun add(player: Player): Boolean {
        return players.add(player)
    }

    fun updatePlayer(index: Int, name: String) {
       players[index].setPlayerNAme(name)
    }

    fun listPlayers(): String =
        if (players.isEmpty()) "No Players stored"
        else formatListString(players)

    fun numberOfPlayers(): Int {
        return players.size
    }


    private fun formatListString(playersToFormat: List<Player>): String =
        playersToFormat
            .joinToString(separator = "\n") { player ->
                players.indexOf(player).toString() + ": " + player.toString()
            }

    fun removePlayer(indexToDelete: Int): Player? {
        return if (isValidListIndex(indexToDelete, players)) {
            players.removeAt(indexToDelete)
        } else null
        }


    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, players);
    }

    @Throws(Exception::class)
    fun load() {
        players = serializer.read() as ArrayList<Player>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(players)
    }
}