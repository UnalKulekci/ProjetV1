import java.io.{FileOutputStream, PrintWriter}
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object Record {
  def addNew(file: String, score: Int): Unit = {
    try {
      val fs = new FileOutputStream(file, true)
      val pw = new PrintWriter(fs)
      pw.println(score)
      pw.close()
    } catch {
      case e: Exception =>
        println("File can't be written")
        e.printStackTrace()
    }
  }

  def getFirst10(file: String): Array[Int] = {
    val res: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
    try {
      val source = Source.fromFile(file)
      for (line: String <- source.getLines()) {
        res.append(Integer.parseInt(line))
      }
      source.close()
      return res.sorted(Ordering.Int).reverse.take(10).toArray
    }
    catch {
      case e =>
        println(s"An error occurred: ${e.getMessage}")
        Array.empty[Int]
    }
  }
}
