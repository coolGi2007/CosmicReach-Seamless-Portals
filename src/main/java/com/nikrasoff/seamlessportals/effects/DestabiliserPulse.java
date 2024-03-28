package com.nikrasoff.seamlessportals.effects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.nikrasoff.seamlessportals.portals.Portal;
import com.nikrasoff.seamlessportals.SeamlessPortals;

public class DestabiliserPulse extends PulseEffect{
    public float destroyRadius;

    public DestabiliserPulse(Vector3 position, float size){
        super(position, new Vector3(0, 0, 0), new Vector3(size, size, size).scl(2), new Color(1, 0, 0, 0),
                new Color(1, 0, 0, 0.5f), 1, new Vector3(size, size, size).scl(2), new Color(1, 0, 0, 0), 0.5f);
        this.destroyRadius = size;
    }

    @Override
    public void render(Camera playerCamera) {
        super.render(playerCamera);
        if (this.animationSequence.getCurrentAnimationID() > 0){
            for (Portal portal : SeamlessPortals.portalManager.createdPortals){
                Vector3 portalPos = portal.position.cpy();

                Vector3 destroyDiff = new Vector3(this.destroyRadius, this.destroyRadius, this.destroyRadius);
                Vector3 destroyMin = this.position.cpy().sub(destroyDiff);
                Vector3 destroyMax = this.position.cpy().add(destroyDiff);
                BoundingBox destroyBounds = new BoundingBox(destroyMin, destroyMax);
                if (destroyBounds.contains(portalPos)){
                    portal.startDestruction();
                }
            }
        }
    }
}
