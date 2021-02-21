package com.jimmoores.alife;

public abstract class Instruction {
  private int _index;
  public abstract void execute(Organism current, World world, ExecutionEnvironment env, int x, int y);
  public abstract boolean isBlocking();
  public void setInstructionIndex(int index) {
    _index = index;
  }
  
  public int getInstructionIndex() {
    return _index;
  }
  
}
