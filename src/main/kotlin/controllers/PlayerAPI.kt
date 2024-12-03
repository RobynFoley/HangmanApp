package controllers

import jdk.jfr.TransitionTo
import models.Player
import persistence.Serializer
import utils.isValidListIndex


/**
 * This class manages a list of players and provides functionality for adding, updating, deleting,
 * and listing players. It uses a [Serializer] to load and store the notes persistently.
 *
 * @property serializer A serializer instance for reading and writing the notes.
 * @constructor Initializes the PlayerAPI with the specified [serializerType].
 */
class PlayerAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var players = ArrayList<Player>()



    /**
     * Adds a new [Player] to the list.
     *
     * @param player The [Player] to be added.
     * @return `true` if the player was successfully added, `false` otherwise.
     */
    fun add(player: Player): Boolean {
        return players.add(player)
    }

    fun updatePlayer(index: Int, name: String) {
       players[index].playerName = name
    }

    fun getPlayer(index: Int): Player {
        return if (isValidListIndex(index, players)) {
            return players[index]
        } else {
            return players[0]
        }

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
                players.indexOf(player).toString() + ":    " + player.toString()
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

    fun increaseGameCount(player: Player?) {
        player?.gamesPlayed = player?.gamesPlayed!! + 1
    }

    fun increaseWinCount(player: Player?) {
        player?.winCount = player?.winCount!! + 1
    }


}