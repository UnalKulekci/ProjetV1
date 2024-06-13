![fast_and_precise](image/logo.png)

Fast and Precise is a game coded in Scala based on the `ZType` game which tests our ability to write on a keyboard.

This game shows who is the king of the keyboard!

## GOAL

This project was made for the 101.2 - Object-oriented programming course in ISC first year program.

## SCREENSHOTS

Here are some screenshots of the game.

### GAME START

To start the game, press `SPACE`.

The game starts with 4 words, on the next round, it will be 5 words, etc.

![in_game_start](image/game_start.png)

### IN GAME

The player needs to complete a word before going to the next one.

![in_game_typing](image/game_typing.png)

### GAME OVER

If the word falls to the bottom, the game will end and you will be able to see your old scores.

![game_over](image/game_over.png)

## TUTORIAL

To play the Fast and Precise GAME,

- Clone this repository in your IDE (IntelliJ is preferred).
- Open the `FastAndPrecise` Folder, not the ProjectV1.
- Add the `GDX2D` libraries from the `libs` folder. (*Right click* -> *Add as Library...*)
  - *gdx2d-desktop-1.2.2.jar*
  - *gdx2d-desktop-1.2.2-sources.jar*
- In `src` folder, launch the `Main.scala` file.


- In the game:
    1. Type the different letters to fill a word.
    2. For every word you have `18 seconds` before it falls down and stops the game.
    3. To go to the next round, you have to write every word shown on the screen.
    4. Every next round contains one more word to type on.
    5. To replay the game, type `y`.
    6. To leave the game, type `q`.
    7. Scores are saved in a .txt file, so you can see your best one!
    8. Enjoy and make the best score possible!

![isc](image/logo_isc.png)

## SOURCES

- Background used in the game
  - https://4kwallpapers.com/abstract/outrun-neon-dark-background-purple-4523.html

- GDX2D Library
  - https://isc-hei.github.io/gdx2d/doc/install/