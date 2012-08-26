package com.jimmoores.alife;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class WorldPanel extends JPanel {
  BufferedImage _back = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_ARGB);
  BufferedImage _front = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_ARGB);
  int[] _imageBuffer = new int[World.WIDTH * World.HEIGHT];
  private static final int[][][] _colorMap = new int[256][256][256]; 
  static {
    for (int h=0; h<256; h++) {
      for (int s=0; s<256; s++) {
        for (int v=0; v<256; v+=(0.8f * 256)) {
          _colorMap[h][s][v] = colorToInt(Color.getHSBColor(h / 256f, s / 256f, v / 256f));
        }
      }
    }
  }
  public void paintWorld(World world) {
    for (int y=0; y<World.HEIGHT; y++) {
      for (int x=0; x<World.WIDTH; x++) {
        Organism org = world.getCurrent(x, y);
        int c;
        if (org != null) {
          double energy = org.getAge(); //org.getEnergy() / world.getMaxEnergy();
          double dna = (org.getEnv().signature());
          c = hsv((float)dna, 0.8f, (float)0.8f);//energy);
        } else {
          c = hsv(0f, 0f, 0f);
        }
        _imageBuffer[(y * World.WIDTH) + x] = c;
      }
    }
    _back.setRGB(0, 0, World.WIDTH, World.HEIGHT, _imageBuffer, 0, World.WIDTH);
    BufferedImage temp = _front;
    _front = _back;
    _back = temp;
    repaint();
  }
  
  private int hsv(float h, float s, float v) {
    return _colorMap[(int) (h * 255f)][(int) (s * 255f)][(int) (v * 255f)];
  }
  
  private static int colorToInt(Color color) {
      return color.getAlpha() << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
  }
  
  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    graphics.drawImage(_front, 0, 0, null);
  }
}
