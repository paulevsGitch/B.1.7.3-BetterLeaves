package paulevs.betterleaves;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.tile.Tile;
import paulevs.corelib.api.ModelRegistry;

public class BetterLeaves implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLeaves normal = new ModelLeaves("leaves", 1.5F);
		ModelLeaves fir = new ModelLeaves("fir", 1.5F);
		
		for (int i = 0; i < 16; i++)
			ModelRegistry.addBlockModel(Tile.LEAVES, i, (i & 1) == 0 ? normal : fir);
		
		System.out.println("BetterLeaves Initialized");
	}
}
