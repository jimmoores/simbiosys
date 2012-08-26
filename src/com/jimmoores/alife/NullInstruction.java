package com.jimmoores.alife;

public class NullInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    world.getStatistics().countInstruction(this);
  }
  
  @Override
  public boolean isBlocking() {
    return false;
  }

}
