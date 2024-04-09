package ru.feryafox;

import ru.feryafox.FeryaASM.FeryaASM;


public class Main {
    public static void main(String[] args) {
        String source = "mov ax, bx\nadd ff, 10h";

        FeryaASM feryaASM = new FeryaASM(source);
    }
}