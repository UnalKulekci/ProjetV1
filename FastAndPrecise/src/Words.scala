import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Random

object Words {

  val START_WORDS: Int = 4

  // This function is used to replace the letters with accent by their no-accent version
  private def noAccent(word: String): String = {
    val res = new Array[Char](word.length)

    for (i <- word.indices) {
      word(i) match {
        case 'é' | 'è' | 'ë' | 'ê' | 'ę' => res(i) = 'e'
        case 'à' | 'â' | 'ä' | 'ą' => res(i) = 'a'
        case 'ù' | 'ü' | 'ú' | 'û' => res(i) = 'u'
        case 'ï' | 'î' => res(i) = 'i'
        case 'ô' => res(i) = 'o'
        case 'ç' => res(i) = 'c'
        case other => res(i) = other
      }
    }
    new String(res)
  }

  // This function is used to read a .csv file and put all the different words into an ArrayBuffer[String]
  private def getFromFile(fileName: String): ArrayBuffer[String] = {
    try {
      val source = Source.fromFile(fileName)
      val lines = source.getLines().toArray
      val processedWords: ArrayBuffer[String] = ArrayBuffer.empty[String]

      for (i <- lines.indices) {
        val word = lines(i).split(";")(0)
        processedWords.append(noAccent(word))
      }
      source.close()
      return processedWords.distinct
    }
    catch {
      case e =>
        println(s"An error occurred: ${e.getMessage}")
        ArrayBuffer.empty[String]
    }
  }

  // This function calculates the maximum number of rounds to play
  private def calculateRounds(totalWords: Int): Int = {
    require(totalWords >= START_WORDS)
    var number_of_rounds: Int = 1
    var sum: Int = START_WORDS
    for (i <- START_WORDS + 1 until totalWords) {
      sum += i
      if (sum <= totalWords) {
        number_of_rounds += 1
      }
    }
    number_of_rounds
  }

  // This function creates Arrays with bigger number of words for every round
  def createRoundArray(src: Array[String]): ArrayBuffer[ArrayBuffer[String]] = {
    var allWords: ArrayBuffer[String] = ArrayBuffer.empty[String]
    // copy the src Array into an ArrayBuffer
    for (i <- src.indices) {
      allWords.append(src(i))
    }
    var final_res: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer.empty[ArrayBuffer[String]]
    var res: ArrayBuffer[String] = ArrayBuffer.empty[String]

    // 1st round -> 4 words, 30th round -> 33 words
    for (num_words <- START_WORDS until calculateRounds(src.length) + START_WORDS) {
      // Choosing num_words random words from src to insert them
      for (i <- 0 until num_words) {
        val rdmIdx: Int = Random.between(0, allWords.length)
        res.append(s"${allWords(rdmIdx)}")
        allWords.remove(rdmIdx)
      }
      final_res.append(res)
      res = ArrayBuffer.empty
    }
    final_res
  }

  def getWords(): ArrayBuffer[String] = {
    getFromFile("data/french_common.csv")
  }
}
