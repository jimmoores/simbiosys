package com.jimmoores.alife;

public class MoveInstruction extends Instruction {
  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    Direction d = env.nextDirection();
    boolean successful = world.move(x, y, d);
    env.setCondition(successful);
    world.getStatistics().countDirection(d);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return true;
  }
}
