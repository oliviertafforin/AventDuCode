package org.olivier.Day9;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Day9b {
    public static void main(String[] args) throws IOException {
        char[] input = Utils.getFileContent("input_d9.txt").toCharArray();
        List<Block> blocks = new ArrayList<>(), full = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            if (i % 2 == 0) {
                blocks.add(new Block(i / 2, input[i] - '0', 0));
                full.add(new Block(i / 2, input[i] - '0', 0));
            } else {
                blocks.get(blocks.size() - 1).free = input[i] - '0';
                full.get(full.size() - 1).free = input[i] - '0';
            }
        }

        int front = 0, back = blocks.size() - 1;
        while (front < back) {
            Block f = blocks.get(front), b = blocks.get(back);

            while (f.free > 0) {
                if (b.length <= 0) {
                    blocks.remove(b);
                    b = blocks.get(--back);
                    if (front == back) {
                        break;
                    }
                }

                f.space.add(b.id);
                f.free--;
                b.length--;
            }

            front++;
        }

        back = full.size() - 1;
        while (back > 0) {
            front = 0;
            while (front < back) {
                if (full.get(front).free >= full.get(back).length) {
                    for (int i = 0; i < full.get(back).length; i++) {
                        full.get(front).space.add(full.get(back).id);
                    }
                    full.get(front).free -= full.get(back).length;
                    full.get(back).id = 0;
                    break;
                }
                front++;
            }
            back--;
        }

        long total1 = 0;
        long pos = 0;
        for (Block b : blocks) {
            for (int i = 0; i < b.length; i++) {
                total1 += pos * b.id;
                pos++;
            }
            for (int n : b.space) {
                if (n < 0) {
                    break;
                }
                total1 += pos * n;
                pos++;
            }
        }

        long total2 = 0;
        pos = 0;
        for (Block b : full) {
            for (int i = 0; i < b.length; i++) {
                total2 += pos * b.id;
                pos++;
            }
            for (int n : b.space) {
                if (n >= 0) {
                    total2 += pos * n;
                }
                pos++;
            }
            pos += b.free;
        }

        System.out.println("Day 9:");
        System.out.println(total1);
        System.out.println(total2);
    }

    static class Block {
        int id, length, free;
        List<Integer> space;

        public Block(int id, int length, int free) {
            this.id = id;
            this.length = length;
            this.free = free;
            space = new ArrayList<>();
        }

        public String toString() {
            return id + "x" + length + "+" + space;
        }
    }

}
