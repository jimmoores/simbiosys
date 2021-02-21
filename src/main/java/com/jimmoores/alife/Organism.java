package com.jimmoores.alife;

import java.util.ArrayList;
import java.util.List;



public class Organism {

  private int _age = 0;
  private boolean _dead = false;
  private double _energy;
  private ExecutionEnvironment _env;
  
  public Organism(World world) {
    this(world, true);
  }
  
  private Organism(World world, boolean randomize) {
    _env = new ExecutionEnvironment(world, this, randomize);
    _energy = Math.random() * getInitialEnergyMax();
  }
  
  public Organism(World world, Organism parent) {
    this(world, false);
    byte[] program = parent.getEnv().getProgram();
    program = mutate(program);
    getEnv().setProgram(program);
    getEnv().setCondition(!parent.getEnv().isCondition());
    getEnv().setPC(0);//parent.getEnv().getPC());
    double energyForChild = parent.getEnergy() * getAsexEnergyRatio();
    setEnergy(energyForChild);
    parent.setEnergy(parent.getEnergy() - energyForChild);
  }

  public Organism(World world, Organism initiator, Organism other) {
    this(world, false);
    byte[] program = mutate(initiator.getEnv().getProgram(), other.getEnv().getProgram());
    getEnv().setProgram(program);
    getEnv().setCondition(!initiator.getEnv().isCondition()); // this is to _theoretically_ allow fork() like behaviour
    getEnv().setPC(0);//initiator.getEnv().getPC());
    double energyForChild = initiator.getEnergy() * getSexEnergyRatio();
    setEnergy(energyForChild);
    initiator.setEnergy(initiator.getEnergy() - energyForChild);
  }  
  
  public byte[] mutate(byte[] program) {
    List<Byte> newProgram = new ArrayList<Byte>();
    for (byte opcode : program) {
      if (Math.random() > getAsexDeleteProbability() || program.length < 2) {
        if (Math.random() > getAsexMutateProbability()) {
          newProgram.add(opcode);
        } else {
          newProgram.add((byte)((Math.random() * 256) + Byte.MIN_VALUE));
        }
      }
      if (Math.random() < getAsexInsertProbability()) {
        newProgram.add((byte)((Math.random() * 256) + Byte.MIN_VALUE));
      }
    }
    return listToArray(newProgram);
  }
  
  private byte[] listToArray(List<Byte> list) {
    byte[] result = new byte[list.size()];
    int i=0;
    for (byte opcode : list) {
      result[i++] = opcode;
    }    
    return result;
  }
  private byte randomOpcode() {
    return (byte)((Math.random() * 256) + Byte.MIN_VALUE);
  }
  
  private byte[] mutate(byte[] initiator, byte[] other) {
    List<Byte> result = new ArrayList<Byte>();
    int i=0;
    int chunk = (int)(Math.random() * getMaxChunkSize());
    boolean first = Math.random() >= 0.5d;
    while (i<initiator.length && i<other.length) {
      if (chunk > 0) {
        if (first) {
          if (Math.random() > getSexDeleteProbability()) {
            if (Math.random() > getSexMutateProbability()) {
              result.add(initiator[i]);
            } else {
              result.add(randomOpcode());
            }
          }
        } else {
          if (Math.random() > getSexDeleteProbability() || initiator.length < 2) {
            if (Math.random() > getSexMutateProbability()) {
              result.add(other[i]);
            } else {
              result.add(randomOpcode());
            }
          }
        }
        if (Math.random() <= getSexInsertProbability()) {
          result.add((byte)((Math.random() * 256) + Byte.MIN_VALUE));
        }
        i++;
        chunk--;
      } else {
        chunk = (int)(Math.random() * getMaxChunkSize());
        first = !first; // flip to other program.
      }
    }
    if (initiator.length > other.length){
      for (;i<initiator.length; i++) { // copy rest over.
        result.add(initiator[i]);
      }
    }
    return listToArray(result);
  }
  
  public ExecutionEnvironment getEnv() {
    return _env;
  }
  
  public double getEnergy() {
    return _energy;
  }
  
  public void setEnergy(double energy) {
    _energy = energy;
  }
  
  public boolean eat(Organism other) {
    return true;
  }
  
  public int getAge() {
    return _age;
  }
  
  @SuppressWarnings("static-access")
  public void endCycle() {
    _age++;
    _energy--;
    if (_energy <= 0.0d || (_age * Math.random()) > getEnv().getWorld().getWorldParameters().getMaxAge()) {
      kill();
    }
  }
  
  public void kill() {
    _dead = true;
  }
  
  public boolean isDead() {
    return _dead;
  }

  public Organism asexualReproduce() {
    return new Organism(getEnv().getWorld(), this);
  }

  public Organism sexualReproduce(Organism parent) {
    return new Organism(getEnv().getWorld(), this, parent);
  }

  public boolean attack(Organism target) {
    if (target.getEnergy() < getEnergy()) {
      double energy = (target.getEnergy() * getAttackRatio());
      setEnergy(getEnergy() + (energy * getAttackEfficiency()));
      target.setEnergy(target.getEnergy() - energy);
      return true;
    }
    return false;
  }

  public boolean transfer(Organism target) {
    double energy = getEnergy();
    energy = energy * getTransferRatio();
    setEnergy(getEnergy() - energy);
    target.setEnergy(target.getEnergy() + (energy * getTransferEfficiency()));
    return true;
  }

  public void execute(int x, int y) {
    if (getEnergy() > 0.0) {
      getEnv().execute(x, y);
      setEnergy(getEnergy() - 1.0d);
    } else {
      kill();
    }
  }

  public double getTransferRatio() {
    return _env.getWorld().getWorldParameters().getTransferRatio();
  }

  public double getTransferEfficiency() {
    return _env.getWorld().getWorldParameters().getTransferEfficiency();
  }

  public double getAttackRatio() {
    return _env.getWorld().getWorldParameters().getAttackRatio();
  }

  public double getAttackEfficiency() {
    return _env.getWorld().getWorldParameters().getAttackEfficiency();
  }

  public double getAsexInsertProbability() {
    return _env.getWorld().getWorldParameters().getAsexInsertProbability();
  }

  public double getAsexMutateProbability() {
    return _env.getWorld().getWorldParameters().getAsexMutateProbability();
  }

  public double getAsexDeleteProbability() {
    return _env.getWorld().getWorldParameters().getAsexDeleteProbability();
  }

  public int getMaxChunkSize() {
    return _env.getWorld().getWorldParameters().getMaxChunkSize();
  }

  public double getSexDeleteProbability() {
    return _env.getWorld().getWorldParameters().getSexDeleteProbability();
  }

  public double getSexMutateProbability() {
    return _env.getWorld().getWorldParameters().getSexMutateProbability();
  }

  public double getSexInsertProbability() {
    return _env.getWorld().getWorldParameters().getSexInsertProbability();
  }

  public double getAsexEnergyRatio() {
    return _env.getWorld().getWorldParameters().getAsexEnergyRatio();
  }

  public double getSexEnergyRatio() {
    return _env.getWorld().getWorldParameters().getSexEnergyRatio();
  }

  public double getInitialEnergyMax() {
    return _env.getWorld().getWorldParameters().getInitialEnergyMax();
  }
}
