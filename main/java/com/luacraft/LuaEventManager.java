package com.luacraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.luacraft.classes.LuaJavaBlock;

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
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
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

				if (event.sender instanceof EntityPlayer)
					LuaUserdataManager.PushUserdata(l, (EntityPlayer) event.sender);
				else
					l.pushNil();

				l.pushString(event.command.getCommandName());
				l.newTable();

				for (int i = 0; i < event.parameters.length; i++) {
					l.pushNumber(i + 1);
					l.pushString(event.parameters[i]);
					l.setTable(-3);
				}

				l.call(4, 0);
			} catch (Exception e) {
				l.handleException(e);
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
			} catch (Exception e) {
				l.handleException(e);
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
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.pushUserdataWithMeta(event.crafting, "ItemStack");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

				l.setTop(0);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				LuaUserdataManager.PushUserdata(l, event.pickedUp);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.pushUserdataWithMeta(event.smelting, "ItemStack");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

				l.setTop(0);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.pushNumber(event.fromDim);
				l.pushNumber(event.toDim);
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.call(2, 0);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.call(2, 0);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.call(2, 0);
			} catch (Exception e) {
				l.handleException(e);
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
			} catch (Exception e) {
				l.handleException(e);
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
			} catch (Exception e) {
				l.handleException(e);
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
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				l.pushString(event.message);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entityItem);
				l.call(2, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.player);
				LuaUserdataManager.PushUserdata(l, event.entityItem);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				LuaUserdataManager.PushUserdata(l, event.lightning);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				LuaUserdataManager.PushUserdata(l, event.world);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.call(2, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.call(2, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.pushUserdataWithMeta(event.source, "DamageSource");
				l.pushNumber(event.ammount);
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.pushUserdataWithMeta(event.source, "DamageSource");
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.pushUserdataWithMeta(event.source, "DamageSource");
				l.newTable();
				for (int i = 0; i < event.drops.size(); i++) {
					l.pushNumber(i + 1);
					LuaUserdataManager.PushUserdata(l, event.drops.get(i));
					l.setTable(-3);
				}
				l.pushNumber(event.lootingLevel);
				l.pushBoolean(event.recentlyHit);
				l.call(6, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.pushNumber(event.distance);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.call(2, 0);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entity);
				l.call(2, 0);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				LuaUserdataManager.PushUserdata(l, event.world);

				LuaJavaBlock thisBlock = new LuaJavaBlock(event.entityPlayer.worldObj, event.pos);
				LuaUserdataManager.PushUserdata(l, thisBlock);
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (Exception e) {
				l.handleException(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// TODO: BreakSpeed

	/**
	 * @author Jake
	 * @function player.interact
	 * @info Called when a player attempts to interact with another entity
	 * @arguments [[Player]]:player, [[Entity]]:entity
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();
				l.pushString("player.interact");
				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				LuaUserdataManager.PushUserdata(l, event.target);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
			} finally {
				l.setTop(0);
			}
		}
	}

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
				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				l.pushUserdataWithMeta(event.original, "ItemStack");
				l.call(3, 1);
			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				l.pushUserdataWithMeta(event.source, "DamageSource");
				l.newTable();
				for (int i = 0; i < event.drops.size(); i++) {
					l.pushNumber(i + 1);
					LuaUserdataManager.PushUserdata(l, event.drops.get(i));
					l.setTable(-3);
				}
				l.pushNumber(event.lootingLevel);
				l.pushBoolean(event.recentlyHit);
				l.call(6, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// TODO: PlayerFlyableFallEvent

	/**
	 * @author Jake
	 * @function player.mineblock
	 * @info Called when a player attempts to mine a block
	 * @arguments [[Player]]:player, [[Block]]:block, [[Vector]]:normal
	 * @return [[Boolean]]:cancel
	 */

	/**
	 * @author Jake
	 * @function player.rightclick
	 * @info Called when a player presses right mouse on nothing
	 * @arguments [[Player]]:player, [[Block]]:block, [[Vector]]:normal
	 * @return [[Boolean]]:cancel
	 */

	/**
	 * @author Jake
	 * @function player.placeblock
	 * @info Called when a player attempts to place a block
	 * @arguments [[Player]]:player, [[Block]]:block, [[Vector]]:normal
	 * @return [[Boolean]]:cancel
	 */

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				l.pushHookCall();

				switch (event.action) {
				case LEFT_CLICK_BLOCK:
					l.pushString("player.mineblock");
					break;
				case RIGHT_CLICK_AIR:
					l.pushString("player.rightclick");
					break;
				case RIGHT_CLICK_BLOCK:
					l.pushString("player.placeblock");
					break;
				}

				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				LuaJavaBlock thisBlock = new LuaJavaBlock(event.entityPlayer.worldObj, event.pos);
				LuaUserdataManager.PushUserdata(l, thisBlock);
				l.pushFace(event.face);
				l.call(4, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				l.pushBoolean(event.canInteractWith);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setResult(Result.values()[l.checkInteger(-1, Result.DEFAULT.ordinal())]);

			} catch (Exception e) {
				l.handleException(e);
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
				LuaUserdataManager.PushUserdata(l, event.entityPlayer);
				LuaUserdataManager.PushUserdata(l, event.orb);
				l.call(3, 1);

				if (!l.isNil(-1))
					event.setCanceled(l.toBoolean(-1));

			} catch (Exception e) {
				l.handleException(e);
			} finally {
				l.setTop(0);
			}
		}
	}

	// TODO: PlayerSleepInBedEvent

	// TODO: PlayerUseItemEvent.Start / Stop / Finish / Tick

	// TODO: UseHoeEvent
}
