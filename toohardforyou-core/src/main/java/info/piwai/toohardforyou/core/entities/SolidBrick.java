/**
 * Copyright (C) 2011 Pierre-Yves Ricau (py.ricau at gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package info.piwai.toohardforyou.core.entities;

import info.piwai.toohardforyou.core.BrickType;
import info.piwai.toohardforyou.core.Constants;
import info.piwai.toohardforyou.core.EntityEngine;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class SolidBrick extends DynamicPhysicsEntity implements PhysicsEntity.HasContactListener {

    private final BrokenListener listener;

    public interface BrokenListener {
        void broken();
    }
    
    public SolidBrick(EntityEngine entityEngine, BrickType brickType, BrokenListener listener, float x, float y) {
        super(entityEngine, brickType.getImagePath(), x, y, 0);
        this.listener = listener;
    }
    
    @Override
    Body initPhysicsBody(World world, float x, float y, float angle) {
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        Vec2[] polygon = new Vec2[4];
        polygon[0] = new Vec2(-getWidth() / 2f, -getHeight() / 2f);
        polygon[1] = new Vec2(getWidth() / 2f, -getHeight() / 2f);
        polygon[2] = new Vec2(getWidth() / 2f, getHeight() / 2f);
        polygon[3] = new Vec2(-getWidth() / 2f, getHeight() / 2f);
        polygonShape.set(polygon, polygon.length);
        fixtureDef.shape = polygonShape;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 1.2f;
        body.createFixture(fixtureDef);
        body.setTransform(new Vec2(x, y), angle);
        return body;
    }

    @Override
    public float getWidth() {
        return Constants.BRICK_WIDTH;
    }

    @Override
    public float getHeight() {
        return Constants.BRICK_HEIGHT;
    }

    @Override
    public void contact(PhysicsEntity other) {
        listener.broken();
    }

}