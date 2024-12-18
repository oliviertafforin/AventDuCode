package org.olivier.Day13;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class Day13 {
    public static void main(String[] args) throws IOException {
        final boolean PART_1 = true;

        List<String> input = List.of(Utils.getFileContent("input_d13.txt").split("\n\n"));
        BigInteger total = BigInteger.ZERO;
        for (String s : input) {
            List<String> systeme = List.of(s.split("\n"));
            String a = systeme.get(0);
            String b = systeme.get(1);
            String p = systeme.get(2);

            int indexXa = a.indexOf("+", a.indexOf("X"));
            int indexYa = a.indexOf("+", a.indexOf("Y"));

            int indexXb = b.indexOf("+", b.indexOf("X"));
            int indexYb = b.indexOf("+", b.indexOf("Y"));

            int indexXp = p.indexOf("=", p.indexOf("X"));
            int indexYp = p.indexOf("=", p.indexOf("Y"));

            // Extraire les valeurs
            BigInteger a1 = BigInteger.valueOf(Integer.parseInt(a.substring(indexXa + 1, a.indexOf(",", indexXa)).trim()));
            BigInteger a2 = BigInteger.valueOf(Integer.parseInt(a.substring(indexYa + 1).trim()));

            BigInteger b1 = BigInteger.valueOf(Integer.parseInt(b.substring(indexXb + 1, b.indexOf(",", indexXb)).trim()));
            BigInteger b2 = BigInteger.valueOf(Integer.parseInt(b.substring(indexYb + 1).trim()));

            BigInteger c1 = new BigInteger(p.substring(indexXp + 1, p.indexOf(",", indexXp)).trim());

            BigInteger c2 = new BigInteger(p.substring(indexYp + 1).trim());
            if (!PART_1) {
                c1 = c1.add(new BigInteger("10000000000000"));
                c2 = c2.add(new BigInteger("10000000000000"));
            }

//            BigInteger  c1 = new BigInteger(p.substring(indexXp + 1, p.indexOf(",", indexXp)).trim());
//            BigInteger  c2 = new BigInteger(p.substring(indexYp + 1).trim());

            // Réduire les coefficients en divisant par le PGCD des constantes


            // Plage pour les solutions possibles (ajustez selon vos besoins)
            BigInteger minRange = BigInteger.valueOf(0);
            BigInteger maxRange = BigInteger.valueOf(200);

            // Rechercher les solutions entières
//            total = total.add(findIntegerSolutions(a1, b1, c1, a2, b2, c2, minRange, maxRange));
            total = total.add(solveLinearEquations(a1, b1, c1, a2, b2, c2));
        }
        ;
        System.out.println(total);
    }

    public static BigInteger solveLinearEquations(BigInteger a1, BigInteger b1, BigInteger c1, BigInteger a2, BigInteger b2, BigInteger c2) {
        // Calcul du déterminant principal
        BigInteger determinant;
        if (a1.multiply(b2).subtract(a2.multiply(b1)).compareTo(BigInteger.ZERO) >= 0) {
            determinant = a1.multiply(b2).subtract(a2.multiply(b1));
        } else {
            determinant = a2.multiply(b1).subtract(a1.multiply(b2));
        }

        if (determinant.equals(BigInteger.ZERO)) {
//            System.out.println("Le système n'a pas de solution unique (soit infini, soit incompatible).");
            return BigInteger.ZERO;
        } else {
            // Calcul des déterminants pour x et y
            BigInteger determinantX;
            BigInteger determinantY;

            if (c1.multiply(b2).subtract(c2.multiply(b1)).compareTo(BigInteger.ZERO) >= 0) {
                determinantX = c1.multiply(b2).subtract(c2.multiply(b1));

            } else {
                determinantX = c2.multiply(b1).subtract(c1.multiply(b2));

            }

            if (a1.multiply(c2).subtract(a2.multiply(c1)).compareTo(BigInteger.ZERO) >= 0) {
                determinantY = a1.multiply(c2).subtract(a2.multiply(c1));
            } else {
                determinantY = a2.multiply(c1).subtract(a1.multiply(c2));
            }


            // Solutions
            if (determinantX.remainder(determinant).equals(BigInteger.ZERO) && determinantY.remainder(determinant).equals(BigInteger.ZERO)) {
                BigInteger x = determinantX.divide(determinant);
                BigInteger y = determinantY.divide(determinant);
//                System.out.println("Plus petite solution trouvée : x = " + x + ", y = " + y);
                return x.multiply(new BigInteger("3")).add(y);
            } else {
//                System.out.println("Le système n'a pas de solution entière.");
                return BigInteger.ZERO;
            }
        }
    }


    public static BigInteger findIntegerSolutions(BigInteger a1, BigInteger b1, BigInteger c1,
                                                  BigInteger a2, BigInteger b2, BigInteger c2,
                                                  BigInteger minRange, BigInteger maxRange) {

        BigInteger solutionX = new BigInteger("999999999999");
        BigInteger solutionY = new BigInteger("999999999999");

        for (BigInteger x = minRange; x.compareTo(maxRange) <= 0; x = x.add(BigInteger.ONE)) {
            for (BigInteger y = minRange; y.compareTo(maxRange) <= 0; y = y.add(BigInteger.ONE)) {
                if (a1.multiply(x).add(b1.multiply(y)).equals(c1) &&
                        a2.multiply(x).add(b2.multiply(y)).equals(c2)) {
//                    System.out.println("Solution trouvée : x = " + x + ", y = " + y);
                    if (x.multiply(new BigInteger("3")).add(y).compareTo(solutionX.multiply(new BigInteger("3")).add(solutionY)) <= 0) {
                        solutionY = y;
                        solutionX = x;
                    }
                }
            }
        }

        if (solutionX.compareTo(new BigInteger("999999999999")) == 0) {
//            System.out.println("Aucune solution entière trouvée dans la plage donnée.");
            return BigInteger.ZERO;
        } else {
//            System.out.println("Plus petite solution trouvée : x = " + solutionX + ", y = " + solutionY);
            return solutionX.multiply(new BigInteger("3")).add(solutionY);
        }
    }
}
