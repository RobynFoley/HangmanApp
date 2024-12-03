package controllers
import persistence.Serializer
import utils.readNextChar
import utils.readNextLine

/**
 * This class contains code for a simple hangman game
 *
 */
class Game {

    //I used this to help me understand different loops
    //https://kotlinlang.org/docs/control-flow.html#for-loops

    //I used this to for my delays
    //https://stackoverflow.com/questions/45213706/kotlin-wait-function

    fun run(word: String): Boolean{


        var letters = word.toCharArray()
        var list = letters.distinct()
        list = list.sorted()

        var hangman = ArrayList<Char>()
        var progress = ArrayList<Char>()

        for (letter in letters) {
            progress.add('_')
        }

        var guessCount: Int = 0

        println(progress)


        while (!(hangman == list)) {
            val guessWord = readNextLine("Enter your guess:  ")
            if (guessWord == word) {
                for(letter in list){
                    hangman.add(letter)
                }
            }else {

                val guess = guessWord.first()
                if (guess in list) {
                println("yippy")
                hangman.add(guess)

                //for(i in hangman.indices) {
                for (y in 0..(letters.size - 1)) {
                    if (letters[y] == guess) {
                        progress[y] = guess
                    }
                }
                println(progress)
                    Thread.sleep(500)
                guessCount++

            } else {
                println("no")
                    Thread.sleep(300)
                println(progress)
                    Thread.sleep(500)
                guessCount++
            }}
            hangman.sort()
            hangman = ArrayList(hangman.distinct())

            if (guessCount == 15){
                println("You Lose !!!")
                println("The word was $word")
                Thread.sleep(500)
                return false
            }
        }


        Thread.sleep(200)
        println("*")
        Thread.sleep(200)
        println("     *")
        Thread.sleep(200)
        println("*")
        println("You Win !!!")
        Thread.sleep(500)
        return true

    }

}