package com.wtbw.mods.machines;

import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import net.minecraft.util.ResourceLocation;

/*
  @author: Naxanria
*/
public class ClientConstants
{
  /**
   * Shortcut for localization
   * @param key name for key
   * @return the localization key
   */
  public static String getKey(String key)
  {
    return WTBWMachines.MODID + "." + key;
  }
  
  public static ResourceLocation getLocation(String key)
  {
    return new ResourceLocation(WTBWMachines.MODID, key);
  }
  
  public static ResourceLocation getTextureLocation(String key)
  {
    return getLocation("textures/" + key);
  }
  
  public static ResourceLocation getGuiLocation(String key)
  {
    return getTextureLocation("gui/" + key);
  }
  
  public static final ResourceLocation ICONS = getGuiLocation("icons.png");
  
  public static String getTooltipKey(String key)
  {
    return WTBWMachines.MODID + ".tooltip." + key;
  }
  
  public static class Tooltips
  {
    public static final String EF_TICK = getTooltipKey("ef_tick");
  }
  
  public static class Gui
  {
    public static final SpriteMap ICONS = new SpriteMap(256, getGuiLocation("icons.png"));
    
    public static String getKey(String key)
    {
      return ClientConstants.getKey("gui." + key);
    }
  }
  
  public static class Jei
  {
    public static final ResourceLocation BACKGROUND = getGuiLocation("jei/backgrounds.png");
  }
}
