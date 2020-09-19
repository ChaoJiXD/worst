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
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void func_174935_a() {
		// TODO �Զ����ɵķ������
		
	}
}
