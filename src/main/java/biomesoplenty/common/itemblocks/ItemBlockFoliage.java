package biomesoplenty.common.itemblocks;

import biomesoplenty.api.BOPBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockFoliage extends ItemColored
{
	private static final String[] foliageTypes = new String[] {"algae", "shortgrass", "mediumgrass", "hedgebottom", "bush", "sprout", "hedgetop", "poisonivy", "berrybush", "shrub", "wheatgrass", "dampgrass", "koru", "cloverpatch", "leafpile", "deadleafpile"};
	@SideOnly(Side.CLIENT)
	private IIcon[] textures;
	private static final int HEDGETOP = 6;

	public ItemBlockFoliage(Block block)
	{
		super(block, true);
		
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		textures = new IIcon[foliageTypes.length];

		for (int i = 0; i < foliageTypes.length; ++i) {
			textures[i] = iconRegister.registerIcon("biomesoplenty:" + foliageTypes[i]);
		}

		textures[3] = iconRegister.registerIcon("biomesoplenty:hedgetop");
		textures[8] = iconRegister.registerIcon("biomesoplenty:item_berrybush");
		textures[9] = iconRegister.registerIcon("biomesoplenty:item_shrub");
		textures[15] = iconRegister.registerIcon("biomesoplenty:deadleafpile");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2)
	{
		if (itemStack.getItemDamage() == 8 || itemStack.getItemDamage() == 9 || itemStack.getItemDamage() == 15)
			return 16777215;
		else
			//TODO:							 getRenderColor()
			return BOPBlockHelper.get("foliage").getRenderColor(itemStack.getItemDamage());
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta & 15;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= foliageTypes.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + foliageTypes[meta];
	}

	@Override
	public IIcon getIconFromDamage(int meta)
	{
		if (meta == HEDGETOP) {
			meta = 3;
		}
		
		return textures[meta];
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (itemStack.getItemDamage() != 0)
			return super.onItemRightClick(itemStack, world, player);

		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

		if (movingobjectposition == null)
			return itemStack;
		else
		{
			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
			{
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!world.canMineBlock(player, i, j, k))
					return itemStack;

				if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack))
					return itemStack;

				//TODO:	  getBlock()			getMaterial()						water															isAirBlock()
				if (world.getBlock(i, j, k).getMaterial() == Material.water && world.getBlockMetadata(i, j, k) == 0 && world.isAirBlock(i, j + 1, k))
				{
					//TODO:	setBlock()				linkedBlock
					world.setBlock(i, j + 1, k, field_150939_a, 0, 2);

					if (!player.capabilities.isCreativeMode)
					{
						--itemStack.stackSize;
					}
				}
			}

			return itemStack;
		}
	}
	
    @Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
    	if (metadata == 3) 
    	{
    		if (!placeBlockAt(stack, player, world, x, y + 1, z, side, hitX, hitY + 1, hitZ, HEDGETOP)) return false;
    	}

    	return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }
}
