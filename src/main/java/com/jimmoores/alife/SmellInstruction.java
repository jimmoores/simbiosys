package com.jimmoores.alife;

public class SmellInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    Direction d = env.nextDirection();
    int regIndex = env.nextRegister();
    double smell = world.smell(x, y, d);
    env.getRegs()[regIndex] = smell;
    env.setCondition(smell > 0.0d);
    world.getStatistics().countDirection(d);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return false;
  }

}
