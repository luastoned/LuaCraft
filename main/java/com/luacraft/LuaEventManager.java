package com.luacraft;

import com.luacraft.classes.Vector;
import com.luacraft.console.ConsoleManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.luacraft.classes.LuaJavaBlock;
import com.naef.jnlua.LuaRuntimeException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class LuaEventManager {
	LuaCraftState l = null;

	public LuaEventManager(LuaCraftState state) {
		l = state;
	}

	// FML Bus

	// Command Events

	/**
	 * @author Jake
	 * @function command.run
	 * @info Calls whenever a command is ran
	 * @arguments [[Player]]:player, [[String]]:command, [[Table]]:arguments
	 * @return nil
	 */

	@SubscribeEvent
	public void onCommand(CommandEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("command.run");

				if (event.getSender() instanceof EntityPlayer)
					LuaUserdata.PushUserdata(l, (EntityPlayer) event.getSender());
				else
					l.pushNil();

				l.pushString(event.getCommand().getCommandName());
				l.newTable();

				for (int i = 0; i < event.getParameters().length; i++) {
					l.pushNumber(i + 1);
					l.pushString(event.getParameters()[i]);
					l.setTable(-3);
				}

				l.call(4, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	// Input Events

	/**
	 * @author Jake
	 * @function input.keypress
	 * @info Calls whenever a key is pressed
	 * @arguments [[Number]]:key, [[Boolean]]:repeat
	 * @return nil
	 */

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("input.keypress");
				l.pushNumber(Keyboard.getEventKey());
				l.pushBoolean(Keyboard.isRepeatEvent());
				l.call(3, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function input.mousepress
	 * @info Calls whenever a mouse button is pressed
	 * @arguments [[Number]]:key
	 * @return nil
	 */

	@SubscribeEvent
	public void onMouseInput(MouseInputEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("input.mousepress");
				l.pushNumber(Mouse.getEventButton());
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	// Player Events (ItemCraftedEvent, ItemPickupEvent, ItemSmeltedEvent,
	// PlayerChangedDimensionEvent, PlayerLoggedInEvent, PlayerLoggedOutEvent,
	// PlayerRespawnEvent)

	/**
	 * @author Jake
	 * @function player.craftitem
	 * @info Called whenever a player crafts a new item
	 * @arguments [[Player]]:player, [[ItemStack]]:stack
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.craftitem");
				LuaUserdata.PushUserdata(l, event.player);
				l.pushUserdataWithMeta(event.crafting, "ItemStack");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

				l.setTop(0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.pickupitem
	 * @info Called whenever a player picks up an item
	 * @arguments [[Player]]:player, [[EntityItem]]:item
	 * @return [[RESULT]]:result
	 */

	@SubscribeEvent
	public void onItemPickup(ItemPickupEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.pickupitem");
				LuaUserdata.PushUserdata(l, event.player);
				LuaUserdata.PushUserdata(l, event.pickedUp);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.smeltitem
	 * @info Called whenever a player smelts an item in a furnace
	 * @arguments [[Player]]:player, [[ItemStack]]:stack
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onItemSmelted(ItemSmeltedEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.smeltitem");
				LuaUserdata.PushUserdata(l, event.player);
				l.pushUserdataWithMeta(event.smelting, "ItemStack");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

				l.setTop(0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.changedimension
	 * @info Called whenever a player attempts to go to a new dimension
	 * @arguments [[Player]]:player, [[Number]]:fromID, [[Number]]:toID
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.changedimension");
				LuaUserdata.PushUserdata(l, event.player);
				l.pushNumber(event.fromDim);
				l.pushNumber(event.toDim);
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.connect
	 * @info Called when a player connects to the server
	 * @arguments [[Player]]:player
	 * @return nil
	 */

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.connect");
				LuaUserdata.PushUserdata(l, event.player);
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.disconnect
	 * @info Called when a player disconnects from the server
	 * @arguments [[Player]]:player
	 * @return nil
	 */

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.disconnect");
				LuaUserdata.PushUserdata(l, event.player);
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.spawned
	 * @info Called when a player spawns for the first time
	 * @arguments [[Player]]:player
	 * @return nil
	 */

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.spawned");
				LuaUserdata.PushUserdata(l, event.player);
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	// Tick Events (ClientTickEvent, PlayerTickEvent, RenderTickEvent,
	// ServerTickEvent, WorldTickEvent)

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.START)
			return;

		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("game.tick");
				l.call(1, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START)
			return;

		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("game.tick");
				l.call(1, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if (event.phase == Phase.START)
			return;

		synchronized (l) {
			if (!l.isOpen())
				return;

			if (l.getMinecraft().thePlayer == null)
				return;

			try {
				l.pushHookCall();
				l.pushString("render.tick");
				l.pushNumber(event.renderTickTime);
				l.pushNumber(event.phase.ordinal());
				l.call(3, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	// Minecraft Bus

	/**
	 * @author Jake
	 * @function player.say
	 * @info Called when a player types something in chat
	 * @arguments [[Player]]:player, [[String]]:message
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onServerChat(ServerChatEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.say");
				LuaUserdata.PushUserdata(l, event.getPlayer());
				l.pushString(event.getMessage());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// Brewing Events

	// TODO: PotionBrewedEvent

	// Item Events

	/**
	 * @author Jake
	 * @function item.expired
	 * @info Called when an item gets cleaned up from the world, after being on the ground for too long
	 * @arguments [[EntityItem]]:item
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onItemExpire(ItemExpireEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("item.expired");
				LuaUserdata.PushUserdata(l, event.getEntityItem());
				l.call(2, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.dropitem
	 * @info Called when a player drops an item on the ground
	 * @arguments [[Player]]:player, [[EntityItem]]:item
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onItemToss(ItemTossEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.dropitem");
				LuaUserdata.PushUserdata(l, event.getPlayer());
				LuaUserdata.PushUserdata(l, event.getEntityItem());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// Entity Events

	/**
	 * @author Jake
	 * @function entity.lightning
	 * @info Called when an entity is struck by lightning
	 * @arguments [[Entity]]:target, [[Entity]]:lightning
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.lightning");
				LuaUserdata.PushUserdata(l, event.getEntity());
				LuaUserdata.PushUserdata(l, event.getLightning());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinedWorld(EntityJoinWorldEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.joinworld");
				LuaUserdata.PushUserdata(l, event.getEntity());
				LuaUserdata.PushUserdata(l, event.getWorld());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// Living Events

	/**
	 * @author Jake
	 * @function entity.spawned
	 * @info Called when an entity attempts to spawn into the world
	 * @arguments [[Entity]]:entity
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onLivingSpawned(LivingSpawnEvent.CheckSpawn event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.spawned");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.call(2, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function entity.removed
	 * @info Called when an entity gets removed from the world
	 * @arguments [[Entity]]:entity
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onLivingRemoved(LivingSpawnEvent.AllowDespawn event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.removed");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.call(2, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function entity.attacked
	 * @info Called when an entity attacks another entity
	 * @arguments [[Entity]]:target, [[DamageSource]]:source, [[Number]]:damage
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.attacked");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.pushUserdataWithMeta(event.getSource(), "DamageSource");
				l.pushNumber(event.getAmount());
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// TODO: LivingHurtEvent (same as LivingAttackEvent)

	/**
	 * @author Jake
	 * @function entity.death
	 * @info Called when an entity is killed
	 * @arguments [[Entity]]:target, [[DamageSource]]:source, [[Number]]:damage
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.death");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.pushUserdataWithMeta(event.getSource(), "DamageSource");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function entity.dropall
	 * @info Called when a entity dies and drops all their loot
	 * @arguments [[Entity]]:entity, [[Table]]:drops, [[Number]]:lootLevel, [[Boolean]]:hitRecent
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onEntityDrops(LivingDropsEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.dropall");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.pushUserdataWithMeta(event.getSource(), "DamageSource");
				l.newTable();
				for (int i = 0; i < event.getDrops().size(); i++) {
					l.pushNumber(i + 1);
					LuaUserdata.PushUserdata(l, event.getDrops().get(i));
					l.setTable(-3);
				}
				l.pushNumber(event.getLootingLevel());
				l.pushBoolean(event.isRecentlyHit());
				l.call(6, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function entity.fall
	 * @info Called when an entity falls to the ground
	 * @arguments [[Entity]]:entity, [[Number]]:distance
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.fall");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.pushNumber(event.getDistance());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function entity.jump
	 * @info Called when an entity falls to the ground
	 * @arguments [[Entity]]:entity, [[Number]]:distance
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.jump");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function entity.update
	 * @info Called when an entity is updated
	 * @arguments [[Entity]]:entity
	 * @return nil
	 */

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("entity.update");
				LuaUserdata.PushUserdata(l, event.getEntity());
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	// Player Events

	// TODO: ArrowLooseEvent

	// TODO: ArrowNockEvent

	// TODO: AttackEntityEvent (covered by living?)

	@SubscribeEvent
	public void onPlayerBonemeal(BonemealEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.bonemeal");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				LuaUserdata.PushUserdata(l, event.getWorld());
				LuaUserdata.PushUserdata(l, new LuaJavaBlock(event.getEntityPlayer().worldObj, event.getPos()));
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.interact
	 * @info Called when a player attempts to interact with another entity
	 * @arguments [[Player]]:player, [[Entity]]:entity
	 * @return [[Boolean]]:cancel
	 */

	/* dead
	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.interact");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				LuaUserdata.PushUserdata(l, event.getTarget());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}
	*/

	// TODO: EntityItemPickupEvent

	// TODO: FillBucketEvent

	// TODO: HarvestCheck

	// TODO: ItemTooltipEvent

	/**
	 * @author Jake
	 * @function player.destroyitem
	 * @info Called when a player destroys an item in their inventory, normally when completing an action
	 * @arguments [[Player]]:player, [[ItemStack]]:original
	 * @return [[Boolean]]:cancel
	 */

	public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.destroyitem");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				l.pushUserdataWithMeta(event.getOriginal(), "ItemStack");
				l.call(3, 1);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.dropall
	 * @info Called when a player dies and drops all their loot
	 * @arguments [[Player]]:player, [[Table]]:drops, [[Number]]:lootLevel, [[Boolean]]:hitRecent
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.dropall");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				l.pushUserdataWithMeta(event.getSource(), "DamageSource");
				l.newTable();
				for (int i = 0; i < event.getDrops().size(); i++) {
					l.pushNumber(i + 1);
					LuaUserdata.PushUserdata(l, event.getDrops().get(i));
					l.setTable(-3);
				}
				l.pushNumber(event.getLootingLevel());
				l.pushBoolean(event.isRecentlyHit());
				l.call(6, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// TODO: PlayerFlyableFallEvent

	/**
	 * @author Jake
	 * @function player.leftclick
	 * @info Called when a player hits a block
	 * @arguments [[Player]]:player, [[Block]]:block, [[Vector]]:normal
	 * @return [[Boolean]]:cancel
	 */

	/**
	 * @author Jake
	 * @function player.rightclick
	 * @info Called when a player attempts to interact with a block
	 * @arguments [[Player]]:player, [[Block]]:block, [[Vector]]:normal
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			if (event.getPos() == null || event.getFace() == null)
				return;

			try {
				l.pushHookCall();

				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				LuaUserdata.PushUserdata(l, new LuaJavaBlock(event.getEntityPlayer().worldObj, event.getPos()));
				l.pushFace(event.getFace());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.mineblock
	 * @info Called when a player breaks a block
	 * @arguments [[Player]]:player, [[Block]]:block, [[Number]]:exp
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.mineblock");
				LuaUserdata.PushUserdata(l, event.getPlayer());
				LuaUserdata.PushUserdata(l, new LuaJavaBlock(event.getPlayer().worldObj, event.getPos()));
				l.pushNumber(event.getExpToDrop());
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.placeblock
	 * @info Called when a player places a block
	 * @arguments [[Player]]:player, [[Block]]:block
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onBlockPlace(PlaceEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.placeblock");
				LuaUserdata.PushUserdata(l, event.getPlayer());
				LuaUserdata.PushUserdata(l, new LuaJavaBlock(event.getPlayer().worldObj, event.getPos()));
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function player.blockharvestdrops
	 * @info Called when a player breaks a block and it is about to drop items
	 * @arguments [[Player]]:harvester, [[Vector]]:pos, [[number]]:drop chance, [[number]]:fortune level, [[table]]:drops
	 * @return [[number]]:set drop amount
	 */
	@SubscribeEvent
	public void onBlockHarvestDrop(BlockEvent.HarvestDropsEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.blockharvestdrops");
				LuaUserdata.PushUserdata(l, event.getHarvester());
				Vector vec = new Vector(event.getPos());
				vec.push(l);
				l.pushNumber(event.getDropChance());
				if(event.isSilkTouching())
					l.pushInteger(-1); // Pushes -1 if it is silk touch
				else
					l.pushInteger(event.getFortuneLevel());
				l.newTable();
				{
					for(int i = 0; i < event.getDrops().size(); i++) {
						l.pushInteger(i + 1);
						LuaUserdata.PushUserdata(l, event.getDrops().get(i));
						l.setTable(-3);
					}
				}
				l.call(6, 1);

				if (!l.isNil(-1))
					event.setDropChance((float) l.toNumber(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.opencontainer
	 * @info Called when a player attempts to open a container such as a chest
	 * @arguments [[Player]]:player, [[Boolean]]:interact
	 * @return [[RESULT]]:result
	 */

	@SubscribeEvent
	public void onPlayerOpenContainer(PlayerOpenContainerEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.opencontainer");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				l.pushBoolean(event.isCanInteractWith());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author Jake
	 * @function player.pickupxp
	 * @info Called when a player attempts to pickup XP orbs
	 * @arguments [[Player]]:player, [[Entity]]:orb
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onPlayerPickupXP(PlayerPickupXpEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.pickupxp");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				LuaUserdata.PushUserdata(l, event.getOrb());
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function player.onsleep
	 * @info Called when a player attempts to sleep in a bed
	 * @arguments [[Player]]:player, [[number]]:status, [[Vector]]:bedpos
	 * @return
	 */
	@SubscribeEvent
	public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.onsleep");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				l.pushInteger(event.getResultStatus().ordinal());
				Vector vec = new Vector(event.getPos());
				vec.push(l);
				l.call(4, 0);

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// TODO: PlayerUseItemEvent.Start / Stop / Finish / Tick

	/**
	 * @author fr1kin
	 * @function player.onusehoe
	 * @info Called when a player uses a hoe
	 * @arguments [[Player]]:player, [[World]]:world, [[ItemStack]]:stack, [[Vector]]:blockpos
	 * @return [[Boolean]]:cancel
	 */
	@SubscribeEvent
	public void onUseHoe(UseHoeEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.onusehoe");
				LuaUserdata.PushUserdata(l, event.getEntityPlayer());
				LuaUserdata.PushUserdata(l, event.getWorld());
				LuaUserdata.PushUserdata(l, event.getCurrent());
				Vector vec = new Vector(event.getPos());
				vec.push(l);
				l.call(5, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function chunk.load
	 * @info Called when a chunk is loaded
	 * @arguments [[Chunk]]:chunk
	 * @return
	 */
	@SubscribeEvent
	public void onChunkEventLoad(ChunkEvent.Load event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("chunk.load");
				l.pushUserdataWithMeta(event.getChunk(), "Chunk");
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function chunk.unload
	 * @info Called when a chunk is unloaded
	 * @arguments [[Chunk]]:chunk
	 * @return
	 */
	@SubscribeEvent
	public void onChunkEventUnload(ChunkEvent.Unload event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("chunk.unload");
				l.pushUserdataWithMeta(event.getChunk(), "Chunk");
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function chunk.loadfromdisk
	 * @info Called when a chunk is loaded
	 * @arguments [[Chunk]]:chunk
	 * @return
	 */
	@SubscribeEvent
	public void onChunkDataEventLoad(ChunkDataEvent.Load event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("chunk.loadfromdisk");
				l.pushUserdataWithMeta(event.getChunk(), "Chunk");
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function chunk.savetodisk
	 * @info Called when a chunk is saved
	 * @arguments [[Chunk]]:chunk
	 * @return
	 */
	@SubscribeEvent
	public void onChunkDataEventSave(ChunkDataEvent.Save event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("chunk.savetodisk");
				l.pushUserdataWithMeta(event.getChunk(), "Chunk");
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function block.preexplode
	 * @info Called when a block explodes
	 * @arguments [[World]]:world, [[Explosion]]:explosion
	 * @return [[boolean]]:cancel explosion
	 */
	@SubscribeEvent
	public void onExplosionEventStart(ExplosionEvent.Start event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("block.preexplode");
				LuaUserdata.PushUserdata(l, event.getWorld());
				l.pushUserdataWithMeta(event.getExplosion(), "Explosion");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	/**
	 * @author fr1kin
	 * @function block.postexplode
	 * @info Called when a block explodes
	 * @arguments [[World]]:world, [[Explosion]]:explosion, [[table]]:affected entities
	 * @return
	 */
	@SubscribeEvent
	public void onExplosionEventDetnoate(ExplosionEvent.Detonate event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("explosion.postexplode");
				LuaUserdata.PushUserdata(l, event.getWorld());
				l.pushUserdataWithMeta(event.getExplosion(), "Explosion");
				l.newTable();
				{
					for(int i = 0; i < event.getAffectedEntities().size(); i++) {
						l.pushInteger(i + 1);
						LuaUserdata.PushUserdata(l, event.getAffectedEntities().get(i));
						l.setTable(-3);
					}
				}
				l.call(4, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}
}
