package controllers

import models.Admin
import models.Player
import persistence.Serializer
import utils.isValidListIndex

/**
 * This class manages a list of admins and provides functionality for adding, updating, deleting,
 * and listing admins. It uses a [Serializer] to load and store the notes persistently.
 *
 * @property serializer A serializer instance for reading and writing the notes.
 * @constructor Initializes the AdminAPI with the specified [serializerType].
 */
class AdminAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var admins = ArrayList<Admin>()

    fun add(admin: Admin): Boolean {
        return admins.add(admin)
    }

    fun getPasswordByPlayer(player: Player?): String {
        for(admin in admins) {
            if (player == admin.player){
               return admin.password
            }
    }
        return "invalid password"}

    fun isAdmin(player: Player?): Boolean{
        for(admin in admins) {
            if (player == admin.player){
                return true

            }
        }
    return false}

    fun numberOfAdmins(): Int {
        return admins.size
    }

    fun updatePasswordByPlayer(player: Player?, newPassword: String){
        for(admin in admins) {
            if (player == admin.player){
                admin.password = newPassword
            }
    }}

        fun updatePassword(index: Int, password: String) {
        admins[index].password = password
    }

    fun removeAdmin(indexToDelete: Int): Admin? {
        return if (isValidIndex(indexToDelete)) {
            admins.removeAt(indexToDelete)
        } else null
    }

    fun listAdmins(): String =
        if (admins.isEmpty()) "No admins stored"
        else formatListString(admins)

    private fun formatListString(adminsToFormat: List<Admin>): String =
        adminsToFormat
            .joinToString(separator = "\n") { player ->
                admins.indexOf(player).toString() + ":    " + player.toString()
            }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, admins);
    }

    @Throws(Exception::class)
    fun load() {
        admins = serializer.read() as ArrayList<Admin>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(admins)
    }
}