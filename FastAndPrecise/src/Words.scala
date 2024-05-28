import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Random
import scala.util.control.NonFatal

object Words {
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

  private def getFromFile(fileName: String): ArrayBuffer[String] = {

    try {
      val source = Source.fromFile(fileName)
      val lines = source.getLines().toArray
      val processedWords = new Array[String](lines.length)

      for (i <- lines.indices) {
        val word = lines(i).split(";")(0)
        processedWords(i) = noAccent(word)
      }
      source.close()
      return processedWords.to(ArrayBuffer).distinct
    }
    catch {
      case e =>
        println(s"An error occurred: ${e.getMessage}")
        ArrayBuffer.empty[String]
    }
  }

  def calculateRounds(totalWords: Int): Int = {
    require(totalWords >= 4)
    var number_of_rounds: Int = 1
    var sum: Int = 4
    for (i <- 5 until totalWords) {
      sum += i
      if (sum <= totalWords) {
        number_of_rounds += 1
      }
    }
    println(number_of_rounds)
    number_of_rounds
  }

  def createRoundArray(src: Array[String]): Array[Array[String]] = {
    var allWords: ArrayBuffer[String] = ArrayBuffer.empty[String]
    // copy the src Array into an ArrayBuffer
    for (i <- src.indices) {
      allWords.append(src(i))
    }
    var final_res: ArrayBuffer[Array[String]] = ArrayBuffer.empty[Array[String]]
    var res: ArrayBuffer[String] = ArrayBuffer.empty[String]

    // 1st round -> 4 words, 30th round -> 33 words
    for (num_words <- 4 until calculateRounds(src.length) + 4) {
      // Choosing num_words random words from src to insert them
      for (i <- 0 until num_words) {
        val rdmIdx: Int = Random.between(0, allWords.length)
        res.append(s"${allWords(rdmIdx)}")
        allWords.remove(rdmIdx)
      }
      final_res.append(res.toArray)
      res = ArrayBuffer.empty
    }
    final_res.toArray
  }

  def getWords(): ArrayBuffer[String] = {
    getFromFile("data/french_common.csv")
  }
}

object test23 extends App {
  val to_test: Array[Array[String]] = Words.createRoundArray(Words.getWords().toArray)
  for (i <- to_test.indices) {
    println(s"Round ${i + 1} : ${to_test(i).mkString(",")}")
  }
}
