package org.freeforums.geforce.beacon.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

public class GuiUpdatesAvailableList extends GuiListExtended{
	
	private List list;

	public GuiUpdatesAvailableList(Minecraft mc, int par2, int par3, List par5List) {
		super(mc, par2, par3, 32, par3 - 55 + 4, 36);
		this.list = par5List;
		this.field_148163_i = false;
		this.setHasListHeader(true, (int)((float)mc.fontRendererObj.FONT_HEIGHT * 1.5F));
	}
	
	protected void drawListHeader(int par1, int par2, Tessellator par3)
    {
        String s = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + "Avaliable mod updates";
        this.mc.fontRendererObj.drawString(s, par1 + this.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, Math.min(this.top + 3, par2), 16777215);
    }

	public ModUpdateListEntry getListEntry(int par1) {
		return (ModUpdateListEntry) list.get(par1);
	}
	
	public List getList()
    {
        return this.list;
    }

	protected int getSize() {
		return list.size();
	}
	
	public int getListWidth()
    {
        return this.width;
    }

    protected int getScrollBarX()
    {
        return this.right - 6;
    }

}
