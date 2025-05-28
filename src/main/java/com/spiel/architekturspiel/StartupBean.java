package com.spiel.architekturspiel;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.math.BigDecimal;


@Component
public class StartupBean {

    //   public class PositionTracker {

    private int lastX = -1;
    private int lastY = -1;

    private DefaultApi defaultApi;
    private BigDecimal gameId;

    // switchcase Anwendung für Richtung.
    enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    // Declarieren der Karte
    int[][] gameMap = new int[7][7];

    // Getter
    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    // Setter
    public void setLastX(int x) {
        this.lastX = x;
    }

    public void setLastY(int y) {
        this.lastY = y;
    }

    public static int generiereZahl() {
        Random random = new Random();
        return random.nextInt(4) + 1; // ergibt 1 bis 4
    }


    public boolean checkPositionBlocked(int currentX, int currentY) {
        if (currentX == getLastX() && currentY == getLastY()) {
            //System.out.println("❌ Bewegung blockiert - Position unverändert" + "(" + currentX + ", " + currentY + ")");
            return true; // Position hat sich NICHT verändert
        } else {
            setLastX(currentX);
            setLastY(currentY);
            //System.out.println("✅ Bewegung erfolgreich zu (" + currentX + ", " + currentY + ")");
            return false; // Position hat sich geändert
        }

    } // boolean moved = checkPositionChanged(x, y);


    public boolean checkBorderUpRight(int z) {
        if (z == 5) {
            //System.out.println("Obere Grenze erreicht. Wert " + z);
            return false;
        } else {
            //System.out.println("Move Up or Right: Weg Frei. Wert: " + z);
            return true;
        }
    }

    public boolean checkBorderDownLeft(int z) {
        if (z == 1) {
            //System.out.println("Untere Grenze erreicht. Wert " + z);
            return false;
        } else {
            //System.out.println("Move Down or Left: Weg Frei. Wert: " + z);
            return true;
        }
    }

    public void drawMap(boolean blocked, int x, int y, Direction direction) {
        int j = x;
        int i = y;

        if (direction == Direction.UP) {
         //   System.out.println("\uD83E\uDDF1 Blocked is:"+blocked);
            if (blocked) {
                i++;
                gameMap[i][j] = 3;
            //    System.out.println("\uD83E\uDDF1 Border!");
            } else {
                gameMap[i][j] = 2;
              //  System.out.println("Wert geändert an stelle X:" + j + " Y:" + i);
            }
        }

        if (direction == Direction.DOWN) {

            if (blocked) {
                i--;
                gameMap[i][j] = 3;
              //  System.out.println("\uD83E\uDDF1 Border!");
            }else {
                gameMap[i][j] = 2;
               // System.out.println("Wert geändert an stelle X:" + j + " Y:" + i);

            }
        }

        if (direction == Direction.LEFT) {

            if (blocked) {
                j--;
                gameMap[i][j] = 3;
               // System.out.println("\uD83E\uDDF1 Border!");
            } else {
                gameMap[i][j] = 2;
                //System.out.println("Wert geändert an stelle X:" + j + " Y:" + i);

            }
        }

        if (direction == Direction.RIGHT) {

                if (blocked) {
                    j++;
                    gameMap[i][j] = 3;
                   // System.out.println("\uD83E\uDDF1 Border!");
                }else {
                    gameMap[i][j] = 2;
                  //  System.out.println("Wert geändert an stelle X:" + j + " Y:" + i);

                }
        }
}


    // Bewegungsmethoden
    int moveUp(int y) {

        MoveInputDto moveUp = new MoveInputDto();
        moveUp.setDirection(DirectionDto.UP);
        MoveDto result1 = defaultApi.gameGameIdMovePost(gameId, moveUp);
        y = result1.getPositionAfterMove().getPositionY().intValue();
        //         System.out.println(" Zug (UP): " + result1);
        //         System.out.println(" Zug (UP): Y ist " + y);
        return y;
    }

    int moveRight(int z) {

        MoveInputDto moveRight = new MoveInputDto();
        moveRight.setDirection(DirectionDto.RIGHT);
        MoveDto result2 = defaultApi.gameGameIdMovePost(gameId, moveRight);
        z = result2.getPositionAfterMove().getPositionX().intValue();
        //System.out.println(" Zug (RIGHT): " + result2);

        return z;
    }

    int moveDown(int y) {

        MoveInputDto moveDown = new MoveInputDto();
        moveDown.setDirection(DirectionDto.DOWN);
        MoveDto result3 = defaultApi.gameGameIdMovePost(gameId, moveDown);
        y = result3.getPositionAfterMove().getPositionY().intValue();
        //System.out.println(" Zug (Down): " + result3);
        //         System.out.println(" Zug (Down): X ist " + y);
        return y;
    }

