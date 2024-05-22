import java.io.{BufferedReader, FileNotFoundException, FileReader}

case class Words() {

  val fileName: String = "data/french_common.csv"

  try {
    val fr = new FileReader(fileName)
    val inputReader = new BufferedReader(fr)

    var line = inputReader.readLine()
    println(line)

    line = inputReader.readLine().strip(";")
    println(line)

    inputReader.close()
  } catch {
    case e: FileNotFoundException =>
      println("File not found !")
      e.printStackTrace()
    case e: Exception =>
      println(s"Something bad happened during read ${e.getMessage()}")
  }



}
