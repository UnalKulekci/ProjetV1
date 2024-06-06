import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Color, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, SpriteBatch}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


/*  TODO ::
     AT GAME OVER, THE GAME STOPS AND RESTARTS IF THE USER CLICKS YES -- restart will added
     YOU NEED TO INCREASE THE FONT OF THE WORDS ON THE SCREEN -- ??
 */

class Mixed extends PortableApplication(1920, 1080) {

  private val w: ArrayBuffer[ArrayBuffer[String]] = Words.createRoundArray(Words.getWords().toArray)
  private var arrSorted: ArrayBuffer[String] = ArrayBuffer.empty[String]
  private val arrSortedLength: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
  private var currentWordIndex = -1
  private var roundCounter: Int = 0
  private val fallingWords = new ArrayBuffer[WordPosition]()
  private var isGameOver = false // Game over state
  private var scoresCounter: Float = 0f
  private var idxTimer: Int = 0
  private var secondTimer: Int = 0
  private var font: BitmapFont = _
  private var batch: SpriteBatch = _

  // Word select after pressing a key depending on the y position as well as the order of letters
  private def returnCurrentIdx(a: ArrayBuffer[WordPosition], c: Char): Int = {
    var res: Int = 0
    var min: Int = 0
    var x: ArrayBuffer[WordPosition] = ArrayBuffer.empty[WordPosition]
    for (i <- a.indices) {
      if (a(i).word.startsWith(c.toString)) {
        x.append(a(i))
      }
    }
    x = x.sortBy(_.word)
    if (x.length == 1) {
      return a.indexOf(x(0))
    } else if (x.isEmpty) {
      return -1
    } else {
      min = 0
      for (i <- x.indices) {
        if (x(min).y > x(i).y) {
          min = i
        }
      }
      return a.indexOf(x(min))
    }
    -1
  }

  // Function used to display words in a random way
  private def getRandomPosition(maxWidth: Float, maxHeight: Float): (Float, Float) = {
    val x = 100 + Random.nextFloat() * (maxWidth - 200)
    val y = maxHeight
    (x, y)
  }

  // Function called only ones
  override def onInit(): Unit = {
    println(Console.MAGENTA + "FAST & PRECISE" + Console.RESET + " 2024 " + Console.RESET + "by " + Console.BLUE + "ÜNAL" + Console.RESET + " and" + Console.YELLOW + " FILIP" + Console.RESET)
    setTitle("FAST & PRECISE")
    font = new BitmapFont()
    font.getData.setScale(1.1f) // Increase font size
    batch = new SpriteBatch()
  }

  // Graphic function, counter
  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    batch.begin()
    if (!isGameOver) {
      idxTimer += 1
      if (idxTimer % 60 == 0) {
        secondTimer += 1
      }
    }
    font.setColor(Color.VIOLET)
    font.draw(batch, s"Round : ${roundCounter + 1}          Time : ${secondTimer}", 20f, 50f)
    font.setColor(Color.BLACK)
    g.setBackgroundColor(Color.WHITE)

    // Set the scores
    font.draw(batch, s" Total Scores : ${scoresCounter}", 20f, 90f)

    // Filling the array with words for the actual round
    if (roundCounter < w.length) {
      arrSorted = w(roundCounter)
      // Filling fallingWords array used to make the words fall on the window
      if (fallingWords.length < arrSorted.length) {
        for (i <- 0 until arrSorted.length) {
          val (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat + Random.between(0, 120))
          fallingWords.append(WordPosition(arrSorted(i), x, y))
        }
      }
      // ArrSortedLength used to change the color of a word which is being typed
      if (arrSortedLength.length < arrSorted.length) {
        for (i <- arrSorted.indices) {
          arrSortedLength.append(arrSorted(i).length)
        }
      }
    }

    // Update and draw falling words
    for (i: Int <- 0 until fallingWords.length) {
      fallingWords(i).y -= 1 // -60 pixels/s

      if (arrSortedLength(i) != fallingWords(i).word.length) {
        font.setColor(new Color(212 / 255f, 0, 103 / 255f, 1))
      } else {
        font.setColor(Color.BLACK)
      }

      // Drawing words on the screen
      font.draw(batch, fallingWords(i).word, fallingWords(i).x, fallingWords(i).y)

      font.setColor(Color.BLACK)

      if (fallingWords(i).y < 0) {
        font.draw(batch, "G  A  M  E  O  V  E  R", g.getScreenWidth / 2f, g.getScreenHeight / 2f)
        isGameOver = true
      }
    }
    batch.end()
    g.drawSchoolLogo()
  }

  // This function deletes the letters from the word
  override def onKeyDown(keycode: Int): Unit = {
    if (isGameOver) {
      return
    }

    // Getting the Char after pressing a key on the keyboard
    val chr = (keycode + 68).toChar
    if (currentWordIndex == -1) {
      // Getting the word index to work on
      currentWordIndex = returnCurrentIdx(fallingWords, chr)
    }

    if (currentWordIndex != -1 && fallingWords(currentWordIndex).word.startsWith(chr.toString)) {
      fallingWords(currentWordIndex).word = fallingWords(currentWordIndex).word.substring(1)
      if (fallingWords(currentWordIndex).word.isEmpty) {
        scoresCounter += (roundCounter + 1) * arrSortedLength(currentWordIndex)
        arrSorted.remove(currentWordIndex)
        fallingWords.remove(currentWordIndex)
        arrSortedLength.remove(currentWordIndex)
        currentWordIndex = -1
        if (fallingWords.isEmpty) {
          roundCounter += 1
        }
      }
    }
  }
}


