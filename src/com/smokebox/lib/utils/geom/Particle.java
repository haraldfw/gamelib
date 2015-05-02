/**
 *
 */
package com.smokebox.lib.utils.geom;

import com.smokebox.lib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Particle {

  public Vector2 position;
  public Vector2 velocity;
  public Vector2 acceleration;

  public float damping = 0;

  public float inverseMass;

  public Particle(float mass) {
    position = new Vector2();
    velocity = new Vector2();
    acceleration = new Vector2();
    inverseMass = 1 / mass;
  }

  public void integrate(float delta) {

    if (inverseMass <= 0) {
      return;
    }

    velocity.addScaledVector(acceleration, delta);
    position.addScaledVector(velocity, delta);
  }

  public void setPosition(float x, float y) {
    position.set(x, y);
  }

  public void setVelocity(float x, float y) {
    velocity.set(x, y);
  }

  public void setAcceleration(float x, float y) {
    acceleration.set(x, y);
  }
}