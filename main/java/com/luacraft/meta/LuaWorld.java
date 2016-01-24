package com.luacraft.meta;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdataManager;
import com.luacraft.classes.LuaJavaBlock;
import com.luacraft.classes.Vector;
import com.luacraft.library.LuaLibUtil;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

public class LuaWorld {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushString(String.format("World [%d][%s]", self.provider.getDimensionId(),
					self.getWorldInfo().getWorldName()));
			return 1;
		}
	};

	public static JavaFunction __eq = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			World other = (World) l.checkUserdata(2, World.class, "World");
			l.pushBoolean(self == other);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetBlock
	 * @info Get a block at position within the world
	 * @arguments [[Vector]]:position OR [ [[Number]]:x, [[Number]]:y, [[Number]]:z ]
	 * @return [[Block]]:block
	 */

	public static JavaFunction GetBlock = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");

			int x, y, z;

			if (l.isUserdata(2, Vector.class)) {
				Vector thisVec = (Vector) l.checkUserdata(2, Vector.class, "Vector");
				x = (int) thisVec.x;
				y = (int) thisVec.y;
				z = (int) thisVec.z;
			} else {
				x = l.checkInteger(2, 0);
				y = l.checkInteger(3, 0);
				z = l.checkInteger(4, 0);
			}

			LuaJavaBlock thisBlock = new LuaJavaBlock(self, x, z, y);
			LuaUserdataManager.PushUserdata(l, thisBlock);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetTopBlock
	 * @info Gets the topmost block within the given X,Y coordinate
	 * @arguments [[Vector]]:position OR [ [[Number]]:x, [[Number]]:y ]
	 * @return [[Block]]:block
	 */

	public static JavaFunction GetTopBlock = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");

			int x, y;

			if (l.isUserdata(2, Vector.class)) {
				Vector thisVec = (Vector) l.checkUserdata(2, Vector.class, "Vector");
				x = (int) thisVec.x;
				y = (int) thisVec.y;
			} else {
				x = l.checkInteger(2, 0);
				y = l.checkInteger(3, 0);
			}

			int k = 63;

			while (!self.isAirBlock(new BlockPos(x, k + 1, y)))
				k++;

			LuaJavaBlock thisBlock = new LuaJavaBlock(self, x, k, y);
			LuaUserdataManager.PushUserdata(l, thisBlock);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetPlayers
	 * @info Get all players within the world
	 * @arguments nil
	 * @return [[Table]]:players
	 */

	public static JavaFunction GetPlayers = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			List<EntityPlayer> playerList = self.playerEntities;

			l.newTable();
			int i = 1;
			for (EntityPlayer player : playerList) {
				l.pushInteger(i++);
				LuaUserdataManager.PushUserdata(l, player);
				l.setTable(-3);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetPlayerByName
	 * @info Attempts to find a player using the provided string
	 * @arguments [[String]]:search
	 * @return [[Player]]:player
	 */

	public static JavaFunction GetPlayerByName = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			String search = l.checkString(2).toLowerCase();
			List<EntityPlayer> playerList = self.playerEntities;

			for (EntityPlayer player : playerList) {
				String playerName = player.getGameProfile().getName().toLowerCase();
				if (playerName.contains(search)) {
					LuaUserdataManager.PushUserdata(l, player);
					return 1;
				}
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetEntity
	 * @info Gets an entity by its index
	 * @arguments [[Number]]:entindex
	 * @return [[Entity]]:entity
	 */

	public static JavaFunction GetEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			int searchId = l.checkInteger(2);

			List<Entity> entityList = self.loadedEntityList;

			for (Entity ent : entityList)
				if (ent.getEntityId() == searchId) {
					LuaUserdataManager.PushUserdata(l, ent);
					return 1;
				}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetEntities
	 * @info Get all entities within the world
	 * @arguments nil
	 * @return [[Table]]:players
	 */

	public static JavaFunction GetEntities = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			List<Entity> entityList = self.loadedEntityList;

			l.newTable();
			int i = 1;
			for (Entity ent : entityList) {
				l.pushInteger(i++);
				LuaUserdataManager.PushUserdata(l, ent);
				l.setTable(-3);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetEntitiesByClass
	 * @info Get all entities that match the provided class
	 * @arguments [[String]]:classname
	 * @return [[Table]]:entities
	 */

	public static JavaFunction GetEntitiesByClass = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			String search = l.checkString(2);
			List<Entity> entityList = self.loadedEntityList;

			l.newTable();
			int i = 1;
			for (Entity ent : entityList) {
				String className = EntityList.getEntityString(ent).toLowerCase();

				if (className.contains(search)) {
					l.pushInteger(i++);
					LuaUserdataManager.PushUserdata(l, ent);
					l.setTable(-3);
				}
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function CreateEntity
	 * @info Creates a new entity
	 * @arguments [[String]]:classname
	 * @return [[Entity]]:entity
	 */

	public static JavaFunction CreateEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			Entity ent = EntityList.createEntityByName(l.checkString(2), self);
			LuaUserdataManager.PushUserdata(l, ent);
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsDayTime
	 * @info Checks if the sun is up
	 * @arguments nil
	 * @return [[Boolean]]:day
	 */

	public static JavaFunction IsDayTime = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushBoolean(self.isDaytime());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsNightTime
	 * @info Checks if the moon is up
	 * @arguments nil
	 * @return [[Boolean]]:night
	 */

	public static JavaFunction IsNightTime = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushBoolean(!self.isDaytime());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetTime
	 * @info Returns the worlds time
	 * @arguments nil
	 * @return [[Number]]:time
	 */

	public static JavaFunction GetTime = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushNumber(self.getWorldTime());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetTime
	 * @info Sets the worlds time
	 * @arguments [[Number]]:time
	 * @return nil
	 */

	public static JavaFunction SetTime = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			self.setWorldTime((long) l.checkNumber(2));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function IsRaining
	 * @info Check if the rain is falling
	 * @arguments nil
	 * @return [[Boolean]]:rain
	 */

	public static JavaFunction IsRaining = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushBoolean(self.getWorldInfo().isRaining());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetRaining
	 * @info Set the weather to rainfall
	 * @arguments [[Boolean]]:rain
	 * @return nil
	 */

	public static JavaFunction SetRaining = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			self.getWorldInfo().setRaining(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function IsStorming
	 * @info Check if it is storming
	 * @arguments nil
	 * @return [[Boolean]]:storm
	 */

	public static JavaFunction IsStorming = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushBoolean(self.getWorldInfo().isThundering());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetStorming
	 * @info Set the weather to storm
	 * @arguments [[Boolean]]:storm
	 * @return nil
	 */

	public static JavaFunction SetStorming = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			self.getWorldInfo().setThundering(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetDifficulty
	 * @info Returns the worlds difficulty
	 * @arguments nil
	 * @return [[Number]]:diff
	 */

	public static JavaFunction GetDifficulty = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushNumber(self.getDifficulty().ordinal());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetDifficulty
	 * @info Sets the worlds difficulty
	 * @arguments [[Number]]:diff
	 * @return nil
	 */

	public static JavaFunction SetDifficulty = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			self.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(l.checkInteger(2)));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetSpawnPos
	 * @info Returns the worlds spawnposition
	 * @arguments nil
	 * @return [[Vector]]:pos
	 */

	public static JavaFunction GetSpawnPos = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			WorldInfo info = self.getWorldInfo();
			Vector pos = new Vector(info.getSpawnX(), info.getSpawnZ(), info.getSpawnY());
			pos.push(l);
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetSpawnPos
	 * @info Sets the worlds spawnposition
	 * @arguments [[Vector]]:pos
	 * @return nil
	 */

	public static JavaFunction SetSpawnPos = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			WorldInfo info = self.getWorldInfo();
			info.setSpawn(new BlockPos(pos.x, pos.z, pos.y));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetAnimalSpawning
	 * @info Check if animals are spawning
	 * @arguments nil
	 * @return [[Boolean]]:animals
	 */

	public static JavaFunction GetAnimalSpawning = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushBoolean((Boolean) ReflectionHelper.getPrivateValue(World.class, self, "spawnPeacefulMobs"));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function SetAnimalSpawning
	 * @info Set animals spawning
	 * @arguments [[Boolean]]:animals
	 * @return nil
	 */

	public static JavaFunction SetAnimalSpawning = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			ReflectionHelper.setPrivateValue(World.class, self, l.checkBoolean(2), "spawnPeacefulMobs");
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetMobSpawning
	 * @info Check if mobs are spawning
	 * @arguments nil
	 * @return [[Boolean]]:mobs
	 */

	public static JavaFunction GetMobSpawning = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushBoolean((Boolean) ReflectionHelper.getPrivateValue(World.class, self, "spawnHostileMobs"));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetMobSpawning
	 * @info Set mobs spawning
	 * @arguments [[Boolean]]:mobs
	 * @return nil
	 */

	public static JavaFunction SetMobSpawning = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			ReflectionHelper.setPrivateValue(World.class, self, l.checkBoolean(2), "spawnHostileMobs");
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetGamemode
	 * @info Returns the worlds gamemode
	 * @arguments nil
	 * @return [[Number]]:gamemode
	 */

	public static JavaFunction GetGamemode = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushInteger(self.getWorldInfo().getGameType().ordinal());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetGamemode
	 * @info Set the worlds gamemode
	 * @arguments [[Number]]:gamemode
	 * @return nil
	 */

	public static JavaFunction SetGamemode = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			self.getWorldInfo().setGameType(WorldSettings.GameType.getByID(l.checkInteger(2)));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetSeed
	 * @info Returns the worlds seed
	 * @arguments nil
	 * @return [[Number]]:seed
	 */

	public static JavaFunction GetSeed = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushNumber(self.getWorldInfo().getSeed());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddLightning
	 * @info Adds an lightning bolt to the world
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction AddLightning = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.addWeatherEffect(new EntityLightningBolt(self, pos.x, pos.z, pos.y));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function AddExplosion
	 * @info Adds an explosion to the world
	 * @arguments [[Vector]]:pos, [ [[Number]]:size, [[Boolean]]:flaming, [[Boolean]]:smoking ]
	 * @return nil
	 */

	public static JavaFunction AddExplosion = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.newExplosion(null, pos.x, pos.z, pos.y, (float) l.checkNumber(3, 5), l.checkBoolean(4, false),
					l.checkBoolean(5, false));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetDimension
	 * @info Returns the worlds dimension number
	 * @arguments nil
	 * @return [[Number]]:dimension
	 */

	public static JavaFunction GetDimension = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.pushNumber(self.provider.getDimensionId());
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function GetBiome
	 * @info Returns the current biome of a position.
	 * @arguments [[Vector]]:pos
	 * @return [[String]]:biome
	 */

	public static JavaFunction GetBiome = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			l.pushString(
					self.getWorldChunkManager().getBiomeGenAt(null, (int) pos.x, (int) pos.y, 0, 0, false).toString());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function EmitSound
	 * @info Play a sound emitting from the position given
	 * @arguments [[Vector]]:Position, [[String]]:Sound name, [ [[Number]]:Pitch, [[Number]]:Volume ]
	 * @return nil
	 */

	public static JavaFunction EmitSound = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.playSoundEffect(pos.x, pos.z, pos.y, l.checkString(3), (float) l.checkNumber(4, 1),
					(float) l.checkNumber(5, 1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function TraceLine
	 * @info Trace a line from one point to another
	 * @arguments [[TraceTable]]:trace
	 * @return [[TraceHitTable]]:hit
	 */

	public static JavaFunction TraceLine = new JavaFunction() {
		public int invoke(LuaState l) {
			World self = (World) l.checkUserdata(1, World.class, "World");
			l.checkType(2, LuaType.TABLE);

			l.getField(2, "start");
			Vector start = (Vector) l.toUserdata(-1);
			l.pop(1);

			l.getField(2, "endpos");
			Vector endpos = (Vector) l.toUserdata(-1);
			l.pop(1);

			l.getField(2, "hitwater");
			boolean hitWater = l.toBoolean(-1);
			l.pop(1);

			List<Entity> filter = new ArrayList<Entity>();

			l.getField(2, "filter");
			if (!l.isNil(-1)) {

				if (l.isUserdata(-1, Entity.class))
					filter.add((Entity) l.toUserdata(-1));

				if (l.isTable(-1)) {
					l.pushNil();

					while (l.next(-2)) {

						if (l.isUserdata(-1, Entity.class))
							filter.add((Entity) l.toUserdata(-1));

						l.pop(1); // Pop the value, keep the key
					}
				}
			}
			l.pop(1);

			LuaLibUtil.pushTrace((LuaCraftState) l, self, start, endpos, hitWater, filter);
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("World");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(__eq);
			l.setField(-2, "__eq");

			l.pushJavaFunction(GetBlock);
			l.setField(-2, "GetBlock");
			l.pushJavaFunction(GetTopBlock);
			l.setField(-2, "GetTopBlock");
			l.pushJavaFunction(GetPlayers);
			l.setField(-2, "GetPlayers");
			l.pushJavaFunction(GetPlayerByName);
			l.setField(-2, "GetPlayerByName");
			l.pushJavaFunction(GetEntity);
			l.setField(-2, "GetEntity");
			l.pushJavaFunction(GetEntities);
			l.setField(-2, "GetEntities");
			l.pushJavaFunction(GetEntitiesByClass);
			l.setField(-2, "GetEntitiesByClass");
			l.pushJavaFunction(CreateEntity);
			l.setField(-2, "CreateEntity");
			l.pushJavaFunction(IsDayTime);
			l.setField(-2, "IsDayTime");
			l.pushJavaFunction(GetTime);
			l.setField(-2, "GetTime");
			l.pushJavaFunction(SetTime);
			l.setField(-2, "SetTime");
			l.pushJavaFunction(IsRaining);
			l.setField(-2, "IsRaining");
			l.pushJavaFunction(SetRaining);
			l.setField(-2, "SetRaining");
			l.pushJavaFunction(IsStorming);
			l.setField(-2, "IsStorming");
			l.pushJavaFunction(SetStorming);
			l.setField(-2, "SetStorming");
			l.pushJavaFunction(GetDifficulty);
			l.setField(-2, "GetDifficulty");
			l.pushJavaFunction(SetDifficulty);
			l.setField(-2, "SetDifficulty");
			l.pushJavaFunction(GetSpawnPos);
			l.setField(-2, "GetSpawnPos");
			l.pushJavaFunction(SetSpawnPos);
			l.setField(-2, "SetSpawnPos");
			l.pushJavaFunction(GetAnimalSpawning);
			l.setField(-2, "GetAnimalSpawning");
			l.pushJavaFunction(SetAnimalSpawning);
			l.setField(-2, "SetAnimalSpawning");
			l.pushJavaFunction(GetMobSpawning);
			l.setField(-2, "GetMobSpawning");
			l.pushJavaFunction(SetMobSpawning);
			l.setField(-2, "SetMobSpawning");
			l.pushJavaFunction(GetGamemode);
			l.setField(-2, "GetGamemode");
			l.pushJavaFunction(SetGamemode);
			l.setField(-2, "SetGamemode");
			l.pushJavaFunction(GetSeed);
			l.setField(-2, "GetSeed");
			l.pushJavaFunction(AddLightning);
			l.setField(-2, "AddLightning");
			l.pushJavaFunction(AddExplosion);
			l.setField(-2, "AddExplosion");
			l.pushJavaFunction(GetDimension);
			l.setField(-2, "GetDimension");
			l.pushJavaFunction(GetBiome);
			l.setField(-2, "GetBiome");
			l.pushJavaFunction(EmitSound);
			l.setField(-2, "EmitSound");
			l.pushJavaFunction(TraceLine);
			l.setField(-2, "TraceLine");
		}
		l.pop(1);
	}
}
