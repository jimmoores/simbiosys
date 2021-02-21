package com.jimmoores.alife;

public class DecrementIndexInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    env.setIndex(env.getIndex() == 0 ? 0 : env.getIndex() - 1);
    env.setCondition(env.getIndex() > 0);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return false;
  }

}