    int moveLeft(int x) {

        MoveInputDto moveLeft = new MoveInputDto();
        moveLeft.setDirection(DirectionDto.LEFT);
        MoveDto result4 = defaultApi.gameGameIdMovePost(gameId, moveLeft);
        x = result4.getPositionAfterMove().getPositionX().intValue();
        // System.out.println(" Zug (Left): " + result4);
        //         System.out.println(" Zug (Left): Y ist " + x);

        return x;
    }


    int executeMovement(Direction direction, int z, Boolean canMove) {

        switch (direction) {
            case UP: {
                if (canMove) {
                 //   System.out.println("⬆\uFE0F Up wurde angewählt!");
                    z = moveUp(z);
                //    System.out.println(" Zug (UP): Y ist " + z);
                }
            }
            break;

            case RIGHT: {
                if (canMove) {
                //    System.out.println("➡\uFE0F Right wurde angewählt!");
                    z = moveRight(z);
                //    System.out.println(" Zug (Right): X ist " + z);
                }
            }
            break;

            case LEFT: {
                if (canMove) {
                //    System.out.println("⬅\uFE0F Left wurde angewählt!");
                    z = moveLeft(z);
                //    System.out.println(" Zug (Left): X ist " + z);
                }

            }
            break;

            case DOWN: {
                if (canMove) {
               //     System.out.println("⬇\uFE0F Down wurde angewählt!");
                    z = moveDown(z);
                //    System.out.println(" Zug (Down): Y ist " + z);
                }

            }
            break;

        }
        return z;
    }

    @PostConstruct
    public void init() {
        this.defaultApi = new DefaultApi();
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("OlafRose");
        GameDto response = defaultApi.gamePost(gameInput);

        this.gameId = response.getGameId();

        System.out.println(response);


        boolean gameActive = true;

        boolean blocked = false;


        boolean canMoveUp = true;
        boolean canMoveRight = true;
        boolean canMoveLeft = true;
        boolean canMoveDown = true;

        // Parameter für koordinierung

        int y = 1;
        int x = 1;

        // Parameter für Richtung
        int richtung = 0;

        // Erstellen der Karte
       // int[][] gameMap = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                gameMap[i][j] = 1;
                System.out.print("["+gameMap[i][j]+"]");
            }
            System.out.println();
        }


        while (gameActive) {
            GameDto status = defaultApi.gameGameIdGet(gameId);
            GameStatusDto currentStatus = status.getStatus();

            if (currentStatus == GameStatusDto.SUCCESS) {
                System.out.println("🎉 Spiel gewonnen!");
                gameActive = false;
            } else if (currentStatus == GameStatusDto.FAILED) {
                System.out.println("❌ Spiel verloren.");
                gameActive = false;
            } else {

                Direction direction;

                    richtung = generiereZahl();
                    System.out.println("Richtung ist: " + richtung);


                    if (richtung == 1) {
                        //--------- moveUP
                        canMoveUp = checkBorderUpRight(y);
                        direction = Direction.UP;
                        y = executeMovement(direction, y, canMoveUp);
                        blocked = checkPositionBlocked(x, y);
                        drawMap( blocked, x, y, direction);

                    }
                /**System.out.println("Drücke die Eingabetaste, um fortzufahren...");
                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                } **/

                    if (richtung == 2) {

                        //--------- moveRight
                        canMoveRight = checkBorderUpRight(x);
                        direction = Direction.RIGHT;
                        x = executeMovement(direction, x, canMoveRight);
                        blocked = checkPositionBlocked(x, y);

                        drawMap( blocked, x, y, direction);

                    }

                    if (richtung == 3) {
                        //--------- moveLeft
                        canMoveLeft = checkBorderDownLeft(x);
                        direction = Direction.LEFT;
                        x = executeMovement(direction, x, canMoveLeft);
                        blocked = checkPositionBlocked(x, y);

                        drawMap( blocked, x, y, direction);
                    }

                    if (richtung == 4) {
                        //--------- moveDown
                        canMoveDown = checkBorderDownLeft(y);
                        direction = Direction.DOWN;
                        y = executeMovement(direction, y, canMoveDown);
                        blocked = checkPositionBlocked(x, y);

                        drawMap( blocked, x, y, direction);
                    }
                for (int i = 0; i < 7  ; i++) {
                    System.out.println();
                }
                // Erstellen der Karte
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        System.out.print("["+gameMap[i][j]+"]");
                    }
                    System.out.println();
                }

                try {
                    Thread.sleep(500); // 2000 Millisekunden = 2 Sekunden
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                }

            }
        }

    }






