import java.io.{BufferedReader, FileNotFoundException, FileReader}
import scala.collection.mutable.ArrayBuffer

object Words {

  val fileName: String = "data/french_common.csv"
  val wordsBuffer: ArrayBuffer[String] = ArrayBuffer[String]()

  try {
    val fr = new FileReader(fileName)
    val inputReader = new BufferedReader(fr)

    var line = inputReader.readLine()
    while (line != null) {
      val words = line.split(";")
      words.foreach(word => wordsBuffer += word.strip())
      line = inputReader.readLine()
    }

    inputReader.close()
  } catch {
    case e: FileNotFoundException =>
      println("File not found!")
      e.printStackTrace()
    case e: Exception =>
      println(s"Something bad happened during read ${e.getMessage}")
  }

  def getWords: ArrayBuffer[String] = {
    wordsBuffer
  }
}
