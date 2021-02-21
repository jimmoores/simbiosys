package com.jimmoores.alife;

public class ConditionalBranchInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    if (env.isCondition()) { 
      env.setPC(env.nextAddress());
    }
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return false;
  }

}
