package net.joshb.deathmessages.listener;

import net.joshb.deathmessages.api.EntityManager;
import net.joshb.deathmessages.api.PlayerManager;
import net.joshb.deathmessages.config.EntityDeathMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;

public class EntityDamage implements Listener {


    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player && Bukkit.getOnlinePlayers().contains(e.getEntity())) {
            Player p = (Player) e.getEntity();
            PlayerManager pm = PlayerManager.getPlayer(p);
            pm.setLastDamageCause(e.getCause());
            // for fall large if ppl want it float dist = e.getEntity().getFallDistance();
        } else if (!(e.getEntity() instanceof Player)){
            if(EntityDeathMessages.getInstance().getConfig().getConfigurationSection("Entities") == null) return;
            Set<String> listenedMobs = EntityDeathMessages.getInstance().getConfig().getConfigurationSection("Entities")
                    .getKeys(false);
            if(listenedMobs.isEmpty()) return;
            for (String listened : listenedMobs) {
                if(listened.contains(e.getEntity().getType().getEntityClass().getSimpleName().toLowerCase())){
                    EntityManager em;
                    if(EntityManager.getEntity(e.getEntity().getUniqueId()) == null){
                        em = new EntityManager(e.getEntity(), e.getEntity().getUniqueId());
                    } else {
                        em = EntityManager.getEntity(e.getEntity().getUniqueId());
                    }
                    em.setLastDamageCause(e.getCause());
                }
            }
        }
    }

}