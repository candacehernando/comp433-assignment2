package com.example.assignment2;

public class Ball {
    float x, y;
    float radius = 15;
    float velocityY = 5;

    Ball(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void update() {
        y += velocityY;
    }
}
