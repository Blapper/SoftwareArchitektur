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


    public boolean checkPositionBlocked(int currentX, int currentY) {
        if (currentX == getLastX() && currentY == getLastY()) {
            System.out.println("❌ Bewegung blockiert - Position unverändert"+"(" + currentX + ", " + currentY + ")");
            return false; // Position hat sich NICHT verändert
        } else {
            setLastX(currentX);
            setLastY(currentY);
            System.out.println("✅ Bewegung erfolgreich zu (" + currentX + ", " + currentY + ")");
            return false; // Position hat sich geändert
        }

    } // boolean moved = checkPositionChanged(x, y);


    public boolean checkBorderUpRight ( int z) {
        if ( z == 5){
            System.out.println("Grenze erreicht. Wert "+z);
            return false;
        }else{
            System.out.println(" Weg Frei. Wert: "+z);
            return true;
        }
    }

    public boolean checkBorderDownLeft ( int z) {
        if ( z == 1){
            return false;
        }else{
            return true;
        }
    }



    @PostConstruct
    public void init() {
        DefaultApi defaultApi = new DefaultApi();
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("OlafRose");
        GameDto response = defaultApi.gamePost(gameInput);

        BigDecimal gameId = response.getGameId();

        System.out.println(response);

        // switchcase Anwendung für Richtung.
        boolean gameActive = true;

        // Hier wird eine Schleife erzeug welche läuft bis das Spiel zuende
        // Der Code ist eine Abfrage verschachteltung. Die wichtigste Abfrage kommt zuerst: Im Spielfeld bleiben
        // Die weiteren Abfragen schlagen eine Andere Richtung ein, sobald auf ein Hinterniss gestoßen wird.

        boolean blocked = false;


        boolean canMoveUp = true;
        boolean canMoveRight = true;
        boolean canMoveLeft = true;
        boolean canMoveDown = true;

        // Prarameter für koordinierung

        int y = 1;
        int x = 1;
        int posY = 0;

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


            //--------- moveUP

            canMoveUp = checkBorderUpRight(y);

           while (canMoveUp) {

               canMoveUp = checkBorderUpRight(y);

               if (canMoveUp == true && blocked == false) {
                   MoveInputDto moveUp = new MoveInputDto();
                   moveUp.setDirection(DirectionDto.UP);
                   MoveDto result1 = defaultApi.gameGameIdMovePost(gameId, moveUp);
                   System.out.println(" Zug (UP): " + result1);
                   //y++;
                   System.out.println(" Zug (UP): Y ist " + y);
                   posY = result1.getPositionAfterMove().getPositionY().intValue();
                   System.out.println(" Text Y hat den Wert:" + posY);
               }

               blocked = checkPositionBlocked(x, y);
               if (blocked){
                y--;
                    }

           }

                blocked = false;


                //--------- moveRIGHT

                canMoveRight = checkBorderUpRight(x);
                System.out.println(" Prüfe ob (RIGHT) möglich ");
                while (canMoveRight && !blocked) {

                    System.out.println(" Prüfe ob (RIGHT) möglich, in der Schleife ");
                    canMoveRight = checkBorderUpRight(x);

                    if (canMoveRight == true && blocked == false) {
                        MoveInputDto moveRight = new MoveInputDto();
                        moveRight.setDirection(DirectionDto.RIGHT);
                        MoveDto result2 = defaultApi.gameGameIdMovePost(gameId, moveRight);
                        System.out.println(" Zug (RIGHT): " + result2);

                        x++;

                    }

                    blocked = checkPositionBlocked(x, y);
                    if (!blocked){
                    // x--;
                    }
                }

                blocked = false;

                // ------- MoveLeft

                canMoveLeft = checkBorderDownLeft(x);

                while (canMoveLeft) {

                    canMoveLeft = checkBorderDownLeft(x);

                    if (canMoveLeft == true && blocked == false) {
                        MoveInputDto moveLeft = new MoveInputDto();
                        moveLeft.setDirection(DirectionDto.LEFT);
                        MoveDto result4 = defaultApi.gameGameIdMovePost(gameId, moveLeft);
                        System.out.println(" Zug (Left): " + result4);
                        x--;
                    }

                    blocked = checkPositionBlocked(x, y);
                    if (!blocked)
                    { x++;}

                }

                blocked = false;

                //--------- moveDOWN

                canMoveDown = checkBorderDownLeft(y);

                while (canMoveDown) {

                    canMoveDown = checkBorderDownLeft(y);

                    if (canMoveDown == true && blocked == false) {
                        MoveInputDto moveDown = new MoveInputDto();
                        moveDown.setDirection(DirectionDto.DOWN);
                        MoveDto result3 = defaultApi.gameGameIdMovePost(gameId, moveDown);
                        System.out.println(" Zug (DOWN): " + result3);
                        y--;
                    }

                    blocked = checkPositionBlocked(x, y);
                    if (!blocked)
                    { y++;}
                }

                blocked = false;

            }
        }

    }
}





