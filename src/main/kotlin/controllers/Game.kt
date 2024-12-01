package controllers
import utils.readNextChar



class Game {

    //I used this to help me understand different loops
    //https://kotlinlang.org/docs/control-flow.html#for-loops

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
            val guess = readNextChar("Enter a Letter:  ")

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
                guessCount++

            } else {
                println("no")
                println(progress)
                guessCount++
            }
            hangman.sort()
            hangman = ArrayList(hangman.distinct())

            if (guessCount == 15){
                return false
            }
        }



        println("done!")
        return true

    }

}