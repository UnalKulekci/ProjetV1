import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.control.NonFatal

class Words() {
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

  def getFromFile(): ArrayBuffer[String] = {

    val fileName = "data/french_common.csv"

    try {
      val source = Source.fromFile(fileName)
      val lines = source.getLines().toArray
      val processedWords = new Array[String](lines.length)

      for (i <- lines.indices) {
        val word = lines(i).split(";")(0)
        processedWords(i) = noAccent(word)
      }
      source.close()
      return processedWords.to(ArrayBuffer)
    }
    catch {
      case e =>
        println(s"An error occurred: ${e.getMessage}")
        ArrayBuffer.empty[String]
    }
  }

  def getWords(): ArrayBuffer[String] = {
    getFromFile()

  }

}

