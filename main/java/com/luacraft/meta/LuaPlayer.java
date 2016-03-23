package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Vector;
import com.luacraft.library.LuaLibUtil;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;

public class LuaPlayer {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushString(String.format("Player [%d][%s]", self.getEntityId(), self.getGameProfile().getName()));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetName
	 * @info Get a players name
	 * @arguments nil
	 * @return [[String]]:name
	 */

	public static JavaFunction GetName = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushString(self.getDisplayName());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetUserName
	 * @info Get a players display name
	 * @arguments nil
	 * @return [[String]]:name
	 */

	public static JavaFunction GetUserName = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushString(self.getDisplayName());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetScore
	 * @info Return the players score
	 * @arguments nil
	 * @return [[Number]]:score
	 */

	public static JavaFunction GetScore = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushInteger(self.getScore());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddScore
	 * @info Add to the players score
	 * @arguments [ [[Number]]:add ]
	 * @return nil
	 */

	public static JavaFunction AddScore = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.addScore(l.checkInteger(2, 1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetScore
	 * @info Set the players score
	 * @arguments [[Number]]:score
	 * @return nil
	 */

	public static JavaFunction SetScore = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.setScore(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function SetHunger
	 * @info Set the players hunger (reaches from 0 to 20)
	 * @arguments [[Number]]:hunger
	 * @return nil
	 */

	public static JavaFunction SetHunger = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.getFoodStats().setFoodLevel(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetHunger
	 * @info Returns the players hunger
	 * @arguments nil
	 * @return [[Number]]:hunger
	 */

	public static JavaFunction GetHunger = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushInteger(self.getFoodStats().getFoodLevel());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetArmor
	 * @info Returns the players armor value
	 * @arguments nil
	 * @return [[Number]]:armor
	 */

	public static JavaFunction GetArmor = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushInteger(self.inventory.getTotalArmorValue());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetInventory
	 * @info Returns a table containing [[Item]] / [[ItemStack]] objects
	 * @arguments nil
	 * @return [[Table]]:inv
	 */

	public static JavaFunction GetInventory = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");

			l.newTable();

			InventoryPlayer inventory = self.inventory;

			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack item = inventory.getStackInSlot(i);
				if (item != null) {
					l.pushInteger(i + 1);
					l.pushUserdataWithMeta(item, "ItemStack");
					l.setTable(-3);
				}
			}

			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetEquipment
	 * @info Returns a table containing [[Item]] / [[ItemStack]] objects
	 * @arguments nil
	 * @return [[Table]]:equipment
	 */

	public static JavaFunction GetEquipment = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");

			l.newTable();

			ItemStack[] inventory = self.inventory.armorInventory;

			for (int i = 0; i < inventory.length; i++) {
				ItemStack item = inventory[i];
				if (item != null) {
					l.pushInteger(i + 1);
					l.pushUserdataWithMeta(item, "ItemStack");
					l.setTable(-3);
				}
			}

			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetActiveSlot
	 * @info Returns an [[Item]] / [[ItemStack]] object
	 * @arguments nil
	 * @return [[ItemStack]]:item, [[Number]]:slot
	 */

	public static JavaFunction GetActiveSlot = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushUserdataWithMeta(self.getCurrentEquippedItem(), "ItemStack");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetCreative
	 * @info Sets if the player is in creative mode or not
	 * @arguments [[Boolean]]:creative
	 * @return nil
	 */

	public static JavaFunction SetCreative = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.capabilities.isCreativeMode = l.checkBoolean(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function IsCreative
	 * @info Returns whether or not the player is in creative
	 * @arguments nil
	 * @return [[Boolean]]:creative
	 */

	public static JavaFunction IsCreative = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushBoolean(self.capabilities.isCreativeMode);
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsSleeping
	 * @info Check if the player is in a bed
	 * @arguments nil
	 * @return [[Boolean]]:inbed
	 */

	public static JavaFunction IsSleeping = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushBoolean(self.isPlayerSleeping());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsFullyAsleep
	 * @info Check if the player is fully asleep
	 * @arguments nil
	 * @return [[Boolean]]:asleep
	 */

	public static JavaFunction IsFullyAsleep = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushBoolean(self.isPlayerFullyAsleep());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetExperience
	 * @info Returns the percentage of completion the player has for the current level
	 * @arguments nil
	 * @return [[Number]]:exp
	 */

	public static JavaFunction GetExperience = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushNumber(self.experience);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetTotalExperience
	 * @info Returns a players total experience
	 * @arguments nil
	 * @return [[Number]]:totalexp
	 */

	public static JavaFunction GetTotalExperience = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushNumber(self.experienceTotal);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetLevel
	 * @info Returns the players experience level
	 * @arguments nil
	 * @return [[Number]]:level
	 */

	public static JavaFunction GetLevel = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushNumber(self.experienceLevel);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetLevel
	 * @info Sets the players experience level
	 * @arguments [[Number]]:level
	 * @return nil
	 */

	public static JavaFunction SetLevel = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.experienceLevel = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetLevelCap
	 * @info Returns the max amount of experience needed for level up
	 * @arguments nil
	 * @return [[Number]]:lvlcap
	 */

	public static JavaFunction GetLevelCap = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushInteger(self.xpBarCap());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddExperience
	 * @info Adds player experience to their current level
	 * @arguments [[Number]]:exp
	 * @return nil
	 */

	public static JavaFunction AddExperience = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.addExperience(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SubExperience
	 * @info Subtracts player experience from their current level
	 * @arguments [[Number]]:exp
	 * @return nil
	 */

	public static JavaFunction SubExperience = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			int diff = self.experienceTotal - l.checkInteger(2);
			self.experienceLevel = 0;
			self.experience = 0;
			self.experienceTotal = 0;
			self.addExperience(diff);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetExperience
	 * @info Sets a players experience percentage for their current level
	 * @arguments [[Number]]:exp
	 * @return nil
	 */

	public static JavaFunction SetExperience = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.experience = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetTotalExperience
	 * @info Sets a players total experience
	 * @arguments [[Number]]:totalexp
	 * @return nil
	 */

	public static JavaFunction SetTotalExperience = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.experienceTotal = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetGod
	 * @info Set if the player should take damage or not
	 * @arguments [[Boolean]]:godmode
	 * @return nil
	 */

	public static JavaFunction SetGod = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.capabilities.disableDamage = l.checkBoolean(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function IsGod
	 * @info Return if the player is in god mode or not
	 * @arguments [[Boolean]]:godmode
	 * @return nil
	 */

	public static JavaFunction IsGod = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushBoolean(self.capabilities.disableDamage);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetSkinURL
	 * @info Return the players url to their minecraft skin
	 * @arguments nil
	 * @return [[String]]url
	 */

	public static JavaFunction GetSkinURL = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushString("http://s3.amazonaws.com/MinecraftSkins/" + self.getDisplayName() + ".png");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetContainer
	 * @info Get's the container object for the player
	 * @arguments nil
	 * @return [[Container]]:inv
	 */

	public static JavaFunction GetContainer = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushUserdataWithMeta(self.inventory, "Container");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetBedPos
	 * @info Set a players bed position
	 * @arguments [[Vector]]:vec, [ [[Boolean]]:override, [[Number]]:dimensionID ]
	 * @return nil
	 */

	public static JavaFunction SetBedPos = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			ChunkCoordinates bedPos = new ChunkCoordinates((int) pos.x, (int) pos.z, (int) pos.y);
			self.setSpawnChunk(bedPos, l.checkBoolean(3, false), l.checkInteger(4, self.dimension));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetBedPos
	 * @info Returns an open area around the players set bed position
	 * @arguments nil
	 * @return [[Vector]]:vec
	 */

	public static JavaFunction GetBedPos = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			ChunkCoordinates bed = self.getBedLocation(l.checkInteger(2, self.dimension));
			if (bed != null) {
				Vector pos = new Vector(bed.posX, bed.posZ, bed.posY);
				pos.push(l);
				return 1;
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetFlying
	 * @info Set whether or not the player is flying
	 * @arguments [[Boolean]]:flying
	 * @return nil
	 */

	public static JavaFunction SetFlying = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.capabilities.isFlying = l.checkBoolean(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function IsFlying
	 * @info Returns if the player is flying
	 * @arguments nil
	 * @return [[Boolean]]:flying
	 */

	public static JavaFunction IsFlying = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushBoolean(self.capabilities.isFlying);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetFlightAllowed
	 * @info Set whether or not the player is allowed to fly
	 * @arguments [[Boolean]]:enabled
	 * @return nil
	 */

	public static JavaFunction SetFlightAllowed = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			self.capabilities.allowFlying = l.checkBoolean(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function IsFlyingAllowed
	 * @info Returns if the player is allowed to fly
	 * @arguments nil
	 * @return [[Boolean]]:flying
	 */

	public static JavaFunction IsFlightAllowed = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushBoolean(self.capabilities.allowFlying);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetTeam
	 * @info Returns the players current team
	 * @arguments nil
	 * @return [[Team]]:team
	 */

	public static JavaFunction GetTeam = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayer.class, "Player");
			l.pushUserdataWithMeta(self.getTeam(), "Team");
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function ChatPrint
	 * @alias Msg
	 * @info Print something to a players chat
	 * @arguments [[String]]:msg OR [[Number]]:color, ...
	 * @return nil
	 */

	public static JavaFunction ChatPrint = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayer self = (EntityPlayer) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			String chatMsg = LuaLibUtil.toChat(l, 2);
			self.addChatMessage(new ChatComponentText(chatMsg));
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Player");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, true);

			l.newMetatable("LivingBase");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(GetName);
			l.setField(-2, "GetName");
			l.pushJavaFunction(GetUserName);
			l.setField(-2, "GetUserName");
			l.pushJavaFunction(GetScore);
			l.setField(-2, "GetScore");
			l.pushJavaFunction(AddScore);
			l.setField(-2, "AddScore");
			l.pushJavaFunction(SetScore);
			l.setField(-2, "SetScore");
			l.pushJavaFunction(SetHunger);
			l.setField(-2, "SetHunger");
			l.pushJavaFunction(GetHunger);
			l.setField(-2, "GetHunger");
			l.pushJavaFunction(GetArmor);
			l.setField(-2, "GetArmor");
			l.pushJavaFunction(GetInventory);
			l.setField(-2, "GetInventory");
			l.pushJavaFunction(GetEquipment);
			l.setField(-2, "GetEquipment");
			l.pushJavaFunction(GetActiveSlot);
			l.setField(-2, "GetActiveSlot");
			l.pushJavaFunction(SetCreative);
			l.setField(-2, "SetCreative");
			l.pushJavaFunction(IsCreative);
			l.setField(-2, "IsCreative");
			l.pushJavaFunction(IsSleeping);
			l.setField(-2, "IsSleeping");
			l.pushJavaFunction(IsFullyAsleep);
			l.setField(-2, "IsFullyAsleep");
			l.pushJavaFunction(GetExperience);
			l.setField(-2, "GetExperience");
			l.pushJavaFunction(GetTotalExperience);
			l.setField(-2, "GetTotalExperience");
			l.pushJavaFunction(GetLevel);
			l.setField(-2, "GetLevel");
			l.pushJavaFunction(SetLevel);
			l.setField(-2, "SetLevel");
			l.pushJavaFunction(GetLevelCap);
			l.setField(-2, "GetLevelCap");
			l.pushJavaFunction(AddExperience);
			l.setField(-2, "AddExperience");
			l.pushJavaFunction(SubExperience);
			l.setField(-2, "SubExperience");
			l.pushJavaFunction(SetExperience);
			l.setField(-2, "SetExperience");
			l.pushJavaFunction(SetTotalExperience);
			l.setField(-2, "SetTotalExperience");
			l.pushJavaFunction(SetGod);
			l.setField(-2, "SetGod");
			l.pushJavaFunction(IsGod);
			l.setField(-2, "IsGod");
			l.pushJavaFunction(GetSkinURL);
			l.setField(-2, "GetSkinURL");
			l.pushJavaFunction(GetContainer);
			l.setField(-2, "GetContainer");
			l.pushJavaFunction(SetBedPos);
			l.setField(-2, "SetBedPos");
			l.pushJavaFunction(GetBedPos);
			l.setField(-2, "GetBedPos");
			l.pushJavaFunction(SetFlying);
			l.setField(-2, "SetFlying");
			l.pushJavaFunction(IsFlying);
			l.setField(-2, "IsFlying");
			l.pushJavaFunction(SetFlightAllowed);
			l.setField(-2, "SetFlightAllowed");
			l.pushJavaFunction(IsFlightAllowed);
			l.setField(-2, "IsFlightAllowed");
			l.pushJavaFunction(GetTeam);
			l.setField(-2, "GetTeam");
			l.pushJavaFunction(ChatPrint);
			l.setField(-2, "Msg");
			l.pushJavaFunction(ChatPrint);
			l.setField(-2, "ChatPrint");
		}
		l.pop(1);
	}
}
