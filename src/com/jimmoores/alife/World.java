package com.jimmoores.alife;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class World {
  public static final int WIDTH = 128*4;
  public static final int HEIGHT = 128*4;
  public Organism[][] _current = new Organism[HEIGHT][WIDTH];
  public Organism[][] _next = new Organism[HEIGHT][WIDTH];
  private double _maxEnergy;
  private static Statistics _statistics;
  private WorldParameters _parameters;
  
  public World() {
    _parameters = new WorldParameters();
    for (int y=0; y<HEIGHT; y++) {
      for (int x=0; x<WIDTH; x++) {
        _current[y][x] = new Organism(this);
      }
    }
    _statistics = ChartStatistics.getInstance();
  }
  
  private void executeCycle() {
    List<Point> allOrganisms = new ArrayList<Point>();
    for (int y=0; y<HEIGHT; y++) {
      for (int x=0; x<WIDTH; x++) {
        if (_current[y][x] != null) {
          allOrganisms.add(new Point(x, y));
        }
      }
    }
    Collections.shuffle(allOrganisms);
    for (Point point : allOrganisms) {
      Organism o = _current[point.y][point.x];
      if (!o.isDead()) {
        executeCycle(o, point.x, point.y);
      }
    }
  }
  
  private void analyseCycle() {
    for (int y=0; y<HEIGHT; y++) {
      for (int x=0; x<WIDTH; x++) {
        if (_current[y][x] != null) {
          Organism o = _current[y][x];
          //_statistics.analyseDNA(o.getEnv().getProgram());
          _statistics.countDNA(o.getEnv().signatureInt());
          _statistics.countEnergy(o.getEnergy());
          _statistics.countAge(o.getAge());
        }
      }
    }
    _statistics.processResults();
  }
  
  public Statistics getStatistics() {
    return _statistics;
  }
  
  private void executeCycle(Organism o, int x, int y) {
    o.execute(x, y);
    o.endCycle();
  }
  
  public double getMaxEnergy() {
    return _maxEnergy;
  }
  
  public void executeWorldCycle() {
    executeCycle();
    analyseCycle();
    // current = next, next = null
    Organism[][] temp = _current;
    _current = _next;
    for (int y=0; y<temp.length; y++) {
      for (int x=0; x<temp[y].length; x++) {
        temp[y][x] = null;
      }
    }
    _next = temp;
    for (int y=0; y<HEIGHT; y++) {
      for (int x=0; x<WIDTH; x++) {
        if (_current[y][x] != null) {
          _maxEnergy = Math.max(_maxEnergy, _current[y][x].getEnergy());
        }
      }
    }
  }
  
  private boolean spaceAvailable(int x, int y) {
    if (_current[y][x] == null) {
      return _next[y][x] == null; // check another organism hasn't moved there already.
    } else {
      return false;
    }
  }
  
  private boolean targetAvailable(int x, int y) {
    return _current[y][x] != null;
  }
  
  public Organism getCurrent(int x, int y) {
    return _current[y][x];
  }
  
  public Organism getNext(int x, int y) {
    return _next[y][x];
  }
  
  public void setNext(int x, int y, Organism organism) {
    _next[y][x] = organism;
  }
  
  private void clearCurrent(int x, int y) {
    _current[y][x] = null;
  }
  
  public void noMove(int x, int y) {
    synchronized (this) {
      _next[y][x] = _current[y][x];
    }
  }
  
  public double smell(int x, int y, Direction d) {
    int targetX = d.applyX(x, WIDTH);
    int targetY = d.applyY(y, HEIGHT);
    synchronized (this) {
      Organism current = getCurrent(x, y);
      setNext(x, y, current);
      if (spaceAvailable(targetX, targetY)) {
        return 0.0d;
      } else {
        Organism target = getCurrent(targetX, targetY);
        if (target == null) {
          return 0.0d;
        } else {
          return target.getEnergy();
        }
      }
    }
  }

  public boolean move(int x, int y, Direction d) {
    int targetX = d.applyX(x, WIDTH);
    int targetY = d.applyY(y, HEIGHT);
	  synchronized (this) {
      if (spaceAvailable(x, y)) {
        Organism current = getCurrent(x, y); 
        setNext(targetX, targetY, current);
        clearCurrent(x, y);
        return true;
      } else {
        return false;
      }
    }
  }
  
  public boolean eat(int x, int y, Direction d) {
    int targetX = d.applyX(x, WIDTH);
    int targetY = d.applyY(y, HEIGHT);
    synchronized (this) {
      Organism current = getCurrent(x, y);
      if (targetAvailable(targetX, targetY)) {
        Organism target = getCurrent(targetX, targetY);
        if (current.eat(target)) {
          setNext(targetX, targetY, current);
          clearCurrent(x, y);
          target.kill();
          return true;
        }
      }
      setNext(x, y, current);
      return false;
    }
  }
  
  public boolean attack(int x, int y, Direction d) {
    int targetX = d.applyX(x, WIDTH);
    int targetY = d.applyY(y, HEIGHT);
    synchronized (this) {
      Organism current = getCurrent(x, y);
      if (targetAvailable(targetX, targetY)) {
        Organism target = getCurrent(targetX, targetY);
        if (current.attack(target)) {
          setNext(x, y, current);
          return true;
        }
      }
      setNext(x, y, current);
      return false;
    }
  }

  public boolean transfer(int x, int y, Direction d) {
    int targetX = d.applyX(x, WIDTH);
    int targetY = d.applyY(y, HEIGHT);
    synchronized (this) {
      Organism current = getCurrent(x, y);
      if (targetAvailable(targetX, targetY)) {
        Organism target = getCurrent(targetX, targetY);
        if (current.transfer(target)) {
          setNext(x, y, current);
          return true;
        }
      }
      setNext(x, y, current);
      return false;
    }
  }
  
  public boolean asexualReproduce(int x, int y, Direction d) {
    int targetX = d.applyX(x, WIDTH);
    int targetY = d.applyY(y, HEIGHT);
    synchronized (this) {
      if (spaceAvailable(targetX, targetY)) {
        Organism current = getCurrent(x, y);
        Organism offspring = current.asexualReproduce();
        setNext(targetX, targetY, offspring);
        setNext(x, y, current);
        return true;
      }
      return false;
    }
  }
  
  public boolean sexualReproduce(int x, int y, Direction parentDir, Direction childDir) {
    if (parentDir == childDir) return false; // can't give birth in same direction as parent.
    int parentX = parentDir.applyX(x, WIDTH);
    int parentY = parentDir.applyY(y, HEIGHT);
    int childX = parentDir.applyX(x, WIDTH);
    int childY = parentDir.applyY(y, HEIGHT);
    synchronized (this) {
      if (spaceAvailable(childX, childY) && targetAvailable(parentX, parentY)) {
        Organism current = getCurrent(x, y);
        Organism parent = getCurrent(parentX, parentY);
        Organism offspring = current.sexualReproduce(parent);
        setNext(childX, childY, offspring);
        setNext(x, y, current);
        return true;
      }
      return false;
    }
  }
  
  public WorldParameters getWorldParameters() {
    return _parameters;
  }
}
