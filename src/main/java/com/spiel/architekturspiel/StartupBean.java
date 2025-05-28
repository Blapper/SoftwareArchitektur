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
            System.out.println("❌ Bewegung blockiert - Position unverändert" + "(" + currentX + ", " + currentY + ")");
            return true; // Position hat sich NICHT verändert
        } else {
            setLastX(currentX);
            setLastY(currentY);
            System.out.println("✅ Bewegung erfolgreich zu (" + currentX + ", " + currentY + ")");
            return false; // Position hat sich geändert
        }

    } // boolean moved = checkPositionChanged(x, y);


    public boolean checkBorderUpRight(int z) {
        if (z == 5) {
            System.out.println("Obere Grenze erreicht. Wert " + z);
            return false;
        } else {
            System.out.println("Move Up or Right: Weg Frei. Wert: " + z);
            return true;
        }
    }

    public boolean checkBorderDownLeft(int z) {
        if (z == 1) {
            System.out.println("Untere Grenze erreicht. Wert " + z);
            return false;
        } else {
            System.out.println("Move Down or Left: Weg Frei. Wert: " + z);
            return true;
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
        System.out.println(" Zug (RIGHT): " + result2);

        return z;
    }

    int moveDown(int y) {

        MoveInputDto moveDown = new MoveInputDto();
        moveDown.setDirection(DirectionDto.DOWN);
        MoveDto result3 = defaultApi.gameGameIdMovePost(gameId, moveDown);
        y = result3.getPositionAfterMove().getPositionY().intValue();
        System.out.println(" Zug (Down): " + result3);
        //         System.out.println(" Zug (Down): X ist " + y);
        return y;
    }

    int moveLeft(int x) {

        MoveInputDto moveLeft = new MoveInputDto();
        moveLeft.setDirection(DirectionDto.LEFT);
        MoveDto result4 = defaultApi.gameGameIdMovePost(gameId, moveLeft);
        x = result4.getPositionAfterMove().getPositionX().intValue();
        System.out.println(" Zug (Left): " + result4);
        //         System.out.println(" Zug (Left): Y ist " + x);

        return x;
    }

    
    int executeMovement(Direction direction, int z, Boolean canMove) {

        switch (direction) {
            case UP: {
                if (canMove) {
                    System.out.println("🎉 Up wurde angewählt!");
                    z = moveUp(z);
                    System.out.println(" Zug (UP): Y ist " + z);
                }
            }
            break;

            case RIGHT: {
                if (canMove) {
                    System.out.println("🎉 Right wurde angewählt!");
                    z = moveRight(z);
                    System.out.println(" Zug (Right): X ist " + z);
                }
            }
            break;

            case LEFT: {
                if (canMove) {
                    System.out.println("🎉 Left wurde angewählt!");
                    z = moveLeft(z);
                    System.out.println(" Zug (Left): X ist " + z);
                }

            }
            break;

            case DOWN: {
                if (canMove) {
                    System.out.println("🎉 Down wurde angewählt!");
                    z = moveDown(z);
                    System.out.println(" Zug (Down): Y ist " + z);
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

        int[][] gameMap = new int[5][5];

        // Parameter für Richtung
        int richtung = 0;


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

                // Optional: Initialisierung und Ausgabe
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        gameMap[i][j] = 1; // Beispielwert
                        System.out.print("["+gameMap[i][j]+"]");
                    }
                    System.out.println();
                }

                    richtung = generiereZahl();
                    System.out.println("Richtung ist: " + richtung);
                    richtung = 1;

                    if (richtung == 1) {
                        //--------- moveUP
                        canMoveUp = checkBorderUpRight(y);
                        direction = Direction.UP;
                        y = executeMovement(direction, y, canMoveUp);
                        blocked = checkPositionBlocked(x, y);

                        if (blocked) {
                            y++;
                            gameMap[y][x] = 0;
                            y--;
                        } else {
                            gameMap[y][x] = 2;
                        }


                    }

                    if (richtung == 2) {

                        //--------- moveRight
                        canMoveRight = checkBorderUpRight(x);
                        direction = Direction.RIGHT;
                        x = executeMovement(direction, x, canMoveRight);
                        blocked = checkPositionBlocked(x, y);


                    }

                    if (richtung == 3) {
                        //--------- moveLeft
                        canMoveLeft = checkBorderDownLeft(x);
                        direction = Direction.LEFT;
                        x = executeMovement(direction, x, canMoveLeft);
                        blocked = checkPositionBlocked(x, y);


                    }

                    if (richtung == 4) {
                        //--------- moveDown
                        canMoveDown = checkBorderDownLeft(y);
                        direction = Direction.DOWN;
                        y = executeMovement(direction, y, canMoveDown);
                        blocked = checkPositionBlocked(x, y);

                        System.out.println("Drücke die Eingabetaste, um fortzufahren...");
                        try {
                            System.in.read(); // Wartet auf Tasteneingabe (z. B. Enter)
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }

    }






