package ru.feryafox.FeryaASM.exceptions;

import ru.feryafox.FeryaASM.enums.CommandType;

public class InvalidOperands extends RuntimeException{
    public InvalidOperands(CommandType command, int codeLine){
        super("Неверный аргуемент в команде " + command + " в строке " + codeLine);
    }
}
