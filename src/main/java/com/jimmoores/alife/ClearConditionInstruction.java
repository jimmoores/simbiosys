package com.jimmoores.alife;

public class ClearConditionInstruction extends Instruction {
  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    env.setCondition(false);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return false;
  }
}
