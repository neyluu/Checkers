* SceneBase can extend JavaFX Scene, so workflow with Scene as variable and then setting it at the end of constructor can be skipped
* Win / lose / draw system
  * Probably not all cases are catched, for example, when player cant do any move(possible?) game will stuck
* GUI rework
  * Gameover windows icon

BUGS:
  * when Piece is promoted to King and can beat next piece, theres no moves that this piece can do and game is frozen
  * White king is darker than Man