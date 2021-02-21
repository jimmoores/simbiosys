package com.jimmoores.alife;

public class WorldParameters {
  private static final double TRANSFER_RATIO = 1d/3d;
  private static final double TRANSFER_EFFICIENCY = 0.8d;
  private static final double ATTACK_RATIO = 1d/2d;
  private static final double ATTACK_EFFICIENCY = 0.5d;
  private static final double ASEX_INSERT_PROBABILITY = 0.001d;
  private static final double ASEX_MUTATE_PROBABILITY = 0.001d;
  private static final double ASEX_DELETE_PROBABILITY = 0.001d;
  private static final int MAX_CHUNK_SIZE = 8;
  private static final double SEX_DELETE_PROBABILITY = 0.01d;
  private static final double SEX_MUTATE_PROBABILITY = 0.01d;
  private static final double SEX_INSERT_PROBABILITY = 0.01d;
  private static final double ASEX_ENERGY_RATIO = 1d/3d;
  private static final double SEX_ENERGY_RATIO = 1d/3d;
  private static final double INITIAL_ENERGY_MAX = 50d;
  private static final double BASK_DELTA_MID = 1.9d;
  private static final double BASK_DELTA_SCALE = 1d;
  private static final int MAX_AGE = 2000;
  private static final long YEAR_LENGTH = 1000;
  private long _cycleNum = 0L;
  
  public void setCycleNumber(long cycleNum) {
    _cycleNum  = cycleNum;
  }
  
  public double getTransferRatio() {
    return TRANSFER_RATIO;
  }
  public double getTransferEfficiency() {
    return TRANSFER_EFFICIENCY;
  }
  public double getAttackRatio() {
    return ATTACK_RATIO;
  }
  public double getAttackEfficiency() {
    return ATTACK_EFFICIENCY;
  }
  public double getAsexInsertProbability() {
    return ASEX_INSERT_PROBABILITY;
  }
  public double getAsexMutateProbability() {
    return ASEX_MUTATE_PROBABILITY;
  }
  public double getAsexDeleteProbability() {
    return ASEX_DELETE_PROBABILITY;
  }
  public int getMaxChunkSize() {
    return MAX_CHUNK_SIZE;
  }
  public double getSexDeleteProbability() {
    return SEX_DELETE_PROBABILITY;
  }
  public double getSexMutateProbability() {
    return SEX_MUTATE_PROBABILITY;
  }
  public double getSexInsertProbability() {
    return SEX_INSERT_PROBABILITY;
  }
  public double getAsexEnergyRatio() {
    return ASEX_ENERGY_RATIO;
  }
  public double getSexEnergyRatio() {
    return SEX_ENERGY_RATIO;
  }
  public double getInitialEnergyMax() {
    return INITIAL_ENERGY_MAX;
  }
  public double getBaskDelta() {
    long day = _cycleNum % YEAR_LENGTH;
    double yearFraction = (double) day / (double) YEAR_LENGTH;
    double energy = BASK_DELTA_MID + Math.sin(Math.PI * yearFraction) * BASK_DELTA_SCALE;
    return energy;
  }
  public int getMaxAge() {
    return MAX_AGE;
  }
}
