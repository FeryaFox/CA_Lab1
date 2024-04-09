package ru.feryafox.FeryaASM;

import ru.feryafox.FeryaASM.enums.CommandType;
import ru.feryafox.FeryaASM.enums.Register;
import ru.feryafox.FeryaASM.exceptions.InvalidOperands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FeryaASM {
    private HashMap<Register, Integer> registers = new HashMap<>();
    private ArrayList<Command> controlFlow = new ArrayList<>();
    public FeryaASM(String commands){
        String[] commands_ = commands.split("\\n");
        for (int i = 0; i < commands_.length; i++){
            if (commands_[i].startsWith(";")) continue;
            addCommandToFlow(commands_[i], i + 1);
        }
        System.out.println(5);
    }
    private void addCommandToFlow(String command_line, int lineCode){
        String command = command_line.split(" ")[0];
        String[] operands = Arrays.stream(command_line.split("\s+"))
                .skip(1)
                .map(s -> s.replace(",", ""))
                .toArray(String[]::new);
        switch (command){
            case "mov":
                if (operands.length != 2) throw new InvalidOperands(CommandType.MOV, lineCode);
                controlFlow.add(new Command(CommandType.MOV, operands));
                break;
            case "add":
                if (operands.length != 2) throw new InvalidOperands(CommandType.ADD, lineCode);
                controlFlow.add(new Command(CommandType.ADD, operands));
                break;
            case "sub":
                if (operands.length != 2) throw new InvalidOperands(CommandType.SUB, lineCode);
                controlFlow.add(new Command(CommandType.SUB, operands));
                break;
        }
    }
}


class Command{
    private CommandType command = null;
    private String[] operands = null;

    public Command(CommandType command, String[] operands){
        this.command = command;
        this.operands = operands.clone();
    }


    public CommandType getCommand() {
        return command;
    }

    public String[] getOperands() {
        return operands;
    }

}
