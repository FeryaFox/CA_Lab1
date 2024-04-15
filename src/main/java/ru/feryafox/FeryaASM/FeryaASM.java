package ru.feryafox.FeryaASM;

import org.sk.PrettyTable;
import ru.feryafox.FeryaASM.enums.CommandType;
import ru.feryafox.FeryaASM.enums.Register;
import ru.feryafox.FeryaASM.exceptions.InvalidOperands;
import ru.feryafox.FeryaASM.exceptions.InvalidOperandsArgument;
import ru.feryafox.FeryaASM.utils.RegisterUtils;
import ru.feryafox.FeryaASM.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class FeryaASM {
    private HashMap<Register, Short> registers = new HashMap<>();
    private ArrayList<Command> controlFlow = new ArrayList<>();
    private Integer nextCommandAdress = 0;

    public void tick() throws InvalidOperandsArgument {
        Command currentCommand = controlFlow.get(nextCommandAdress);
        executeCommand(currentCommand);
        nextCommandAdress++;
    }

    public boolean isLast(){
        return nextCommandAdress == controlFlow.size();
    }

    private void executeCommand(Command command) throws InvalidOperandsArgument {
        switch (command.getCommand()){
            case ADD -> add(command.getOperands());
            case MOV -> mov(command.getOperands());
            case SUB -> sub(command.getOperands());
            case MUL -> mul(command.getOperands());
            case DIV -> div(command.getOperands());
        }
    }

    public FeryaASM(String commands){
        String[] commands_ = commands.split("\\n");
        for (int i = 0; i < commands_.length; i++){
            if (commands_[i].startsWith(";")) continue;
            addCommandToFlow(commands_[i], i + 1);
        }
        fillRegisters();

    }

    private void addCommandToFlow(String command_line, int lineCode){
        String command = command_line.split(" ")[0];
        String[] operands = Arrays.stream(command_line.split("\s+"))
                .skip(1)
                .map(s -> s.replace(",", ""))
                .map(StringUtils::deleteComment)
                .filter(s -> !s.isEmpty())
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
            case "mul":
                if (operands.length != 1) throw new InvalidOperands(CommandType.MUL, lineCode);
                controlFlow.add(new Command(CommandType.MUL, operands));
                break;
            case "div":
                if (operands.length != 1) throw new InvalidOperands(CommandType.DIV, lineCode);
                controlFlow.add(new Command(CommandType.DIV, operands));
                break;
        }
    }

    public String getRegistersString(){
        PrettyTable table = new PrettyTable("Register", "Value", "High", "Value", "Low", "Value");

        table.addRow(
                Register.AX.name(), getRegisterBinary(Register.AX),
                Register.AH.name(), getRegisterBinary(Register.AH),
                Register.AL.name(), getRegisterBinary(Register.AL)
        );
        table.addRow(
                Register.BX.name(), getRegisterBinary(Register.BX),
                Register.BH.name(), getRegisterBinary(Register.BH),
                Register.BL.name(), getRegisterBinary(Register.BL)
        );
        table.addRow(
                Register.CX.name(), getRegisterBinary(Register.CX),
                Register.CH.name(), getRegisterBinary(Register.CH),
                Register.CL.name(), getRegisterBinary(Register.CL)
        );
        table.addRow(
                Register.DX.name(), getRegisterBinary(Register.DX),
                Register.DH.name(), getRegisterBinary(Register.DH),
                Register.DL.name(), getRegisterBinary(Register.DL)
        );
        return table.toString();
    }

    public String getRegisterStringNotBinary(){
        PrettyTable table = new PrettyTable("Register", "Value", "High", "Value", "Low", "Value");
        table.addRow(
                Register.AX.name(), getRegister(Register.AX).toString(),
                Register.AH.name(), getRegister(Register.AH).toString(),
                Register.AL.name(), getRegister(Register.AL).toString()
        );
        table.addRow(
                Register.BX.name(), getRegister(Register.BX).toString(),
                Register.BH.name(), getRegister(Register.BH).toString(),
                Register.BL.name(), getRegister(Register.BL).toString()
        );
        table.addRow(
                Register.CX.name(), getRegister(Register.CX).toString(),
                Register.CH.name(), getRegister(Register.CH).toString(),
                Register.CL.name(), getRegister(Register.CL).toString()
        );
        table.addRow(
                Register.DX.name(), getRegister(Register.DX).toString(),
                Register.DH.name(), getRegister(Register.DH).toString(),
                Register.DL.name(), getRegister(Register.DL).toString()
        );
        return table.toString();
    }

    public Short getRegister(Register register){
        if (register == Register.AX || register == Register.BX || register == Register.CX || register == Register.DX){
            return registers.get(register);
        }
        else if (register == Register.AL || register == Register.BL || register == Register.CL || register == Register.DL){
            return RegisterUtils.getLowerByte(registers.get(getFullRegister(register)));
        }
        else if (register == Register.AH || register == Register.BH || register == Register.CH || register == Register.DH) {
            return RegisterUtils.getUpperByte(registers.get(getFullRegister(register)));
        }
        return (short)-1;
    }

    public String getRegisterBinary(Register register){
        if (register == Register.AX || register == Register.BX || register == Register.CX || register == Register.DX){
            return RegisterUtils.toBinaryString(registers.get(register));
        }
        else if (register == Register.AL || register == Register.BL || register == Register.CL || register == Register.DL){
            return RegisterUtils.getLowerByteBinary(registers.get(getFullRegister(register)));
        }
        else if (register == Register.AH || register == Register.BH || register == Register.CH || register == Register.DH) {
            return RegisterUtils.getUpperByteBinary(registers.get(getFullRegister(register)));
        }
        return RegisterUtils.toBinaryString((short)0);
    }

    private static Register getFullRegister(Register register){
        return switch (register) {
            case AX, AH, AL -> Register.AX;
            case BX, BH, BL -> Register.BX;
            case CX, CH, CL -> Register.CX;
            case DX, DH, DL -> Register.DX;
        };
    }

    private void setRegisters(Register register, Short value){
        Register fullRegister = getFullRegister(register);
        switch (register){
            case AX, BX, CX, DX:
                registers.put(register, value);
                break;
            case AH, BH, CH, DH:
                registers.put(fullRegister, RegisterUtils.setUpperByte(getRegister(fullRegister), value));
                break;
            case AL, BL, CL, DL:
                registers.put(fullRegister, RegisterUtils.setLowerByte(getRegister(fullRegister), value));
                break;
        }
    }

    private void fillRegisters(){
        registers.put(Register.AX, (short)0);
        registers.put(Register.BX, (short)0);
        registers.put(Register.CX, (short)0);
        registers.put(Register.DX, (short)0);
    }

    private Register convertStringToRegister(String reg){
        try {
            return Register.valueOf(reg.toUpperCase());
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    private void add(String[] operands) throws InvalidOperandsArgument {
        if (operands.length != 2) throw new InvalidOperandsArgument(CommandType.ADD);

        Register o1 = convertStringToRegister(operands[0]);

        if (o1 == null) throw new InvalidOperandsArgument(CommandType.ADD);
        short o1Value = getRegister(o1);

        Register o2 = convertStringToRegister(operands[1]);
        short o2Value;
        if (o2 != null) o2Value = getRegister(o2);
        else o2Value = Short.parseShort(operands[1]);

        setRegisters(o1, (short)(o1Value + o2Value));

    }

    private void mov(String[] operands) throws InvalidOperandsArgument {
        if (operands.length != 2) throw new InvalidOperandsArgument(CommandType.MOV);

        Register o1 = convertStringToRegister(operands[0]);

        if (o1 == null) throw new InvalidOperandsArgument(CommandType.MOV);

        Register o2 = convertStringToRegister(operands[1]);
        short o2Value;
        if (o2 != null) o2Value = getRegister(o2);
        else o2Value = Short.parseShort(operands[1]);

        setRegisters(o1, o2Value);
    }

    private void sub(String[] operands) throws InvalidOperandsArgument {
        if (operands.length != 2) throw new InvalidOperandsArgument(CommandType.SUB);

        Register o1 = convertStringToRegister(operands[0]);

        if (o1 == null) throw new InvalidOperandsArgument(CommandType.SUB);
        short o1Value = getRegister(o1);

        Register o2 = convertStringToRegister(operands[1]);
        short o2Value;
        if (o2 != null) o2Value = getRegister(o2);
        else o2Value = Short.parseShort(operands[1]);

        setRegisters(o1, (short)(o1Value - o2Value));

    }

    private void mul(String[] operands) throws InvalidOperandsArgument {
        if (operands.length != 1) throw new InvalidOperandsArgument(CommandType.MUL);
        Register o = convertStringToRegister(operands[0]);

        short oValue;
        if (o != null) oValue = getRegister(o);
        else oValue = Short.parseShort(operands[0]);

        setRegisters(Register.AX, (short)(getRegister(Register.AX) * oValue));
    }

    private void div(String[] operands) throws InvalidOperandsArgument {
        if (operands.length != 1) throw new InvalidOperandsArgument(CommandType.DIV);
        Register o = convertStringToRegister(operands[0]);

        short oValue;
        if (o != null) oValue = getRegister(o);
        else oValue = Short.parseShort(operands[0]);

        setRegisters(Register.AX, (short)(getRegister(Register.AX) / oValue));
        setRegisters(Register.DX, (short)(getRegister(Register.AX) % oValue));
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
