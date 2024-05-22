import java.io.{BufferedReader, FileNotFoundException, FileReader}
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


object Words {

  def noAccent(word: String): String = {
    var res: Array[Char] = word.toCharArray
    for (i <- 0 until res.length) {
      if(res(i) == 'é' || res(i) == 'è' || res(i) == 'ë' || res(i) == 'ê' || res(i) == 'ę'){
        res(i) = 'e'
      }
      if(res(i) == 'à' || res(i) == 'â' || res(i) == 'ä' || res(i) == 'ą'){
        res(i) = 'a'
      }
      if(res(i) == 'ù' ||res(i) == 'ü' ||res(i) == 'ú' || res(i) == 'û'){
        res(i) = 'u'
      }
      if(res(i) == 'ï' ||res(i) == 'î'){
        res(i) = 'i'
      }
      if(res(i) == 'ô'){
        res(i) = 'o'
      }
      if(res(i) == 'ç'){
        res(i) = 'c'
      }
    }
    res.mkString("")
  }

  def GetFromFile(fileName: String): Array[String] = {
    try {
      var res: Array[String] = Source.fromFile(fileName).getLines().toArray
      for (i <- 0 until res.length) {
        res(i) = noAccent(res(i).split(";")(0))
      }
      return res
    } catch {
      case e: Exception =>
        e.printStackTrace()
        return Array.empty
    }
  }


}

object test extends App {
  println(Words.GetFromFile("data/french_common.csv").mkString("\n"))
}
