package com.spiel.architekturspiel;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class StartupBean {

    //   public class PositionTracker {

    private int lastX = -1;
    private int lastY = -1;

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

    /**
     * Vergleicht übergebene Position mit der letzten gespeicherten.
     * Gibt true zurück, wenn sich etwas verändert hat – andernfalls false.
     * Aktualisiert die gespeicherte Position bei Veränderung.
     */
    public boolean checkPositionBlocked(int currentX, int currentY) {
        if (currentX == getLastX() && currentY == getLastY()) {
            System.out.println("❌ Bewegung blockiert - Position unverändert");
            return false; // Position hat sich NICHT verändert
        } else {
            setLastX(currentX);
            setLastY(currentY);
            System.out.println("✅ Bewegung erfolgreich zu (" + currentX + ", " + currentY + ")");
            return true; // Position hat sich geändert
        }

    } // boolean moved = checkPositionChanged(x, y);



    @PostConstruct
    public void init() {
        DefaultApi defaultApi = new DefaultApi();
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("testGame");
        GameDto response = defaultApi.gamePost(gameInput);

        BigDecimal gameId = response.getGameId();

        System.out.println(response);

        DirectionDto direction = DirectionDto.UP; // oder RIGHT, DOWN, LEFT

        // switchcase Anwendung für Richtung.
        switch (direction) {
            case UP -> {
                MoveInputDto move = new MoveInputDto();
                move.setDirection(DirectionDto.UP);
                MoveDto result = defaultApi.gameGameIdMovePost(gameId, move);
                System.out.println("Zug: UP -> " + result.getMoveStatus());
            }
            case RIGHT -> {
                MoveInputDto move = new MoveInputDto();
                move.setDirection(DirectionDto.RIGHT);
                MoveDto result = defaultApi.gameGameIdMovePost(gameId, move);
                System.out.println("Zug: RIGHT -> " + result.getMoveStatus());
            }
            case DOWN -> {
                MoveInputDto move = new MoveInputDto();
                move.setDirection(DirectionDto.DOWN);
                MoveDto result = defaultApi.gameGameIdMovePost(gameId, move);
                System.out.println("Zug: DOWN -> " + result.getMoveStatus());
            }
            case LEFT -> {
                MoveInputDto move = new MoveInputDto();
                move.setDirection(DirectionDto.LEFT);
                MoveDto result = defaultApi.gameGameIdMovePost(gameId, move);
                System.out.println("Zug: LEFT -> " + result.getMoveStatus());
            }
        }

        boolean gameActive = true;

        // Hier wird eine Schleife erzeug welche läuft bis das Spiel zuende
        // Der Code ist eine Abfrage verschachteltung. Die wichtigste Abfrage kommt zuerst: Im Spielfeld bleiben
        // Die weiteren Abfragen schlagen eine Andere Richtung ein, sobald auf ein Hinterniss gestoßen wird.

        boolean blocked = false;


        boolean canMoveUp = true;
        boolean canMoveRIGHT = true;
        boolean canMoveLEFT = true;
        boolean canMoveDOWN = true;

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

                // Spielfeldrahmen

                // Prüfe ob UP Valide
                status = defaultApi.gameGameIdGet(gameId);
                int y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position
                int x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position


                //--------- moveUP
                status = defaultApi.gameGameIdGet(gameId);
                y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position

                if (y == 5) {
                    System.out.println("Am oben Rand – Up blockiert.");
                    canMoveUp = false;
                }
             /**   while (canMoveUp && !blocked) {

                    status = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position

                    if (y == 5) {
                        System.out.println("Am oben Rand – Up blockiert.");
                        canMoveUp= false;
                    }

                    MoveInputDto moveUp = new MoveInputDto();
                    moveUp.setDirection(DirectionDto.UP);
                    MoveDto result1 = defaultApi.gameGameIdMovePost(gameId, moveUp);
                    System.out.println(" Zug (UP): " + result1);

                    GameDto Status = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position
                    x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position
                    blocked = checkPositionBlocked(x, y);
                    System.out.println(" Blockgelesen: " + result1);

                } **/

                blocked = false;

                //--------- moveRIGHT
                status = defaultApi.gameGameIdGet(gameId);
                x = status.getPosition().getPositionX().intValue();  // aktuelle y-Position

                if (x == 5) {
                    System.out.println("Am rechten Rand – RIGHT blockiert.");
                    canMoveRIGHT = false;
                }else {
                    canMoveRIGHT = true;}

                while (canMoveRIGHT && !blocked) {

                    status = defaultApi.gameGameIdGet(gameId);
                    x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position

                    if (x == 5) {
                        System.out.println("Am rechten Rand – RIGHT blockiert.");
                        canMoveRIGHT = false;
                    }

                    MoveInputDto moveRight = new MoveInputDto();
                    moveRight.setDirection(DirectionDto.RIGHT);
                    MoveDto result2 = defaultApi.gameGameIdMovePost(gameId, moveRight);
                    System.out.println(" Zug (RIGHT): " + result2);

                    status = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position
                    x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position
                    blocked = checkPositionBlocked(x, y);
                }

                blocked = false;

                // ------- MoveLeft
                status = defaultApi.gameGameIdGet(gameId);
                x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position
                if (x == 1) {
                    System.out.println("Am linken Rand – LEFT blockiert.");
                    canMoveLEFT = false;
                }

                while (canMoveLEFT && !blocked) {

                    status = defaultApi.gameGameIdGet(gameId);
                    x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position

                    if (x == 1) {
                        System.out.println("Am linken Rand – LEFT blockiert.");
                        canMoveLEFT = false;
                    }

                    MoveInputDto moveLeft = new MoveInputDto();
                    moveLeft.setDirection(DirectionDto.LEFT);
                    MoveDto result4 = defaultApi.gameGameIdMovePost(gameId, moveLeft);
                    System.out.println(" Zug (DOWN): " + result4);

                    GameDto newStatus = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position
                    x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position
                    blocked = checkPositionBlocked(x, y);
                }

                blocked = false;

                //--------- moveDOWN
                status = defaultApi.gameGameIdGet(gameId);
                y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position

                if (y == 1) {
                    System.out.println("Am oberen Rand – UP blockiert.");
                    canMoveDOWN = false;
                }

                while (canMoveDOWN && !blocked) {

                    status = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position

                    if (y == 1) {
                        System.out.println("Am oberen Rand – UP blockiert.");
                        canMoveDOWN = false;
                    }

                    MoveInputDto moveDown = new MoveInputDto();
                    moveDown.setDirection(DirectionDto.DOWN);
                    MoveDto result3 = defaultApi.gameGameIdMovePost(gameId, moveDown);
                    System.out.println(" Zug (DOWN): " + result3);

                    GameDto newStatus = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position
                    x = status.getPosition().getPositionX().intValue();  // aktuelle x-Position
                    blocked = checkPositionBlocked(x, y);

                    status = defaultApi.gameGameIdGet(gameId);
                    y = status.getPosition().getPositionY().intValue();  // aktuelle y-Position

                    if (y == 1) {
                        System.out.println("Am oberen Rand – UP blockiert.");
                        canMoveDOWN = false;
                    }
                }


                    }
                }
            }
        }




