package com.jimmoores.alife;

public class SexualReproduceInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    Direction parentDir = env.nextDirection();
    Direction childDir = env.nextDirection();
    world.sexualReproduce(x, y, parentDir, childDir);
    world.getStatistics().countDirection(parentDir);
    world.getStatistics().countDirection(childDir);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return true;
  }

}
