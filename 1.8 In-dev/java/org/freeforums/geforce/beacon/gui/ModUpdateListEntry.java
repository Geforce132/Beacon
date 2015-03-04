package org.freeforums.geforce.beacon.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModUpdateListEntry implements IGuiListEntry{

    private static final ResourceLocation field_148316_c = new ResourceLocation("textures/gui/resource_packs.png");
    private static final ResourceLocation releasePng = new ResourceLocation("beacon:textures/gui/downloadRelease.png");
    private static final ResourceLocation betaPng = new ResourceLocation("beacon:textures/gui/downloadBeta.png");
    private static final ResourceLocation alphaPng = new ResourceLocation("beacon:textures/gui/downloadAlpha.png");

    private final Minecraft mc;
    private GuiCheckForMods updateScreen;
    private ModTypes releaseType;
    
    private String modName;
    private String modVersion;
	
	public ModUpdateListEntry(String modName, String modVersion, ModTypes releaseType, GuiCheckForMods updateScreen, Minecraft mc){
		this.modName = modName;
		this.modVersion = modVersion;
		this.updateScreen = updateScreen;
		this.releaseType = releaseType;
		this.mc = mc;
	}

	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        if(this.releaseType == ModTypes.RELEASE){
            this.mc.getTextureManager().bindTexture(this.releasePng);
        }else if(this.releaseType == ModTypes.BETA){
            this.mc.getTextureManager().bindTexture(this.betaPng);
        }if(this.releaseType == ModTypes.ALPHA){
            this.mc.getTextureManager().bindTexture(this.alphaPng);
        }
        
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        int i2;

        if (this.mc.gameSettings.touchscreen || isSelected)
        {
            this.mc.getTextureManager().bindTexture(field_148316_c);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int l1 = mouseX - x;
            i2 = mouseY - y;

            if (this.func_148309_e())
            {
                if (l1 < 32)
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            else
            {
                if (this.func_148308_f())
                {
                    if (l1 < 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.func_148314_g())
                {
                    if (l1 < 32 && l1 > 16 && i2 < 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.func_148307_h())
                {
                    if (l1 < 32 && l1 > 16 && i2 > 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }
            }
        }
   
        String s = this.getEntryName();
        i2 = this.mc.fontRendererObj.getStringWidth(s);

        if (i2 > 157)
        {
            s = this.mc.fontRendererObj.trimStringToWidth(s, 157 - this.mc.fontRendererObj.getStringWidth("...")) + "...";
        }

        this.mc.fontRendererObj.drawStringWithShadow(s, (float)(x + 32 + 2), (float)(y + 1), 16777215);
        List list = this.mc.fontRendererObj.listFormattedStringToWidth(this.getEntryMetadata(), 157);

        for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2)
        {
            this.mc.fontRendererObj.drawStringWithShadow((String)list.get(j2), (float)(x + 32 + 2), (float)(y + 12 + 10 * j2), 8421504);
        }
	}

	protected String getEntryMetadata() {
		return modVersion;
	}

	protected String getEntryName() {
		return modName;
	}
	
	protected boolean func_148309_e()
    {
        return !this.updateScreen.hasResourcePackEntry(this);
    }
	
	protected boolean func_148308_f()
    {
        return this.updateScreen.hasResourcePackEntry(this);
    }

	protected boolean func_148314_g()
    {
        List list = this.updateScreen.getListForEntry(this);
        int i = list.indexOf(this);
        return i > 0;
    }
	
	protected boolean func_148307_h()
    {
        List list = this.updateScreen.getListForEntry(this);
        int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1;
    }
	
	public boolean mousePressed(int par1, int par2, int par3, int par4, int par5, int par6) {
		if (par5 <= 32)
        {
            if (this.func_148309_e())
            {
                this.updateScreen.getListForEntry(this).remove(this);
                this.updateScreen.getSelectedUpdatesList().add(0, this);
                this.updateScreen.func_175288_g();
                return true;
            }

            if (par5 < 16 && this.func_148308_f())
            {
                this.updateScreen.getListForEntry(this).remove(this);
                this.updateScreen.getAvaliableUpdatesList().add(0, this);
                this.updateScreen.func_175288_g();
                return true;
            }

            List list;
            int k1;

            if (par5 > 16 && par6 < 16 && this.func_148314_g())
            {
                list = this.updateScreen.getListForEntry(this);
                k1 = list.indexOf(this);
                list.remove(this);
                list.add(k1 - 1, this);
                this.updateScreen.func_175288_g();
                return true;
            }

            if (par5 > 16 && par6 > 16 && this.func_148307_h())
            {
                list = this.updateScreen.getListForEntry(this);
                k1 = list.indexOf(this);
                list.remove(this);
                list.add(k1 + 1, this);
                this.updateScreen.func_175288_g();
                return true;
            }
        }

        return false;
	}
	
	public void setSelected(int par1, int par2, int par3) {}

	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}

	@SideOnly(Side.CLIENT)
	public enum ModTypes {
		RELEASE,
		BETA,
		ALPHA;
	}
}
