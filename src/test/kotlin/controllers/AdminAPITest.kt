package controllers

import models.Admin
import models.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File

class AdminAPITest {
    private var a1: Admin? = null
    private var a2: Admin? = null
    private var a3: Admin? = null
    private var p1: Player? = null
    private var p2: Player? = null
    private var p3: Player? = null
    private var populatedAdmins: AdminAPI? = AdminAPI(XMLSerializer(File("admins.xml")))
    private var emptyAdmins: AdminAPI? = AdminAPI(XMLSerializer(File("empty-admins.xml")))

    @BeforeEach
    fun setup() {
        p1 = Player("Emma", 5, 10)
        p2 = Player("Mathew", 1, 11)
        p3 = Player("Peter", 4, 5)

        a1 = Admin(p1!!, "password1")
        a2 = Admin(p2!!, "password2")
        a3 = Admin(p3!!, "password3")

        // Adding admins to the populated list
        populatedAdmins!!.add(a1!!)
        populatedAdmins!!.add(a2!!)
        populatedAdmins!!.add(a3!!)
    }

    @AfterEach
    fun tearDown() {
        a1 = null
        a2 = null
        a3 = null
        p1 = null
        p2 = null
        p3 = null
        populatedAdmins = null
        emptyAdmins = null
    }

    @Nested
    inner class AddAdmins {
        @Test
        fun `adding an Admin to a populated list adds to ArrayList`() {
            val newAdmin = Admin(Player("Sarah", 3, 9), "password4")
            assertEquals(3, populatedAdmins!!.numberOfAdmins())
            assertTrue(populatedAdmins!!.add(newAdmin))
            assertEquals(4, populatedAdmins!!.numberOfAdmins())
        }

        @Test
        fun `adding an Admin to an empty list adds to ArrayList`() {
            val newAdmin = Admin(Player("Sarah", 3, 9), "password4")
            assertEquals(0, emptyAdmins!!.numberOfAdmins())
            assertTrue(emptyAdmins!!.add(newAdmin))
            assertEquals(1, emptyAdmins!!.numberOfAdmins())
        }
    }

    @Nested
    inner class ListAdmins {
        @Test
        fun `listAdmins returns No Admins Stored message when ArrayList is empty`() {
            assertEquals(0, emptyAdmins!!.numberOfAdmins())
            assertTrue(emptyAdmins!!.listAdmins().lowercase().contains("no admins stored"))
        }

        @Test
        fun `listAdmins returns Admins when ArrayList has Admins stored`() {
            assertEquals(3, populatedAdmins!!.numberOfAdmins())
            val adminsString = populatedAdmins!!.listAdmins().lowercase()
            assertTrue(adminsString.contains("emma"))
            assertTrue(adminsString.contains("mathew"))
            assertTrue(adminsString.contains("peter"))
        }
    }

    @Nested
    inner class RemoveAdmins {
        @Test
        fun `removing an Admin that does not exist returns null`() {
            assertNull(emptyAdmins!!.removeAdmin(0))
            assertNull(populatedAdmins!!.removeAdmin(-1))
            assertNull(populatedAdmins!!.removeAdmin(3))
        }

        @Test
        fun `removing an Admin that exists removes and returns the correct Admin`() {
            assertEquals(3, populatedAdmins!!.numberOfAdmins())
            assertEquals(a3, populatedAdmins!!.removeAdmin(2))
            assertEquals(2, populatedAdmins!!.numberOfAdmins())
            assertEquals(a1, populatedAdmins!!.removeAdmin(0))
            assertEquals(1, populatedAdmins!!.numberOfAdmins())
        }
    }

    @Nested
    inner class UpdatePasswords {
        @Test
        fun `updating an Admin password by index updates the correct password`() {
            populatedAdmins!!.updatePassword(0, "newPassword")
            assertEquals("newPassword", a1!!.password)
        }

        @Test
        fun `updating an Admin password by Player updates the correct password`() {
            populatedAdmins!!.updatePasswordByPlayer(p2, "securePassword")
            assertEquals("securePassword", a2!!.password)
        }
    }

    @Nested
    inner class GetPassword {
        @Test
        fun `getPasswordByPlayer returns the correct password for a valid player`() {
            assertEquals("password1", populatedAdmins!!.getPasswordByPlayer(p1))
            assertEquals("password2", populatedAdmins!!.getPasswordByPlayer(p2))
        }

        @Test
        fun `getPasswordByPlayer returns invalid password for a non-admin player`() {
            val nonAdminPlayer = Player("NonAdmin", 0, 0)
            assertEquals("invalid password", populatedAdmins!!.getPasswordByPlayer(nonAdminPlayer))
        }
    }

