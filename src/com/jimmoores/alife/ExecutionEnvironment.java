package com.jimmoores.alife;

import java.util.ArrayList;
import java.util.List;

public class ExecutionEnvironment {
  private static final int INITIAL_SIZE = 32;
  private static final int NUM_REGS = 4;
  private static final int MAX_INSTRUCTIONS_PER_CYCLE = 8;

  private byte[] _program;
  private int _pc = 0;
  private int _index = 0;
  private double[] _regs;
  private boolean _condition;
  private World _world;
  private Organism _organism;
  
  private static final List<Instruction> s_dispatchTableList = new ArrayList<Instruction>();
  private static Instruction[] s_dispatchTable = new Instruction[0];
  
  static void registerInstruction(Instruction instruction) {
    s_dispatchTableList.add(instruction);
    s_dispatchTable = s_dispatchTableList.toArray(s_dispatchTable);
  }
  
  public static int getNumRegisteredInstructions() {
    return s_dispatchTable.length;
  }
  
  public ExecutionEnvironment(World world, Organism organism, boolean randomize) {
    _world = world;
    _organism = organism;
    _program = new byte[INITIAL_SIZE];
    if (randomize) {
      for (int i=0; i<INITIAL_SIZE; i++) {
        _program[i] = (byte) (((int)(Math.random() * 256))-128);
      }
    }
    _regs = new double[NUM_REGS];
  }
  
  public byte[] getProgram() {
    return _program;
  }
  
  public void setProgram(byte[] program) {
    _program = program;
  }
  
  public World getWorld() {
    return _world;
  }
  
  public Organism getOrganism() {
    return _organism;
  }
  
  public void setIndex(int index) { 
    _index = index;
  }
  
  public int getIndex() {
    return _index;
  }
  
  public void setPC(int pc) {
    _pc = pc;
  }
  
  public int getPC() {
    return _pc;
  }
  
  public double[] getRegs() {
    return _regs;
  }
  
  public void setCondition(boolean condition) {
    _condition = condition;
  }
  
  public boolean isCondition() {
    return _condition;
  }
  
  public byte nextByte() {
    byte next = _program[_pc % _program.length ]; // must make sure pc never -ve.
    _pc = Math.abs((_pc + 1) % _program.length);
    return next;
  }
  
  public int nextAddress() {
    byte next = nextByte();
    return Math.abs(_pc + next) % _program.length;
  }
  
  public Instruction nextInstruction() {
    int opcode = Math.abs(nextByte() % s_dispatchTable.length);
    if (opcode < s_dispatchTable.length && opcode >= 0) {
      return s_dispatchTable[opcode];
    } else {
      return new NullInstruction();
    }
  }
  
  public Direction nextDirection() {
    return Direction.values()[nextByte() & 0x7]; // should mask off bottom 3 bits, direction.
  }
  
  public int nextRegister() {
    return Math.abs(nextByte() % NUM_REGS);
  }
  
  public void execute(int x, int y) {
    Instruction instruction;
    int count = 0;
    do {
      instruction = nextInstruction();
      instruction.execute(getOrganism(), getWorld(), this, x, y);
      count++;
    } while ((!instruction.isBlocking()) && count <= MAX_INSTRUCTIONS_PER_CYCLE);
    if (!instruction.isBlocking()) {
      getWorld().noMove(x, y); // all blocking instructions do it already.
    }
  }
  
  public double signature() {
    int total = 0;
    for (byte b : _program) {
      total += Math.abs(b);
    }
    return (double)(total % 256) / 256d;
  }
  
  public int signatureInt() {
    int total = 0;
    for (byte b : _program) {
      total += (int)Byte.MIN_VALUE + (int)b;
    }
    return total;
  }
}
