package com.example.assignment2;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class Ball {
    float x, y;
    float radius = 8;

    float velocytY = 0;
    // float velocityX = (float)(Math.random() * 4 - 2);
    float gravity = 0.8f + (float)Math.random() * 0.05f;
    float bounceFactor = 0.84f + (float)Math.random() * 0.1f;
    float stopThreshold = 2f;
    boolean stopped = false;

    Ball(float x, float y) {
        this.x = x;
        this.y = y;
    }


    void update(int heightView, int widthView) {
        if (stopped) {
            return;
        }
       velocytY += gravity;
       y += velocytY;
       // x += velocityX;

       if (y + radius > heightView) {
           y = heightView - radius - (float)Math.random() * 2;
           velocytY *= -bounceFactor;

           if (Math.abs(velocytY) < stopThreshold) {
               velocytY = 0;
               // velocityX = 0;
               stopped = true;
           }
       }

       // if (x - radius < 0 || x + radius > widthView) {
           // velocityX *= -bounceFactor;
       // }
    }

    boolean isStopped() {
        return stopped;
    }
}
