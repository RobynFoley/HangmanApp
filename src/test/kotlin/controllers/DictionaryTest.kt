package controllers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File

class DictionaryTest {
    private var dictionary: Dictionary? = null
    private var emptyDictionary: Dictionary? = null

    @BeforeEach
    fun setup() {
        dictionary = Dictionary(XMLSerializer(File("dictionary.xml")))
        emptyDictionary = Dictionary(XMLSerializer(File("empty-dictionary.xml")))

        // Adding sample words to dictionary
        dictionary!!.addWord("apple")
        dictionary!!.addWord("banana")
        dictionary!!.addWord("carrot")
    }

    @AfterEach
    fun tearDown() {
        dictionary = null
        emptyDictionary = null
    }

    @Nested
    inner class AddWords {
        @Test
        fun `adding a new word adds it to the dictionary`() {
            assertEquals(3, dictionary!!.numberOfWords())
            dictionary!!.addWord("date")
            assertEquals(4, dictionary!!.numberOfWords())
        }

        @Test
        fun `adding a duplicate word does not add it again`() {
            assertEquals(3, dictionary!!.numberOfWords())
            dictionary!!.addWord("apple")
            assertEquals(3, dictionary!!.numberOfWords())
        }
    }

    @Nested
    inner class ListWords {
        @Test
        fun `listAllWords returns No words stored when dictionary is empty`() {
            assertEquals("No words stored", emptyDictionary!!.listAllWords())
        }

    }

    @Nested
    inner class DeleteWords {
        @Test
        fun `deleting a word removes it from the dictionary`() {
            assertEquals(3, dictionary!!.numberOfWords())
            assertEquals("banana", dictionary!!.deleteWord(1))
            assertEquals(2, dictionary!!.numberOfWords())
        }

        @Test
        fun `deleting a non-existent word returns null`() {
            assertNull(dictionary!!.deleteWord(5))
            assertNull(emptyDictionary!!.deleteWord(0))
        }
    }

    @Nested
    inner class UpdateWords {
        @Test
        fun `updating a word modifies it in the dictionary`() {
            dictionary!!.updateWord(1, "blueberry")
            assertEquals("blueberry", dictionary!!.getWord(1))
        }
    }


    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty dictionary.XML file.
            val storingWords = Dictionary(XMLSerializer(File("dictionary.xml")))
            storingWords.store()

            //Loading the empty dictionary.xml file into a new object
            val loadedWords = Dictionary(XMLSerializer(File("dictionary.xml")))
            loadedWords.load()

            //Comparing the source of the words (storingWords) with the XML loaded words (loadedWords)
            assertEquals(0, storingWords.numberOfWords())
            assertEquals(0, loadedWords.numberOfWords())
            assertEquals(storingWords.numberOfWords(), loadedWords.numberOfWords())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingWords = Dictionary(XMLSerializer(File("dictionary.xml")))
            storingWords.addWord("carrot")
            storingWords.addWord("swim")
            storingWords.addWord("helicopter")
            storingWords.store()

            //Loading notes.xml into a different collection
            val loadedWords = Dictionary(XMLSerializer(File("dictionary.xml")))
            loadedWords.load()

        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingWords = Dictionary(JSONSerializer(File("dictionary.json")))
            storingWords.store()

            //Loading the empty notes.json file into a new object
            val loadedWords = Dictionary(JSONSerializer(File("Dictionary.json")))
            loadedWords.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingWords.numberOfWords())
            assertEquals(0, loadedWords.numberOfWords())
            assertEquals(storingWords.numberOfWords(), loadedWords.numberOfWords())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingWords = Dictionary(JSONSerializer(File("Dictionary.json")))
            storingWords.addWord("summer")
            storingWords.addWord("swim")
            storingWords.addWord("tumeric")
            storingWords.store()

            //Loading notes.json into a different collection
            val loadedWords = Dictionary(JSONSerializer(File("Dictionary.json")))
            loadedWords.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingWords.numberOfWords())
            assertEquals(3, loadedWords.numberOfWords())
            assertEquals(storingWords.numberOfWords(), loadedWords.numberOfWords())

        }
    }


}