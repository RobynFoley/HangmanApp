package controllers

import models.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File

class PlayerAPITest {
    private var p1: Player? = null
    private var p2: Player? = null
    private var p3: Player? = null
    private var p4: Player? = null
    private var p5: Player? = null
    private var populatedPlayers: PlayerAPI? = PlayerAPI(XMLSerializer(File("notes.xml")))
    private var emptyPlayers: PlayerAPI? = PlayerAPI(XMLSerializer(File("empty-notes.xml")))

    @BeforeEach
    fun setup(){
        p1 = Player("Emma", 5, 10)
        p2 = Player("Mathew", 1, 11)
        p3 = Player("Peter", 4, 5)
        p4 = Player("Ann", 4, 20)
        p5 = Player("Sarah", 3, 9)

        //adding 5 Note to the notes api
        populatedPlayers!!.add(p1!!)
        populatedPlayers!!.add(p2!!)
        populatedPlayers!!.add(p3!!)
        populatedPlayers!!.add(p4!!)
        populatedPlayers!!.add(p5!!)
    }


    @AfterEach
    fun tearDown(){
        p1 = null
        p2 = null
        p3 = null
        p4 = null
        p5 = null
        populatedPlayers = null
        emptyPlayers = null
    }

    @Nested
    inner class AddPlayers {
        @Test
        fun `adding a Player to a populated list adds to ArrayList`() {
            val newPlayer = Player("John", 0, 0)
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
            assertTrue(populatedPlayers!!.add(newPlayer))
            assertEquals(6, populatedPlayers!!.numberOfPlayers())

        }

        @Test
        fun `adding a Player to an empty list adds to ArrayList`() {
            val newPlayer = Player("John", 0, 0)
            assertEquals(0, emptyPlayers!!.numberOfPlayers())
            assertTrue(emptyPlayers!!.add(newPlayer))
            assertEquals(1, emptyPlayers!!.numberOfPlayers())
        }
    }

    @Nested
    inner class ListPlayers {

        @Test
        fun `listPlayers returns No Players Stored message when ArrayList is empty`() {
            assertEquals(0, emptyPlayers!!.numberOfPlayers())
            assertTrue(emptyPlayers!!.listPlayers().lowercase().contains("no players stored"))
        }

        @Test
        fun `listPlayers returns Players when ArrayList has Players stored`() {
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
            val playersString = populatedPlayers!!.listPlayers().lowercase()
            assertTrue(playersString.contains("emma"))
            assertTrue(playersString.contains("mathew"))
            assertTrue(playersString.contains("peter"))
            assertTrue(playersString.contains("ann"))
            assertTrue(playersString.contains("sarah"))
        }
    }


    @Nested
    inner class DeletePlayers {

        @Test
        fun `deleting a Player that does not exist, returns null`() {
            assertNull(emptyPlayers!!.removePlayer(0))
            assertNull(populatedPlayers!!.removePlayer(-1))
            assertNull(populatedPlayers!!.removePlayer(5))
        }

        @Test
        fun `deleting a Player that exists delete and returns deleted object`() {
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
            assertEquals(p5, populatedPlayers!!.removePlayer(4))
            assertEquals(4, populatedPlayers!!.numberOfPlayers())
            assertEquals(p1, populatedPlayers!!.removePlayer(0))
            assertEquals(3, populatedPlayers!!.numberOfPlayers())
        }
    }

        @Nested
        inner class UpdatePlayers {

            @Test
            fun `updating a Player that exists updates the correct Player`() {
                assertEquals("Emma", populatedPlayers!!.getPlayer(0).playerName)
                populatedPlayers!!.updatePlayer(0, "Emily")
                assertEquals("Emily", populatedPlayers!!.getPlayer(0).playerName)
            }

            @Test
            fun `updating a Player that does not exist does not alter the list`() {
                assertThrows(IndexOutOfBoundsException::class.java) {
                    populatedPlayers!!.updatePlayer(5, "Invalid Player")
                }
                assertThrows(IndexOutOfBoundsException::class.java) {
                    populatedPlayers!!.updatePlayer(-1, "Invalid Player")
                }
            }
        }

        @Nested
        inner class RetrievePlayers {

            @Test
            fun `getting a Player by valid index returns the correct Player`() {
                assertEquals(p1, populatedPlayers!!.getPlayer(0))
                assertEquals(p2, populatedPlayers!!.getPlayer(1))
            }

            @Test
            fun `getting a Player by invalid index returns the default Player`() {
                assertEquals(p1, populatedPlayers!!.getPlayer(-1)) // Default Player
                assertEquals(p1, populatedPlayers!!.getPlayer(5))  // Default Player
            }
        }

        @Nested
        inner class IsValidIndex {

            @Test
            fun `isValidIndex returns true for valid indices`() {
                assertTrue(populatedPlayers!!.isValidIndex(0))
                assertTrue(populatedPlayers!!.isValidIndex(4))
            }

            @Test
            fun `isValidIndex returns false for invalid indices`() {
                assertFalse(populatedPlayers!!.isValidIndex(-1))
                assertFalse(populatedPlayers!!.isValidIndex(5))
            }
        }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty players.xml file.
            val savingPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            savingPlayers.store()

            // Loading the empty players.xml file into a new object.
            val loadedPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            loadedPlayers.load()

            // Comparing the source of the players (savingPlayers) with the XML loaded players (loadedPlayers).
            assertEquals(0, savingPlayers.numberOfPlayers())
            assertEquals(0, loadedPlayers.numberOfPlayers())
            assertEquals(savingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
        }

        @Test
        fun `saving and loading a populated collection in XML doesn't lose data`() {
            // Saving 3 players to the players.xml file.
            val savingPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            savingPlayers.add(Player("Emma", 5, 10))
            savingPlayers.add(Player("Mathew", 1, 11))
            savingPlayers.add(Player("Peter", 4, 5))
            savingPlayers.store()

            // Loading players.xml into a different collection.
            val loadedPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            loadedPlayers.load()

            // Comparing the source of the players (savingPlayers) with the XML loaded players (loadedPlayers).
            assertEquals(3, savingPlayers.numberOfPlayers())
            assertEquals(3, loadedPlayers.numberOfPlayers())
            assertEquals(savingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty players.json file.
            val savingPlayers = PlayerAPI(JSONSerializer(File("players.json")))
            savingPlayers.store()

            // Loading the empty players.json file into a new object.
            val loadedPlayers = PlayerAPI(JSONSerializer(File("players.json")))
            loadedPlayers.load()

            // Comparing the source of the players (savingPlayers) with the JSON loaded players (loadedPlayers).
            assertEquals(0, savingPlayers.numberOfPlayers())
            assertEquals(0, loadedPlayers.numberOfPlayers())
            assertEquals(savingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
        }

        @Test
        fun `saving and loading a populated collection in JSON doesn't lose data`() {
            // Saving 3 players to the players.json file.
            val savingPlayers = PlayerAPI(JSONSerializer(File("players.json")))
            savingPlayers.add(Player("Emma", 5, 10))
            savingPlayers.add(Player("Mathew", 1, 11))
            savingPlayers.add(Player("Peter", 4, 5))
            savingPlayers.store()

            // Loading players.json into a different collection.
            val loadedPlayers = PlayerAPI(JSONSerializer(File("players.json")))
            loadedPlayers.load()

            // Comparing the source of the players (savingPlayers) with the JSON loaded players (loadedPlayers).
            assertEquals(3, savingPlayers.numberOfPlayers())
            assertEquals(3, loadedPlayers.numberOfPlayers())
            assertEquals(savingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
        }
    }



    }

