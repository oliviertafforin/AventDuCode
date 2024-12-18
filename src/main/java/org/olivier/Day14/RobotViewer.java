package org.olivier.Day14;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RobotViewer extends JPanel {

    private List<Robot> robots;
    private int n = 0;
    private int i = 0;

    // Constructeur pour passer la liste des points
    public RobotViewer(List<Robot> robots) {
        this.robots = robots;
        setPreferredSize(new Dimension(1024, 768)); // Taille de la fenêtre
    }

    // Méthode pour mettre à jour les points
    public void update(List<Robot> robots, int iteration) {
        this.robots = new ArrayList<>(robots);
        n++;
        this.i = iteration;
        repaint(); // Redessine l'interface graphique
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Anti-aliasing pour un affichage plus fluide
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner les points
        g2d.setColor(Color.GREEN);
        for (Robot robot : robots) {
            int x = robot.getCurPos().v0();
            int y = robot.getCurPos().v1();
            g2d.fillOval((x - 2) * 4, (y - 2) * 4, 4, 4); // Dessiner un petit cercle
        }
        // Afficher le compteur
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(String.valueOf(n), 10, 20);
        g2d.drawString(String.valueOf(i), 10, 40);
    }
}
