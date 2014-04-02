
import java.awt.Color;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
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

    double moveAmount;

    @Override
    public void run() {
        // Set colors
        setBodyColor(Color.black);
        setGunColor(Color.black);
        setRadarColor(Color.GRAY);
        setBulletColor(Color.cyan);
        setScanColor(Color.cyan);

        // Initialize moveAmount to the maximum possible for this battlefield.
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

        turnLeft(getHeading() % 90);
        ahead(moveAmount);

        // Turn the gun to turn right 90 degrees.
        turnGunRight(90);
        turnRight(90);

        while (true) {
            //scan();
            //moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight()) - 10;
            // Move up the wall
            ahead(moveAmount);
            // Turn to the next wall
            turnRight(90);
            turnGunRight(270);

            back(moveAmount);

            turnLeft(90);
            turnGunLeft(270);

            //scan();
        }

    }

    double previousEnergy = 100;
    int movementDirection = 1;
    int gunDirection = 1;

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // Para mirar o radar no adversário
        //turnRadarRight(angleRelative(e.getBearing() + e.getHeading() - getRadarHeading()));
        // Para mirar o canhão no adverário
        //turnGunRight(angleRelative(e.getBearing() + e.getHeading() - getGunHeading()));
        // Virar o robo em direção ao adversario
        //turnRight(angleRelative(e.getBearing()));

        // Ajusta mira
        aim(e.getBearing());

        if (e.getEnergy() < 20) {
            fatalShooting(e.getEnergy());
        } else {
            shooting(e.getDistance());
        }

        scan();
        
        ahead(moveAmount - ((moveAmount / 100) + 1));

        scan();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        //setTurnRight(100);
        //ahead(Math.random() + 100);
        
        turnGunLeft(e.getBearing());
        
        scan();
        
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight()) - 10;
        // Move up the wall
        ahead(moveAmount);
        // Turn to the next wall
        turnRight(90);
        
        //turnGunLeft(270);
        
        scan();

        back(100);
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
    // Em caso de ficar um longo tempo sem ação.
    public void onSkippedTurn(SkippedTurnEvent e) {
        //turnGunLeft(360);
    }

    // Buscar angulo do oponente.
    public double angleRelative(double valor) {
        double rel = valor;

        while (rel <= -180) {
            rel += 360;
        }

        while (valor > 180) {
            rel -= 360;
        }
        return valor;

    }

    // Ajustar a mira
    public void aim(double valor) {
        double adv = getHeading() + valor - getGunHeading();
        if (!(adv > -180 && adv <= 180)) {
            while (adv <= -180) {
                adv += 360;
            }
            while (adv > 180) {
                adv -= 360;
            }
        }

        turnGunRight(adv);

    }

    // Tiro fatal
    public void fatalShooting(double energia) {
        double tiro = (energia / 4) + .1;
        fire(tiro);

    }

    // Tiro com economia de energia
    public void shooting(double distancia) {
        if (distancia > 200 || getEnergy() < 15) {
            fire(1);
        } else if (distancia > 50) {
            fire(2);
        } else {
            fire(2);
        }
    }

}
