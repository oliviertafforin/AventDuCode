package org.olivier.Day23;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.*;

public class Day23 {
    private static List<List<Computer>> INTERCONNECTIONS = new ArrayList<>();

    static class Computer {
        private String nom;
        private List<Computer> interconnected;

        public Computer(String nom) {
            this.nom = nom;
            this.interconnected = new ArrayList<>();
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public List<Computer> getInterconnected() {
            return interconnected;
        }

        public void setInterconnected(List<Computer> interconnected) {
            this.interconnected = interconnected;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Computer computer = (Computer) o;
            return nom.equals(computer.nom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nom);
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> liste = List.of(Utils.getFileContent("input_d23.txt").split("\n"));

        Set<Computer> computers = new HashSet<>();
        liste.forEach(s -> {
            Computer c1 = computers.stream().filter(c -> c.getNom().equals(s.split("-")[0])).findAny().orElse(new Computer(s.split("-")[0]));
            Computer c2 = computers.stream().filter(c -> c.getNom().equals(s.split("-")[1])).findAny().orElse(new Computer(s.split("-")[1]));
            c1.getInterconnected().add(c2);
            c2.getInterconnected().add(c1);
            computers.add(c1);
            computers.add(c2);

        });

        //PARTIE 1 (warning : pas propre)
        for (Computer computer : computers) {
            for (Computer connected : computer.getInterconnected()) {
                List<Computer> connectionsCommunes = computer.getInterconnected().stream()
                        .filter(connected.getInterconnected()::contains).toList();
                for (Computer c : connectionsCommunes) {
                    List<Computer> temp = new ArrayList<>();
                    temp.add(computer);
                    temp.add(c);
                    temp.add(connected);

                    if (!dejaPresente(temp) && containsChiefComputer(temp)) {
                        INTERCONNECTIONS.add(temp);
                    }
                }

            }
        }

        System.out.println("Part 1 : " + INTERCONNECTIONS.size());
    }

    private static boolean containsChiefComputer(List<Computer> temp) {
        return temp.stream().anyMatch(c -> c.getNom().startsWith("t"));
    }

    private static boolean dejaPresente(List<Computer> connections) {
        return INTERCONNECTIONS.stream().anyMatch(i -> {
            var identique = true;
            for (Computer connection : connections) {
                if (!i.contains(connection)) {
                    return false;
                }
            }
            return identique;
        });
    }
}
