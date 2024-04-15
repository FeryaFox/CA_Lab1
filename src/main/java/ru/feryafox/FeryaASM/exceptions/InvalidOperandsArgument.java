package ru.feryafox.FeryaASM.exceptions;

import ru.feryafox.FeryaASM.enums.CommandType;

public class InvalidOperandsArgument extends Exception{
    public InvalidOperandsArgument(CommandType commandType){
        super("Неверный аргумент в операнде " + commandType);
    }
}
