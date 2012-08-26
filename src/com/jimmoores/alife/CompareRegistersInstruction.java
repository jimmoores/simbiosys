package com.jimmoores.alife;

public class CompareRegistersInstruction extends Instruction {

  @Override
  public void execute(Organism current, World world, ExecutionEnvironment env, int x, int y) {
    int reg1 = env.nextRegister();
    int reg2 = env.nextRegister();
    env.setCondition(env.getRegs()[reg1] > env.getRegs()[reg2]);
    world.getStatistics().countInstruction(this);
  }

  @Override
  public boolean isBlocking() {
    return false;
  }

}
