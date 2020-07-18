package paulevs.betterleaves;

import java.util.Random;

import net.minecraft.level.TileView;
import net.minecraft.tile.Leaves;
import net.minecraft.tile.Tile;
import paulevs.corelib.CoreLib;
import paulevs.corelib.model.Model;
import paulevs.corelib.model.shape.Shape;
import paulevs.corelib.model.shape.ShapeBoxCustom;
import paulevs.corelib.model.shape.ShapeCross;
import paulevs.corelib.texture.UVPair;

public class ModelLeaves extends Model {
	private static UVPair uvNormal;
	private static UVPair uvFir;
	private Shape cross;
	private Shape box;

	public ModelLeaves(String leafTex, float scale) {
		this.addTexture("leaf", "/assets/betterleaves/textures/" + leafTex + ".png");
		this.addTexture("snow_side", "/assets/betterleaves/textures/snow_side.png");
		cross = new ShapeCross(scale, 1.414F * scale);
		box = new ShapeBoxCustom();
	}

	@Override
	public void renderBlock() {
		if (shouldRender(Shape.getX(), Shape.getY(), Shape.getZ(), Shape.getTileView())) {
			if (uvNormal == null)
			{
				uvNormal = UVPair.getVanillaUV(Tile.LEAVES, 0, 0);
				uvFir = UVPair.getVanillaUV(Tile.LEAVES, 0, 1);
			}
			
			Shape.setColorFromWorld();
			Shape.setLightFromWorld();
			Shape.setUV((Shape.getMeta() & 1) == 0 ? uvNormal : uvFir);
			box.render();
			
			boolean snow = hasSnow(Shape.getX(), Shape.getY() + 1, Shape.getZ(), Shape.getTileView());
			
			if (snow)
			{
				Shape.setColorWhite();
				this.setTexture("snow_side");
				Shape.drawAll();
				Shape.setFaceRendering(CoreLib.FACE_NEG_Y, false);
				Shape.setFaceRendering(CoreLib.FACE_POS_Y, false);
				box.render();
			}
			else
			{
				Random random = Shape.getRandomForLocation();
				float ox = random.nextFloat() * 0.25F - 0.125F;
				float oz = random.nextFloat() * 0.25F - 0.125F;
				
				this.setTexture("leaf");
				Shape.setOffset(ox, 0, oz);
				cross.render();
				Shape.resetOffset();
			}
		}
	}

	@Override
	public void renderItem() {}
	
	@Override
	public boolean hasItem() {
		return false;
	}

	private boolean isLeavesOrSolid(int x, int y, int z, TileView world) {
		Tile tile = Tile.BY_ID[world.getTileId(x, y, z)];
		return tile != null && (tile instanceof Leaves || (tile.isFullCube() && tile.isFullOpaque()));
	}

	private boolean shouldRender(int x, int y, int z, TileView world) {
		return  !isLeavesOrSolid(x - 1, y, z, world) ||
				!isLeavesOrSolid(x + 1, y, z, world) ||
				!isLeavesOrSolid(x, y - 1, z, world) ||
				!isLeavesOrSolid(x, y + 1, z, world) ||
				!isLeavesOrSolid(x, y, z - 1, world) ||
				!isLeavesOrSolid(x, y, z + 1, world);
	}
	
	private boolean hasSnow(int x, int y, int z, TileView world)
	{
		return world.getTileId(x, y, z) == Tile.SNOW.id;
	}
}