    @Nested
    inner class IsAdmin {
        @Test
        fun `isAdmin returns true for a player who is an admin`() {
            assertTrue(populatedAdmins!!.isAdmin(p1))
            assertTrue(populatedAdmins!!.isAdmin(p2))
        }

        @Test
        fun `isAdmin returns false for a player who is not an admin`() {
            val nonAdminPlayer = Player("NonAdmin", 0, 0)
            assertFalse(populatedAdmins!!.isAdmin(nonAdminPlayer))
        }
    }

    @Nested
    inner class IsValidIndex {
        @Test
        fun `isValidIndex returns true for valid indices`() {
            assertTrue(populatedAdmins!!.isValidIndex(0))
            assertTrue(populatedAdmins!!.isValidIndex(2))
        }

        @Test
        fun `isValidIndex returns false for invalid indices`() {
            assertFalse(populatedAdmins!!.isValidIndex(-1))
            assertFalse(populatedAdmins!!.isValidIndex(3))
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty admins.xml file.
            val savingAdmins = AdminAPI(XMLSerializer(File("admins.xml")))
            savingAdmins.store()

            // Loading the empty admins.xml file into a new object.
            val loadedAdmins = AdminAPI(XMLSerializer(File("admins.xml")))
            loadedAdmins.load()

            // Comparing the source of the admins (savingAdmins) with the XML loaded admins (loadedAdmins).
            assertEquals(0, savingAdmins.numberOfAdmins())
            assertEquals(0, loadedAdmins.numberOfAdmins())
            assertEquals(savingAdmins.numberOfAdmins(), loadedAdmins.numberOfAdmins())
        }

        @Test
        fun `saving and loading a populated collection in XML doesn't lose data`() {
            // Saving 3 admins to the admins.xml file.
            val savingAdmins = AdminAPI(XMLSerializer(File("admins.xml")))
            savingAdmins.add(Admin(Player("Emma", 5, 10), "password1"))
            savingAdmins.add(Admin(Player("Mathew", 1, 11), "password2"))
            savingAdmins.add(Admin(Player("Peter", 4, 5), "password3"))
            savingAdmins.store()

            // Loading admins.xml into a different collection.
            val loadedAdmins = AdminAPI(XMLSerializer(File("admins.xml")))
            loadedAdmins.load()

            // Comparing the source of the admins (savingAdmins) with the XML loaded admins (loadedAdmins).
            assertEquals(3, savingAdmins.numberOfAdmins())
            assertEquals(3, loadedAdmins.numberOfAdmins())
            assertEquals(savingAdmins.numberOfAdmins(), loadedAdmins.numberOfAdmins())
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty admins.json file.
            val savingAdmins = AdminAPI(JSONSerializer(File("admins.json")))
            savingAdmins.store()

            // Loading the empty admins.json file into a new object.
            val loadedAdmins = AdminAPI(JSONSerializer(File("admins.json")))
            loadedAdmins.load()

            // Comparing the source of the admins (savingAdmins) with the JSON loaded admins (loadedAdmins).
            assertEquals(0, savingAdmins.numberOfAdmins())
            assertEquals(0, loadedAdmins.numberOfAdmins())
            assertEquals(savingAdmins.numberOfAdmins(), loadedAdmins.numberOfAdmins())
        }

        @Test
        fun `saving and loading a populated collection in JSON doesn't lose data`() {
            // Saving 3 admins to the admins.json file.
            val savingAdmins = AdminAPI(JSONSerializer(File("admins.json")))
            savingAdmins.add(Admin(Player("Emma", 5, 10), "password1"))
            savingAdmins.add(Admin(Player("Mathew", 1, 11), "password2"))
            savingAdmins.add(Admin(Player("Peter", 4, 5), "password3"))
            savingAdmins.store()

            // Loading admins.json into a different collection.
            val loadedAdmins = AdminAPI(JSONSerializer(File("admins.json")))
            loadedAdmins.load()

            // Comparing the source of the admins (savingAdmins) with the JSON loaded admins (loadedAdmins).
            assertEquals(3, savingAdmins.numberOfAdmins())
            assertEquals(3, loadedAdmins.numberOfAdmins())
            assertEquals(savingAdmins.numberOfAdmins(), loadedAdmins.numberOfAdmins())
        }
    }

}
