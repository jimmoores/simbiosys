package com.jimmoores.alife;

public class AttackInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    Direction d = env.nextDirection();
    boolean successful = world.attack(x, y, d);
    env.setCondition(successful);
    world.noMove(x, y);
    world.getStatistics().countDirection(d);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return true;
  }

}
