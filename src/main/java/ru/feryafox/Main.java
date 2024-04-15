package ru.feryafox;

import ru.feryafox.FeryaASM.FeryaASM;
import ru.feryafox.FeryaASM.exceptions.InvalidOperandsArgument;


public class Main {
    public static void main(String[] args) throws InvalidOperandsArgument {
        String source = """
                mov bx, 10 ;A
                mov ax, 2
                mov cx, 2
                mul cx
                                
                sub bx, ax
                mov ax, bx 
                mov cx, 5
                mul cx
                                
                                
                mov bx, 50
                sub bx, ax
                                
                add bx, 2
      
                                
                """;


        FeryaASM feryaASM = new FeryaASM(source);
        try {
            while (!feryaASM.isLast()) {
                feryaASM.tick();
            }
        }
        catch (InvalidOperandsArgument e){
            throw e;
        }
        System.out.println(feryaASM.getRegisterStringNotBinary());
    }
}