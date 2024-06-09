import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.{Color, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, SpriteBatch}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Mixed extends PortableApplication(1920, 1080) {

  private var w: ArrayBuffer[ArrayBuffer[String]] = Words.createRoundArray(Words.getWords().toArray)
  private var arrSorted: ArrayBuffer[String] = ArrayBuffer.empty[String]
  private val arrSortedLength: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
  private var currentWordIndex = -1
  private var roundCounter: Int = 0
  private val fallingWords = new ArrayBuffer[WordPosition]()
  private var scoresCounter: Float = 0f
  private var idxTimer: Int = 0
  private var secondTimer: Int = 0
  private var wallpaper: BitmapImage = _
  private var gameover: BitmapImage = _
  private var font_blue: BitmapFont = _
  private var font_isc: BitmapFont = _
  private var font_black: BitmapFont = _
  private var font_in_use: BitmapFont = _
  private var isGameOver = false // Game over state
  private var chr: Char = ' '

  // Used to replay the game
  private def initGame(): Unit = {
    isGameOver = false
    w = Words.createRoundArray(Words.getWords().toArray)
    arrSorted = ArrayBuffer.empty[String]
    fallingWords.clear()
    arrSortedLength.clear()
    currentWordIndex = -1
    roundCounter = 0
    scoresCounter = 0f
    idxTimer = 0
    secondTimer = 0
  }

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
    val x = Random.between(200, maxWidth - 200)
    val y = maxHeight + Random.between(50, 150)
    (x, y)
  }

  // Function called only ones
  override def onInit(): Unit = {
    println(Console.MAGENTA + "FAST & PRECISE" + Console.RESET + " 2024 " + Console.RESET + "by " + Console.BLUE + "ÜNAL" + Console.RESET + " and" + Console.YELLOW + " FILIP" + Console.RESET)
    setTitle("FAST & PRECISE")
    wallpaper = new BitmapImage("data/wallpaper.png")
    gameover = new BitmapImage("data/gameover.png")

    // FONT
    val starjedi = Gdx.files.internal("data/font/Starjedi.ttf")
    val parameter = new FreeTypeFontGenerator.FreeTypeFontParameter
    val generator = new FreeTypeFontGenerator(starjedi)

    // BLUE FONT
    parameter.size = generator.scaleForPixelHeight(30)
    parameter.color = Color.WHITE
    parameter.borderColor = Color.valueOf("165baa")
    parameter.borderWidth = 3
    font_blue = generator.generateFont(parameter)

    // PINK FONT
    parameter.size = generator.scaleForPixelHeight(30)
    parameter.color = Color.valueOf("d41367")
    parameter.borderColor = Color.WHITE

    parameter.borderWidth = 3
    font_isc = generator.generateFont(parameter)

    // BLACK FONT
    parameter.size = generator.scaleForPixelHeight(30)
    parameter.color = Color.BLACK
    parameter.borderColor = Color.WHITE
    parameter.borderWidth = 3
    font_black = generator.generateFont(parameter)
    generator.dispose()

    font_in_use = font_blue
  }

  // Graphic function, counter
  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawPicture(getWindowWidth / 2, getWindowHeight / 2, wallpaper)
    if (!isGameOver) {
      idxTimer += 1
      if (idxTimer % 60 == 0) {
        secondTimer += 1
      }
    }

    // Print the score
    g.drawString(20f, 70f, s" Total Scores :", font_black)
    g.drawString(230f, 70f, s" ${scoresCounter.toInt}", font_isc)
    // Print Round & Timer
    g.drawString(20f, 50f, s"Round : ${roundCounter + 1}          Time :", font_black)
    g.drawString(330f, 50f, s"${secondTimer} s", font_isc)

    // Filling the array with words for the actual round
    if (roundCounter < w.length) {
      arrSorted = w(roundCounter)
      // Filling fallingWords array used to make the words fall on the window
      if (fallingWords.length < arrSorted.length) {
        for (i <- arrSorted.indices) {
          val (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
          val gapX = x * 0.1f + x
          if (i == 0 || (i > 0 && (fallingWords(i - 1).x < gapX) || (fallingWords(i - 1).x > gapX))) {
            fallingWords.append(WordPosition(arrSorted(i), gapX, y))
          }
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
    for (i: Int <- fallingWords.indices) {
      fallingWords(i).y -= 1 // -60 pixels/s

      if (arrSortedLength(i) != fallingWords(i).word.length) {
        font_in_use = font_isc
      } else {
        font_in_use = font_blue
      }
      // Drawing words on the screen
      g.drawString(fallingWords(i).x, fallingWords(i).y, fallingWords(i).word, font_in_use)
      font_in_use = font_blue

      if (fallingWords(i).y < 0) {
        g.clear()
        isGameOver = true
        g.drawStringCentered(g.getScreenHeight / 2 - 150, s"Achieved round : ${roundCounter}", font_blue)
        g.drawStringCentered(g.getScreenHeight / 2 - 200, s"Score : ${scoresCounter.toInt}", font_blue)
        g.drawPicture(g.getScreenWidth / 2, g.getScreenHeight / 2, gameover)
        g.drawStringCentered(g.getScreenHeight / 2 - 100, "PRESS y to replay", font_isc)
        g.drawStringCentered(g.getScreenHeight / 2 - 250, "PRESS q to quit", font_black)
      }
    }
    g.drawSchoolLogo()
  }

  // This function deletes the letters from the word
  override def onKeyDown(keycode: Int): Unit = {

    // Getting the Char after pressing a key on the keyboard
    chr = (keycode + 68).toChar
    if (isGameOver) {
      if (chr == 'y') {
        initGame()
      } else if (chr == 'q') {
        println("Thank you for playing")
        println(s"Achieved round : $roundCounter")
        println(s"Score : ${scoresCounter.toInt} in $secondTimer seconds")
        System.exit(1337)
      } else {
        return
      }
    }
    if (currentWordIndex == -1) {
      // Getting the word index to work on
      currentWordIndex = returnCurrentIdx(fallingWords, chr)
    }

    // Removing word from arrays after deleting every typing every letter of the word
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


