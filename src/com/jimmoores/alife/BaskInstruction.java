package com.jimmoores.alife;

public class BaskInstruction extends Instruction {
  public static final double BASK_DELTA = 6.0d;
  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    current.setEnergy(current.getEnergy() + world.getWorldParameters().getBaskDelta());
    world.noMove(x, y);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return true;
  }

}
