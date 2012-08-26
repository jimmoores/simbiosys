package com.jimmoores.alife;

public enum Direction {
  NORTH_WEST {
	  public int applyX(int x, int max) {
		  if (x == 0) {
			  return max-1;
		  } else {
			  return x-1;
		  }
	  }
	  
	  public int applyY(int y, int max) {
		  if (y == 0) {
			  return max-1;
		  } else {
			  return y-1;
		  }
	  }
  }, 
  NORTH {
	  public int applyX(int x, int max) {
		  return x;
	  }
	  public int applyY(int y, int max) {
		  if (y == 0) {
			  return max-1;
		  } else {
			  return y-1;
		  }
	  }
  }, 
  NORTH_EAST {
	  public int applyX(int x, int max) {
		  if (x == max-1) {
			  return 0;
		  } else {
			  return x+1;
		  }
	  }
	  public int applyY(int y, int max) {
		  if (y == 0) {
			  return max-1;
		  } else {
			  return y-1;
		  }
	  }
  },
  WEST {
	  public int applyX(int x, int max) {
		  if (x == 0) {
			  return max-1;
		  } else {
			  return x-1;
		  }
	  }
	  
	  public int applyY(int y, int max) {
		  return y;
	  }
	  
  },
  EAST {
	  public int applyX(int x, int max) {
		  if (x == max-1) {
			  return 0;
		  } else {
			  return x+1;
		  }
	  }
	  public int applyY(int y, int max) {
		  return y;
	  }
  },
  SOUTH_WEST {
	  public int applyX(int x, int max) {
		  if (x == 0) {
			  return max-1;
		  } else {
			  return x-1;
		  }
	  }
	  public int applyY(int y, int max) {
		  if (y == max-1) {
			  return 0;
		  } else {
			  return y+1;
		  }
	  }
  }, 
  SOUTH {
	  public int applyX(int x, int max) {
		  return x;
	  }
	  public int applyY(int y, int max) {
		  if (y == max-1) {
			  return 0;
		  } else {
			  return y+1;
		  }
	  }
  }, 
  SOUTH_EAST {
	  public int applyX(int x, int max) {
		  if (x == max-1) {
			  return 0;
		  } else {
			  return x+1;
		  }
	  }
	  public int applyY(int y, int max) {
		  if (y == max-1) {
			  return 0;
		  } else {
			  return y+1;
		  }
	  }
  };
  public abstract int applyX(int x, int max);
  public abstract int applyY(int y, int max);
}
