package shadersmod.client;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;

public class DefaultTexture extends AbstractTexture
{
    public DefaultTexture()
    {
        this.loadTexture((IResourceManager)null);
    }

    public void loadTexture(IResourceManager resourcemanager)
    {
        int[] aint = ShadersTex.createAIntImage(1, -1);
        ShadersTex.setupTexture(this.getMultiTexID(), aint, 1, 1, false, false);
    }

	@Override
	public void func_174936_b(boolean var1, boolean var2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void func_174935_a() {
		// TODO 自动生成的方法存根
		
	}
}
