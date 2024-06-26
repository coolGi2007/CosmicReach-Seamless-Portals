package com.nikrasoff.seamlessportals.portals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class PortalManager {
    public String prevPortalGenZone;
    public Vector3 prevPortalGenPos;
    public Array<Portal> createdPortals = new Array<>(Portal.class);

    public boolean shouldUpdatePortalArray = false;


    public PortalManager(){}
    
    public BlockPosition getPrevGenBlockPos(){
        if (this.prevPortalGenPos == null) return null;
        Chunk c = InGame.world.getZone(this.prevPortalGenZone).getChunkAtBlock((int) this.prevPortalGenPos.x, (int) this.prevPortalGenPos.y, (int) this.prevPortalGenPos.z);
        return new BlockPosition(c, (int) (this.prevPortalGenPos.x - c.blockX), (int) (this.prevPortalGenPos.y - c.blockY), (int) (this.prevPortalGenPos.z - c.blockZ));
    }

    public void createPortalPair(BlockPosition portalPos1, BlockPosition portalPos2, Zone zone1, Zone zone2){
        Portal portal1 = Portal.fromBlockPos(new Vector2(3, 3), portalPos1, zone1);
        Portal portal2 = Portal.fromBlockPos(new Vector2(3, 3), portalPos2, zone2);
        portal1.linkPortal(portal2);
        portal2.linkPortal(portal1);
        this.createdPortals.add(portal1);
        this.createdPortals.add(portal2);
    }

    public void linkPortalsInArray(){
        for (int i = 0; i < this.createdPortals.size; i += 2){
            this.createdPortals.get(i).linkPortal(this.createdPortals.get(i + 1));
            this.createdPortals.get(i + 1).linkPortal(this.createdPortals.get(i));
        }
    }

    public void updatePortalArray(){
        this.shouldUpdatePortalArray = false;
        Array<Portal> newPortalArray = new Array<>(Portal.class);
        for (Portal portal : this.createdPortals){
            if (!portal.isPortalDestroyed){
                newPortalArray.add(portal);
            }
        }
        this.createdPortals = newPortalArray;
    }

    public void renderPortals(Camera playerCamera){
        if (this.shouldUpdatePortalArray) this.updatePortalArray();
        Player player = InGame.getLocalPlayer();
        for (Portal portal : this.createdPortals){
            portal.updateAnimations(Gdx.graphics.getDeltaTime());
            BoundingBox portalBB = portal.getMeshBoundingBox();
            if (portal.zoneID.equals(player.zoneId) && !portal.isPortalDestroyed && playerCamera.frustum.boundsInFrustum(portalBB) && portal.position.dst(playerCamera.position) < 50){
                portal.render(playerCamera);
            }
        }
    }
}
