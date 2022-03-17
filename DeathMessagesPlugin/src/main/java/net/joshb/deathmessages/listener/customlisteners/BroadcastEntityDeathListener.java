package net.joshb.deathmessages.listener.customlisteners;

import com.sk89q.worldguard.protection.flags.StateFlag;
import net.joshb.deathmessages.DeathMessages;
import net.joshb.deathmessages.api.EntityManager;
import net.joshb.deathmessages.api.PlayerManager;
import net.joshb.deathmessages.api.events.BroadcastEntityDeathMessageEvent;
import net.joshb.deathmessages.assets.Assets;
import net.joshb.deathmessages.config.Messages;
import net.joshb.deathmessages.config.Settings;
import net.joshb.deathmessages.listener.PluginMessaging;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.regex.Matcher;

public class BroadcastEntityDeathListener implements Listener {

    @EventHandler
    public void broadcastListener(BroadcastEntityDeathMessageEvent e) {
        PlayerManager pm = e.getPlayer();
        boolean hasOwner = false;
        if(e.getEntity() instanceof Tameable){
            Tameable tameable = (Tameable) e.getEntity();
            if(tameable.getOwner() != null) hasOwner = true;
        }
        if (!e.isCancelled()) {
            if (Messages.getInstance().getConfig().getBoolean("Console.Enabled")) {
                String message = Assets.entityDeathPlaceholders(Messages.getInstance().getConfig().getString("Console.Message"), pm.getPlayer(), e.getEntity(), hasOwner);
                message = message.replaceAll("%message%", Matcher.quoteReplacement(Assets.legacySerializer.serialize(e.getTextComponent())));
                Bukkit.getConsoleSender().sendMessage(message);
            }
            if(pm.isInCooldown()){
                return;
            } else {
                pm.setCooldown();
            }

            boolean privateTameable = Settings.getInstance().getConfig().getBoolean("Private-Messages.Entity");

            for(World w : e.getBroadcastedWorlds()){
                for(Player pls : w.getPlayers()){
                    if(Settings.getInstance().getConfig().getStringList("Disabled-Worlds").contains(w.getName())){
                        continue;
                    }
                    PlayerManager pms = PlayerManager.getPlayer(pls);
                    if(privateTameable && pms.getUUID().equals(pm.getPlayer().getUniqueId())){
                        if (pms.getMessagesEnabled()) {
                            pls.sendMessage(e.getTextComponent());
                        }
                    } else {
                        if (pms.getMessagesEnabled()) {
                            if(DeathMessages.worldGuardExtension != null){
                                if(DeathMessages.worldGuardExtension.getRegionState(pls, e.getMessageType().getValue()).equals(StateFlag.State.DENY)){
                                    return;
                                }
                            }
                            pls.sendMessage(e.getTextComponent());
                            PluginMessaging.sendPluginMSG(pms.getPlayer(), e.getTextComponent().toString());
                        }
                    }
                }
            }
            PluginMessaging.sendPluginMSG(e.getPlayer().getPlayer(), Assets.legacySerializer.serialize(e.getTextComponent()));
        }
        EntityManager.getEntity(e.getEntity().getUniqueId()).destroy();
    }
}
