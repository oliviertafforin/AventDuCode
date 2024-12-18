package org.olivier.Day14;

import org.olivier.Utils.Tuple;
import org.olivier.Utils.Utils;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day14 {
    public static final Integer WIDE = 101;
    public static final Integer TALL = 103;
    public static long lowestDanger = 230172768L;
    public static int iterationLowestDanger = 0;
    public static final boolean PART_1 = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> input = List.of(Utils.getFileContent("input_d14.txt").split("\n"));
        List<Robot> robots = new ArrayList<>();
        RobotViewer viewer = new RobotViewer(robots);
        // Création de la fenêtre
        JFrame frame = new JFrame("Affichage des coordonnées");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(viewer);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);
        int quadrant1 = 0;
        int quadrant2 = 0;
        int quadrant3 = 0;
        int quadrant4 = 0;
        for (String s : input) {
            int indexXa = s.indexOf("=", s.indexOf("p"));
            int indexYa = s.indexOf(",", s.indexOf("p"));

            int indexXb = s.indexOf("=", s.indexOf("v"));
            int indexYb = s.indexOf(",", s.indexOf("v"));
            // Extraire les valeurs
            Integer px = Integer.parseInt(s.substring(indexXa + 1, s.indexOf(",", indexXa)).trim());
            Integer py = Integer.parseInt(s.substring(indexYa + 1, s.indexOf(" ")).trim());

            Integer vx = Integer.parseInt(s.substring(indexXb + 1, s.indexOf(",", indexXb)).trim());
            Integer vy = Integer.parseInt(s.substring(indexYb + 1).trim());
            robots.add(new Robot(new Tuple.Pair<>(px, py), new Tuple.Pair<>(vx, vy)));
        }
        for (int i = 0; i < (PART_1 ? 101 : 8088); i++) {
            oneMoreSecond(robots, viewer, i);
        }

        for (Robot robot : robots) {
            if (robot.getCurPos().v0() > (WIDE / 2)) {
                if (robot.getCurPos().v1() > (TALL / 2)) {
                    quadrant4++;
                } else if (robot.getCurPos().v1() < (TALL / 2)) {
                    quadrant2++;
                }
            } else if (robot.getCurPos().v0() < (WIDE / 2)) {
                if (robot.getCurPos().v1() > (TALL / 2)) {
                    quadrant3++;
                } else if (robot.getCurPos().v1() < (TALL / 2)) {
                    quadrant1++;
                }
            }
        }

        System.out.println(quadrant2 * quadrant1 * quadrant3 * quadrant4);
    }

    private static void oneMoreSecond(List<Robot> robots, RobotViewer viewer, int i) throws InterruptedException {
//Thread.sleep(300);
        int quadrant1 = 0;
        int quadrant2 = 0;
        int quadrant3 = 0;
        int quadrant4 = 0;
        if (i == 0) return;
        for (Robot r : robots) {
            int posX = r.getCurPos().v0();
            int posY = r.getCurPos().v1();

            //Go x
            for (int x = 0; x < Math.abs(r.getVelocity().v0()); x++) {
                if (r.getVelocity().v0() >= 0) {
                    posX++;
                } else {
                    posX--;
                }
                if (posX < 0) {
                    posX = WIDE - 1;
                }
                if (posX >= WIDE) {
                    posX = 0;
                }
            }
            //Go y
            for (int y = 0; y < Math.abs(r.getVelocity().v1()); y++) {
                if (r.getVelocity().v1() >= 0) {
                    posY++;
                } else {
                    posY--;
                }
                if (posY < 0) {
                    posY = TALL - 1;
                }
                if (posY >= TALL) {
                    posY = 0;
                }
            }
            r.setCurPos(new Tuple.Pair<>(posX, posY));
            if (r.getCurPos().v0() > (WIDE / 2)) {
                if (r.getCurPos().v1() > (TALL / 2)) {
                    quadrant4++;
                } else if (r.getCurPos().v1() < (TALL / 2)) {
                    quadrant2++;
                }
            } else if (r.getCurPos().v0() < (WIDE / 2)) {
                if (r.getCurPos().v1() > (TALL / 2)) {
                    quadrant3++;
                } else if (r.getCurPos().v1() < (TALL / 2)) {
                    quadrant1++;
                }
            }
        }
        long currentDanger = (long) quadrant2 * quadrant1 * quadrant3 * quadrant4;
        if (lowestDanger > currentDanger) {
            lowestDanger = currentDanger;
            iterationLowestDanger = i;
        }
        if ((currentDanger < 50000000 && i > 8000)) {
            Thread.sleep(1500);
        }
        viewer.update(robots, i);

//        oneMoreSecond(robots, viewer, i - 1);
    }
}
