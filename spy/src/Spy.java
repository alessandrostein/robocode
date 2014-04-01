
import java.util.Iterator;
import robocode.AdvancedRobot;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.RadarTurnCompleteCondition;
import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alessandrostein
 */
public class Spy extends AdvancedRobot {

    /*    double moveAmount;
     boolean peek;

     final double veryFar = 9999.0;
     final double quarterTurn = 90.0;
     final double threeQuarterTurn = 270.0;
     final double fullTurn = 360.0;

     boolean clockwise = true;

     public void run() {
     //ahead(10);
     // Initialize moveAmount to the maximum possible for this battlefield.
     moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

     //scan();
     //back(50);
     // Initialize peek to false
     peek = false;

     // turnLeft to face a wall.
     // getHeading() % 90 means the remainder of
     // getHeading() divided by 90.
     turnLeft(getHeading() % 90);
     ahead(moveAmount-10);
     // Turn the gun to turn right 90 degrees.
     peek = true;
     turnGunRight(90);
     turnRight(90);

     while (true) {
     waitFor(new TurnCompleteCondition(this));
     //toggleDirection();
     // Look before we turn when ahead() completes.
     peek = true;
     // Move up the wall
     ahead(moveAmount);
     // Don't look now
     peek = false;
     // Turn to the next wall
     turnRight(90);
     }
     }

     @Override
     public void onScannedRobot(ScannedRobotEvent e) {
     //turnGunLeft(e.getBearing());
     //if (e.getHeading() == getX()) {
     if (e.getDistance() > 100) {
     fire(3);
     } else {
     fire(1);
     }
     //}

     if (peek) {
     scan();
     }
     }

     @Override
     public void onHitRobot(HitRobotEvent e) {
     // If he's in front of us, set back up a bit.
     if (e.getBearing() > -90 && e.getBearing() < 90) {
     back(100);
     } // else he's in back of us, so set ahead a bit.
     else {
     ahead(100);
     }
     }


     @Override
     public void onHitByBullet(HitByBulletEvent e) {
     turnLeft(90 - e.getBearing());
     }*/
    double previousEnergy = 100;
    int movementDirection = 1;
    int gunDirection = 1;

    @Override
    public void run() {
        setTurnGunRight(99999);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // Stay at right angles to the opponent
        setTurnRight(e.getBearing() + 90 - 30 * movementDirection);

        // If the bot has small energy drop,
        // assume it fired
        double changeInEnergy = previousEnergy - e.getEnergy();
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            // Dodge!
            movementDirection = -movementDirection;
            setAhead((e.getDistance() / 4 + 25) + movementDirection);
        }
        // When a bot is spotted,
        // sweep the gun and radar
        gunDirection = -gunDirection;
        setTurnGunRight(99999 * gunDirection);

        // Fire directly at target
        if (e.getDistance() < 140) {
            if (getGunHeat() == 0) {
                fire(2.5);
            }
        }

        fire(1);
        // Track the energy level
        previousEnergy = e.getEnergy();

        scan();

    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        setTurnRight(100);
        ahead(Math.random() + 100);
    }

}