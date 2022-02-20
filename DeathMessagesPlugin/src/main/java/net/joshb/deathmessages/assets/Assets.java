package net.joshb.deathmessages.assets;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.joshb.deathmessages.DeathMessages;
import net.joshb.deathmessages.api.EntityManager;
import net.joshb.deathmessages.api.ExplosionManager;
import net.joshb.deathmessages.api.PlayerManager;
import net.joshb.deathmessages.config.EntityDeathMessages;
import net.joshb.deathmessages.config.Messages;
import net.joshb.deathmessages.config.PlayerDeathMessages;
import net.joshb.deathmessages.config.Settings;
import net.joshb.deathmessages.enums.DeathAffiliation;
import net.joshb.deathmessages.enums.PDMode;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assets {

    static boolean addPrefix = Settings.getInstance().getConfig().getBoolean("Add-Prefix-To-All-Messages");
    public static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    public static final PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();

    public static List<String> damageTypes = Arrays.asList(
            "Bed",
            "Respawn-Anchor",
            "Projectile-Arrow",
            "Projectile-Dragon-Fireball",
            "Projectile-Egg",
            "Projectile-EnderPearl",
            "Projectile-Fireball",
            "Projectile-FishHook",
            "Projectile-LlamaSpit",
            "Projectile-Snowball",
            "Projectile-Trident",
            "Projectile-WitherSkull",
            "Projectile-ShulkerBullet",
            "Contact",
            "Melee",
            "Suffocation",
            "Fall",
            "Climbable",
            "Fire",
            "Fire-Tick",
            "Melting",
            "Lava",
            "Drowning",
            "Explosion",
            "Tnt",
            "Firework",
            "End-Crystal",
            "Void",
            "Lightning",
            "Suicide",
            "Starvation",
            "Poison",
            "Magic",
            "Wither",
            "Falling-Block",
            "Dragon-Breath",
            "Custom",
            "Fly-Into-Wall",
            "Hot-Floor",
            "Cramming",
            "Dryout",
            "Unknown");

    public static boolean isNumeric(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public static HashMap<String, String> addingMessage = new HashMap<>();

    public static Component formatMessage(String path) {
        return legacySerializer.deserialize(Messages.getInstance().getConfig().getString(path)
                .replaceAll("%prefix%", Messages.getInstance().getConfig().getString("Prefix")));
    }

    public static String formatString(String string) {
        return string
                .replaceAll("%prefix%", Messages.getInstance().getConfig().getString("Prefix"));
    }

    public static List<String> formatMessage(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            newList.add(s
                    .replaceAll("%prefix%", Messages.getInstance().getConfig().getString("Prefix")));
        }
        return newList;
    }

    public static boolean isClimable(Block b) {
        return b.getType().name().contains("LADDER")
                || b.getType().name().contains("VINE")
                || b.getType().equals(Material.SCAFFOLDING)
                || b.getType().name().contains("TRAPDOOR");
    }

    public static boolean itemNameIsWeapon(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        String displayName = itemStack.getItemMeta().getDisplayName();

        for (String s : Settings.getInstance().getConfig().getStringList("Custom-Item-Display-Names-Is-Weapon")) {
            Pattern pattern = Pattern.compile(s);
            Matcher matcher = pattern.matcher(displayName);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean itemMaterialIsWeapon(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        for (String s : Settings.getInstance().getConfig().getStringList("Custom-Item-Material-Is-Weapon")) {
            Material mat = Material.getMaterial(s);
            if (mat == null) {
                return false;
            }
            if (itemStack.getType().equals(mat)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeapon(ItemStack itemStack) {
        return !itemStack.getType().toString().contains("SHOVEL")
                && !itemStack.getType().toString().contains("PICKAXE")
                && !itemStack.getType().toString().contains("AXE")
                && !itemStack.getType().toString().contains("HOE")
                && !itemStack.getType().toString().contains("SWORD")
                && !itemStack.getType().toString().contains("BOW")
                && !itemNameIsWeapon(itemStack)
                && !itemMaterialIsWeapon(itemStack);
    }

    public static boolean hasWeapon(LivingEntity mob, EntityDamageEvent.DamageCause damageCause) {
        if (mob.getEquipment() == null || mob.getEquipment().getItemInMainHand() == null) {
            return false;
        } else if (isWeapon(mob.getEquipment().getItemInMainHand())) {
            return false;
        } else if (damageCause.equals(EntityDamageEvent.DamageCause.THORNS)) {
            return false;
        } else {
            return true;
        }
    }

    public static Component playerDeathMessage(PlayerManager pm, boolean gang) {
        LivingEntity mob = (LivingEntity) pm.getLastEntityDamager();
        boolean hasWeapon = hasWeapon(mob, pm.getLastDamage());

        if (pm.getLastDamage().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (pm.getLastExplosiveEntity() instanceof EnderCrystal) {
                return get(gang, pm, mob, "End-Crystal");
            } else if (pm.getLastExplosiveEntity() instanceof TNTPrimed) {
                return get(gang, pm, mob, "TNT");
            } else if (pm.getLastExplosiveEntity() instanceof Firework) {
                return get(gang, pm, mob, "Firework");
            } else {
                return get(gang, pm, mob, getSimpleCause(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION));
            }
        }
        if (pm.getLastDamage().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            //Bed kill

            ExplosionManager explosionManager = ExplosionManager.getManagerIfEffected(pm.getUUID());
            if (explosionManager.getMaterial().name().contains("BED")) {
                PlayerManager pyro = PlayerManager.getPlayer(explosionManager.getPyro());
                return get(gang, pm, pyro.getPlayer(), "Bed");
            }
            //Respawn Anchor kill
            if (explosionManager.getMaterial().equals(Material.RESPAWN_ANCHOR)) {
                PlayerManager pyro = PlayerManager.getPlayer(explosionManager.getPyro());
                return get(gang, pm, pyro.getPlayer(), "Respawn-Anchor");
            }
        }
        if (hasWeapon) {
            if (pm.getLastDamage().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                return getWeapon(gang, pm, mob);
            } else if (pm.getLastDamage().equals(EntityDamageEvent.DamageCause.PROJECTILE)
                    && pm.getLastProjectileEntity() instanceof Arrow) {
                return getProjectile(gang, pm, mob, getSimpleProjectile(pm.getLastProjectileEntity()));
            } else {
                return get(gang, pm, mob, getSimpleCause(EntityDamageEvent.DamageCause.ENTITY_ATTACK));
            }
        } else {
            for (EntityDamageEvent.DamageCause dc : EntityDamageEvent.DamageCause.values()) {
                if (pm.getLastDamage().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    return getProjectile(gang, pm, mob, getSimpleProjectile(pm.getLastProjectileEntity()));
                }
                if (pm.getLastDamage().equals(dc)) {
                    return get(gang, pm, mob, getSimpleCause(dc));
                }
            }
            return null;
        }
    }

    public static Component entityDeathMessage(EntityManager em) {
        PlayerManager pm = em.getLastPlayerDamager();
        Player p = pm.getPlayer();
        boolean hasWeapon = hasWeapon(p, pm.getLastDamage());

        if (em.getLastDamage().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (em.getLastExplosiveEntity() instanceof EnderCrystal) {
                return getEntityDeath(p, em.getEntity(), "End-Crystal");
            } else if (em.getLastExplosiveEntity() instanceof TNTPrimed) {
                return getEntityDeath(p, em.getEntity(), "TNT");
            } else if (em.getLastExplosiveEntity() instanceof Firework) {
                return getEntityDeath(p, em.getEntity(), "Firework");
            } else {
                return getEntityDeath(p, em.getEntity(), getSimpleCause(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION));
            }
        }
        if (em.getLastDamage().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            //Bed kill
            ExplosionManager explosionManager = ExplosionManager.getManagerIfEffected(em.getEntityUUID());
            if (explosionManager.getMaterial().name().contains("BED")) {
                PlayerManager pyro = PlayerManager.getPlayer(explosionManager.getPyro());
                return getEntityDeath(pyro.getPlayer(), em.getEntity(), "Bed");
            }
            //Respawn Anchor kill
            if (explosionManager.getMaterial().equals(Material.RESPAWN_ANCHOR)) {
                PlayerManager pyro = PlayerManager.getPlayer(explosionManager.getPyro());
                return getEntityDeath(pyro.getPlayer(), em.getEntity(), "Respawn-Anchor");
            }
        }
        if (hasWeapon) {
            if (em.getLastDamage().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                return getEntityDeathWeapon(p, em.getEntity());
            } else if (em.getLastDamage().equals(EntityDamageEvent.DamageCause.PROJECTILE)
                    && em.getLastProjectileEntity() instanceof Arrow) {
                return getEntityDeathProjectile(p, em, getSimpleProjectile(em.getLastProjectileEntity()));
            } else {
                return getEntityDeathWeapon(p, em.getEntity());
            }
        } else {
            for (EntityDamageEvent.DamageCause dc : EntityDamageEvent.DamageCause.values()) {
                if (em.getLastDamage().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    return getEntityDeathProjectile(p, em, getSimpleProjectile(em.getLastProjectileEntity()));
                }
                if (em.getLastDamage().equals(dc)) {
                    return getEntityDeath(p, em.getEntity(), getSimpleCause(dc));
                }
            }
            return null;
        }
    }


    public static Component getNaturalDeath(PlayerManager pm, String damageCause) {
        Random random = new Random();
        List<String> msgs = sortList(getPlayerDeathMessages().getStringList("Natural-Cause." + damageCause), pm.getPlayer(), pm.getPlayer());
        if (msgs.isEmpty()) return null;
        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }

        if (firstSection.contains("%block%") && pm.getLastEntityDamager() instanceof FallingBlock) {
            try {
                FallingBlock fb = (FallingBlock) pm.getLastEntityDamager();
                String material = XMaterial.matchXMaterial(fb.getBlockData().getMaterial()).parseMaterial().toString().toLowerCase();
                String configValue = Messages.getInstance().getConfig().getString("Blocks." + material);
                firstSection = firstSection.replaceAll("%block%", configValue);
            } catch (NullPointerException e) {
                DeathMessages.plugin.getLogger().log(Level.SEVERE, "Could not parse %block%. Please check your config for a wrong value." +
                        " Your materials could be spelt wrong or it does not exists in the config. If this problem persist, contact support" +
                        " on the discord https://discord.gg/K9zVDwt");
                pm.setLastEntityDamager(null);
                return getNaturalDeath(pm, getSimpleCause(EntityDamageEvent.DamageCause.SUFFOCATION));
            }

        } else if (firstSection.contains("%climbable%") && pm.getLastDamage().equals(EntityDamageEvent.DamageCause.FALL)) {
            try {
                String material = XMaterial.matchXMaterial(pm.getLastClimbing()).parseMaterial().toString().toLowerCase();
                String configValue = Messages.getInstance().getConfig().getString("Blocks." + material);
                firstSection = firstSection.replaceAll("%climbable%", configValue);
            } catch (NullPointerException e) {
                DeathMessages.plugin.getLogger().log(Level.SEVERE, "Could not parse %climbable%. Please check your config for a wrong value." +
                        " Your materials could be spelt wrong or it does not exists in the config. If this problem persist, contact support" +
                        " on the discord https://discord.gg/K9zVDwt - Parsed block: "+ pm.getLastClimbing().toString());
                pm.setLastClimbing(null);
                return getNaturalDeath(pm, getSimpleCause(EntityDamageEvent.DamageCause.FALL));
            }
        }

        Component tx = legacySerializer.deserialize(playerDeathPlaceholders(firstSection, pm, null) + " ");

        if (pm.getLastDamage().equals(EntityDamageEvent.DamageCause.PROJECTILE) && firstSection.contains("%weapon%")) {
            ItemStack i = pm.getPlayer().getEquipment().getItemInMainHand();
            if (!i.getType().equals(Material.BOW)) {
                return getNaturalDeath(pm, "Projectile-Unknown");
            }
            Component displayName;
            if (i.getItemMeta().hasDisplayName() && i.getItemMeta().displayName() != null) {
                displayName = i.getItemMeta().displayName();
            } else {
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Allow-Message-Color-Override")) {

                }
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Enabled")) {
                    if (!Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Ignore-Enchantments")) {
                        if (i.getEnchantments().size() == 0) {
                            return getNaturalDeath(pm, "Projectile-Unknown");
                        }
                    } else {
                        return getNaturalDeath(pm, "Projectile-Unknown");
                    }
                }
                displayName = plainSerializer.deserialize(Assets.convertString(i.getType().name()));
            }

            Component weaponComp = displayName.hoverEvent(HoverEvent.showItem(i.getType().getKey(), 1, BinaryTagHolder.of(
                    NBTItem.convertItemtoNBT(i).getCompound().toString())));
            tx = tx.replaceText(TextReplacementConfig.builder().matchLiteral("%weapon%").replacement(
                    weaponComp).build());
        }

        tc.append(tx);

        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(legacySerializer.deserialize(playerDeathPlaceholders(sec[1], pm, null))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + playerDeathPlaceholders(cmd, pm, null)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + playerDeathPlaceholders(cmd, pm, null)));
            }
        }
        return tc.build();
    }

    public static Component getWeapon(boolean gang, PlayerManager pm, LivingEntity mob) {
        Random random = new Random();

        final boolean basicMode = PlayerDeathMessages.getInstance().getConfig().getBoolean("Basic-Mode.Enabled");
        final String cMode = basicMode ? PDMode.BASIC_MODE.getValue() : PDMode.MOBS.getValue()
                + "." + mob.getType().getEntityClass().getSimpleName().toLowerCase();
        final String affiliation = gang ? DeathAffiliation.GANG.getValue() : DeathAffiliation.SOLO.getValue();
        //List<String> msgs = sortList(getPlayerDeathMessages().getStringList(cMode + "." + affiliation + ".Weapon"), pm.getPlayer());

        List<String> msgs = sortList(getPlayerDeathMessages().getStringList(cMode + "." + affiliation + ".Weapon"), pm.getPlayer(), mob);

        if (msgs.isEmpty()) return null;
        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }

        Component tx = legacySerializer.deserialize(playerDeathPlaceholders(firstSection, pm, mob) + " ");

        if (firstSection.contains("%weapon%")) {
            ItemStack i = mob.getEquipment().getItemInMainHand();
            Component displayName;
            if (i.getItemMeta().hasDisplayName() && i.getItemMeta().displayName() != null) {
                displayName = i.getItemMeta().displayName();
            } else {
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Allow-Message-Color-Override")) {

                }
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Enabled")) {
                    if (!Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Ignore-Enchantments")) {
                        if (i.getEnchantments().size() == 0) {
                            return get(gang, pm, mob, Settings.getInstance().getConfig()
                                    .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Weapon.Default-To"));
                        }
                    } else {
                        return get(gang, pm, mob, Settings.getInstance().getConfig()
                                .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Weapon.Default-To"));
                    }
                }
                displayName = plainSerializer.deserialize(Assets.convertString(i.getType().name()));
            }

            Component weaponComp = displayName.hoverEvent(HoverEvent.showItem(i.getType().getKey(), 1, BinaryTagHolder.of(
                            NBTItem.convertItemtoNBT(i).getCompound().toString())));
            tx = tx.replaceText(TextReplacementConfig.builder().matchLiteral("%weapon%").replacement(
                    weaponComp).build());
        }

        tc.append(tx);

        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(legacySerializer.deserialize(playerDeathPlaceholders(sec[1], pm, mob))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + playerDeathPlaceholders(cmd, pm, mob)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + playerDeathPlaceholders(cmd, pm, mob)));
            }
        }
        return tc.build();
    }

    public static Component getEntityDeathWeapon(Player p, Entity e) {
        Random random = new Random();
        String entityName = e.getType().getEntityClass().getSimpleName().toLowerCase();
        List<String> msgs = sortList(getEntityDeathMessages().getStringList("Entities." + entityName + ".Weapon"), p, e);

        if (msgs.isEmpty()) return null;
        boolean hasOwner = false;
        if (e instanceof Tameable) {
            Tameable tameable = (Tameable) e;
            if (tameable.getOwner() != null) hasOwner = true;
        }

        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }

        Component tx = legacySerializer.deserialize(entityDeathPlaceholders(firstSection, p, e, hasOwner) + " ");

        if (firstSection.contains("%weapon%")) {
            ItemStack i = p.getEquipment().getItemInMainHand();
            Component displayName;
            if (i.getItemMeta().hasDisplayName() && i.getItemMeta().displayName() != null) {
                displayName = i.getItemMeta().displayName();
            } else {
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Allow-Message-Color-Override")) {

                }
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Enabled")) {
                    if (!Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Ignore-Enchantments")) {
                        if (i.getEnchantments().size() == 0) {
                            return getEntityDeath(p, e, Settings.getInstance().getConfig()
                                    .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Weapon.Default-To"));
                        }
                    } else {
                        return getEntityDeath(p, e, Settings.getInstance().getConfig()
                                .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Weapon.Default-To"));
                    }
                }
                displayName = plainSerializer.deserialize(Assets.convertString(i.getType().name()));
            }
            Component weaponComp = displayName.hoverEvent(HoverEvent.showItem(i.getType().getKey(), 1, BinaryTagHolder.of(
                            NBTItem.convertItemtoNBT(i).getCompound().toString())));
            tx = tx.replaceText(TextReplacementConfig.builder().matchLiteral("%weapon%").replacement(
                    weaponComp).build());
        }

        tc.append(tx);

        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(
                    legacySerializer.deserialize(entityDeathPlaceholders(sec[1], p, e, hasOwner))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + entityDeathPlaceholders(cmd, p, e, hasOwner)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + entityDeathPlaceholders(cmd, p, e, hasOwner)));
            }
        }
        return tc.build();
    }

    public static Component get(boolean gang, PlayerManager pm, LivingEntity mob, String damageCause) {
        Random random = new Random();

        final boolean basicMode = PlayerDeathMessages.getInstance().getConfig().getBoolean("Basic-Mode.Enabled");
        final String cMode = basicMode ? PDMode.BASIC_MODE.getValue() : PDMode.MOBS.getValue()
                + "." + mob.getType().getEntityClass().getSimpleName().toLowerCase();
        final String affiliation = gang ? DeathAffiliation.GANG.getValue() : DeathAffiliation.SOLO.getValue();

        // List<String> msgs = sortList(getPlayerDeathMessages().getStringList(cMode + "." + affiliation + "." + damageCause), pm.getPlayer());

        Bukkit.broadcastMessage("get");
        List<String> msgs = sortList(getPlayerDeathMessages().getStringList(cMode + "." + affiliation + "." + damageCause), pm.getPlayer(), mob);

        if (msgs.isEmpty()) {
            if (Settings.getInstance().getConfig().getBoolean("Default-Natural-Death-Not-Defined"))
                return getNaturalDeath(pm, damageCause);
            if (Settings.getInstance().getConfig().getBoolean("Default-Melee-Last-Damage-Not-Defined"))
                return get(gang, pm, mob, getSimpleCause(EntityDamageEvent.DamageCause.ENTITY_ATTACK));
            return null;
        }

        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }
        Component tx = legacySerializer.deserialize(playerDeathPlaceholders(firstSection, pm, mob) + " ");
        tc.append(tx);
        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(legacySerializer.deserialize(playerDeathPlaceholders(sec[1], pm, mob))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + playerDeathPlaceholders(cmd, pm, mob)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + playerDeathPlaceholders(cmd, pm, mob)));
            }
        }
        return tc.build();
    }

    public static Component getProjectile(boolean gang, PlayerManager pm, LivingEntity mob, String projectileDamage) {
        Random random = new Random();
        final boolean basicMode = PlayerDeathMessages.getInstance().getConfig().getBoolean("Basic-Mode.Enabled");
        final String cMode = basicMode ? PDMode.BASIC_MODE.getValue() : PDMode.MOBS.getValue()
                + "." + mob.getType().getEntityClass().getSimpleName().toLowerCase();
        final String affiliation = gang ? DeathAffiliation.GANG.getValue() : DeathAffiliation.SOLO.getValue();

      //  List<String> msgs = sortList(getPlayerDeathMessages().getStringList(cMode + "." + affiliation + "." + projectileDamage), pm.getPlayer());

        List<String> msgs = sortList(getPlayerDeathMessages().getStringList(cMode + "." + affiliation + "." + projectileDamage), pm.getPlayer(), mob);
        if (msgs.isEmpty()) {
            return null;
        }
        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }

        Component tx = legacySerializer.deserialize(playerDeathPlaceholders(firstSection, pm, mob) + " ");

        if (firstSection.contains("%weapon%") && pm.getLastProjectileEntity() instanceof Arrow) {
            ItemStack i = mob.getEquipment().getItemInMainHand();
            Component displayName;
            if (i.getItemMeta().hasDisplayName() && i.getItemMeta().displayName() != null) {
                displayName = i.getItemMeta().displayName();
            } else {
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Enabled")) {
                    if (!Settings.getInstance().getConfig()
                            .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Projectile.Default-To").equals(projectileDamage)) {
                        return getProjectile(gang, pm, mob, Settings.getInstance().getConfig()
                                .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Projectile.Default-To"));
                    }
                }
                displayName = plainSerializer.deserialize(Assets.convertString(i.getType().name()));
            }

            Component weaponComp = displayName.hoverEvent(HoverEvent.showItem(i.getType().getKey(), 1, BinaryTagHolder.of(
                            NBTItem.convertItemtoNBT(i).getCompound().toString())));
            tx = tx.replaceText(TextReplacementConfig.builder().matchLiteral("%weapon%").replacement(
                    weaponComp).build());
        }

        tc.append(tx);

        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(legacySerializer.deserialize(playerDeathPlaceholders(sec[1], pm, mob))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + playerDeathPlaceholders(cmd, pm, mob)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + playerDeathPlaceholders(cmd, pm, mob)));
            }
        }
        return tc.build();
    }

    public static Component getEntityDeathProjectile(Player p, EntityManager em, String projectileDamage) {
        Random random = new Random();
        String entityName = em.getEntity().getType().getEntityClass().getSimpleName().toLowerCase();
        List<String> msgs = sortList(getEntityDeathMessages().getStringList("Entities." + entityName + "." + projectileDamage), p, em.getEntity());
        if (msgs.isEmpty()) {
            if (Settings.getInstance().getConfig().getBoolean("Default-Melee-Last-Damage-Not-Defined")) {
                return getEntityDeath(p, em.getEntity(), getSimpleCause(EntityDamageEvent.DamageCause.ENTITY_ATTACK));
            }
            return null;
        }
        boolean hasOwner = false;
        if (em.getEntity() instanceof Tameable) {
            Tameable tameable = (Tameable) em.getEntity();
            if (tameable.getOwner() != null) hasOwner = true;
        }
        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }

        Component tx = legacySerializer.deserialize(entityDeathPlaceholders(firstSection, p, em.getEntity(), hasOwner) + " ");

        if (firstSection.contains("%weapon%") && em.getLastProjectileEntity() instanceof Arrow) {
            ItemStack i = p.getEquipment().getItemInMainHand();
            Component displayName;
            if (i.getItemMeta().hasDisplayName() && i.getItemMeta().displayName() != null) {
                displayName = i.getItemMeta().displayName();
            } else {
                if (Settings.getInstance().getConfig().getBoolean("Disable-Weapon-Kill-With-No-Custom-Name.Enabled")) {
                    if (!Settings.getInstance().getConfig()
                            .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Projectile.Default-To").equals(projectileDamage)) {
                        return getEntityDeathProjectile(p, em, Settings.getInstance().getConfig()
                                .getString("Disable-Weapon-Kill-With-No-Custom-Name.Source.Projectile.Default-To"));
                    }
                }
                displayName = plainSerializer.deserialize(Assets.convertString(i.getType().name()));
            }

            Component weaponComp = displayName.hoverEvent(
                    HoverEvent.showItem(i.getType().getKey(), 1, BinaryTagHolder.of(
                            NBTItem.convertItemtoNBT(i).getCompound().toString())));
            tx = tx.replaceText(TextReplacementConfig.builder().matchLiteral("%weapon%").replacement(
                    weaponComp).build());
            tc.append(weaponComp);
        }

        tc.append(tx);

        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(
                    legacySerializer.deserialize(entityDeathPlaceholders(sec[1], p, em.getEntity(), hasOwner))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + entityDeathPlaceholders(cmd, p, em.getEntity(), hasOwner)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + entityDeathPlaceholders(cmd, p, em.getEntity(), hasOwner)));
            }
        }
        return tc.build();
    }

    public static Component getEntityDeath(Player player, Entity entity, String damageCause) {
        Random random = new Random();
        boolean hasOwner = false;
        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable) entity;
            if (tameable.getOwner() != null) hasOwner = true;
        }
        List<String> msgs;
        if (hasOwner) {
            msgs = sortList(getEntityDeathMessages().getStringList("Entities." +
                    entity.getType().getEntityClass().getSimpleName().toLowerCase() + ".Tamed"), player, entity);
        } else {
            msgs = sortList(getEntityDeathMessages().getStringList("Entities." +
                        entity.getType().getEntityClass().getSimpleName().toLowerCase() + "." + damageCause), player, entity);
        }
        if (msgs.isEmpty()) return null;

        String msg = msgs.get(random.nextInt(msgs.size()));
        TextComponent.Builder tc = Component.text();
        if (addPrefix) {
            Component tx = legacySerializer.deserialize(Messages.getInstance().getConfig().getString("Prefix"));
            tc.append(tx);
        }
        String[] sec = msg.split("::");
        String firstSection;
        if (msg.contains("::")) {
            if (sec.length == 0) {
                firstSection = msg;
            } else {
                firstSection = sec[0];
            }
        } else {
            firstSection = msg;
        }

        Component tx = legacySerializer.deserialize(entityDeathPlaceholders(firstSection, player, entity, hasOwner) + " ");
        tc.append(tx);

        if (sec.length >= 2) {
            tc.hoverEvent(HoverEvent.showText(
                    legacySerializer.deserialize(entityDeathPlaceholders(sec[1], player, entity, hasOwner))));
        }
        if (sec.length == 3) {
            if (sec[2].startsWith("COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.runCommand("/" + entityDeathPlaceholders(cmd, player, entity, hasOwner)));
            } else if (sec[2].startsWith("SUGGEST_COMMAND:")) {
                String cmd = sec[2].split(":")[1];
                tc.clickEvent(ClickEvent.suggestCommand("/" + entityDeathPlaceholders(cmd, player, entity, hasOwner)));
            }
        }
        return tc.build();
    }

    public static List<String> sortList(List<String> list, Player player, Entity killer) {
        List<String> newList = list;
        List<String> returnList = new ArrayList<>();
        for (String s : list) {
            //Check for permission messages
            if (s.contains("PERMISSION[")) {
                Matcher m = Pattern.compile("PERMISSION\\[([^)]+)\\]").matcher(s);
                while (m.find()) {
                    String perm = m.group(1);
                    if (player.getPlayer().hasPermission(perm)) {
                        returnList.add(s.replace("PERMISSION[" + perm + "]", ""));
                    }
                }
            }
            if (s.contains("PERMISSION_KILLER[")) {
                Matcher m = Pattern.compile("PERMISSION_KILLER\\[([^)]+)\\]").matcher(s);
                while (m.find()) {
                    String perm = m.group(1);
                    if (killer.hasPermission(perm)) {
                        returnList.add(s.replace("PERMISSION_KILLER[" + perm + "]", ""));
                    }
                }
            }
            //Check for region specific messages
            if (s.contains("REGION[")) {
                Matcher m = Pattern.compile("REGION\\[([^)]+)\\]").matcher(s);
                while (m.find()) {
                    String regionID = m.group(1);
                    if (DeathMessages.worldGuardExtension == null) {
                        continue;
                    }
                    if (DeathMessages.worldGuardExtension.isInRegion(player.getPlayer(), regionID)) {
                        returnList.add(s.replace("REGION[" + regionID + "]", ""));
                    }
                }
            }
        }
        if (!returnList.isEmpty()) {
            newList = returnList;
        } else {
            newList.removeIf(s -> s.contains("PERMISSION[") || s.contains("REGION[") || s.contains("PERMISSION_KILLER["));
        }
        return newList;
    }

    public static String entityDeathPlaceholders(String msg, Player player, Entity entity, boolean owner) {
        msg = msg
                .replaceAll("%entity%", Messages.getInstance().getConfig().getString("Mobs."
                        + entity.getType().toString().toLowerCase()))
                .replaceAll("%entity_display%", entity.getCustomName() == null ? Messages.getInstance().getConfig().getString("Mobs."
                        + entity.getType().toString().toLowerCase()): entity.getCustomName())
                .replaceAll("%killer%", player.getName())
                .replaceAll("%killer_display%", player.getDisplayName())
                .replaceAll("%world%", entity.getLocation().getWorld().getName())
                .replaceAll("%world_environment%", getEnvironment(entity.getLocation().getWorld().getEnvironment()))
                .replaceAll("%x%", String.valueOf(entity.getLocation().getBlock().getX()))
                .replaceAll("%y%", String.valueOf(entity.getLocation().getBlock().getY()))
                .replaceAll("%z%", String.valueOf(entity.getLocation().getBlock().getZ()));
        if (owner) {
            if (entity instanceof Tameable) {
                Tameable tameable = (Tameable) entity;
                if (tameable.getOwner() != null && tameable.getOwner().getName() != null) {
                    msg = msg.replaceAll("%owner%", tameable.getOwner().getName());
                }
            }
        }
        try {
            msg = msg.replaceAll("%biome%", entity.getLocation().getBlock().getBiome().name());
        } catch (NullPointerException e) {
            DeathMessages.plugin.getLogger().log(Level.SEVERE, "Custom Biome detected. Using 'Unknown' for a biome name.");
            DeathMessages.plugin.getLogger().log(Level.SEVERE, "Custom Biomes are not supported yet.'");
            msg = msg.replaceAll("%biome%", "Unknown");
        }
        if (DeathMessages.plugin.placeholderAPIEnabled) {
            msg = PlaceholderAPI.setPlaceholders(player.getPlayer(), msg);
        }
        return msg;
    }

    public static String playerDeathPlaceholders(String msg, PlayerManager pm, LivingEntity mob) {
        if (mob == null) {
            msg = msg
                    .replaceAll("%player%", pm.getName())
                    .replaceAll("%player_display%", pm.getPlayer().getDisplayName())
                    .replaceAll("%world%", pm.getLastLocation().getWorld().getName())
                    .replaceAll("%world_environment%", getEnvironment(pm.getLastLocation().getWorld().getEnvironment()))
                    .replaceAll("%x%", String.valueOf(pm.getLastLocation().getBlock().getX()))
                    .replaceAll("%y%", String.valueOf(pm.getLastLocation().getBlock().getY()))
                    .replaceAll("%z%", String.valueOf(pm.getLastLocation().getBlock().getZ()));
            try {
                msg = msg.replaceAll("%biome%", pm.getLastLocation().getBlock().getBiome().name());
            } catch (NullPointerException e) {
                DeathMessages.plugin.getLogger().log(Level.SEVERE, "Custom Biome detected. Using 'Unknown' for a biome name.");
                DeathMessages.plugin.getLogger().log(Level.SEVERE, "Custom Biomes are not supported yet.'");
                msg = msg.replaceAll("%biome%", "Unknown");
            }
            if (DeathMessages.plugin.placeholderAPIEnabled) {
                msg = PlaceholderAPI.setPlaceholders(pm.getPlayer(), msg);
            }
            return msg;
        } else {
            String mobName = mob.getName();
            if (Settings.getInstance().getConfig().getBoolean("Rename-Mobs.Enabled")) {
                String[] chars = Settings.getInstance().getConfig().getString("Rename-Mobs.If-Contains").split("(?!^)");
                for (String ch : chars) {
                    if (mobName.contains(ch)) {
                        mobName = Messages.getInstance().getConfig().getString("Mobs." + mob.getType().toString().toLowerCase());
                        break;
                    }
                }
            }
            if (!(mob instanceof Player) && Settings.getInstance().getConfig().getBoolean("Disable-Named-Mobs")) {
                mobName = Messages.getInstance().getConfig().getString("Mobs." + mob.getType().toString().toLowerCase());
            }
            msg = msg
                    .replaceAll("%player%", pm.getName())
                    .replaceAll("%player_display%", pm.getPlayer().getDisplayName())
                    .replaceAll("%killer%", mobName)
                    .replaceAll("%killer_type%", Messages.getInstance().getConfig().getString("Mobs."
                            + mob.getType().toString().toLowerCase()))
                    .replaceAll("%world%", pm.getLastLocation().getWorld().getName())
                    .replaceAll("%world_environment%", getEnvironment(pm.getLastLocation().getWorld().getEnvironment()))
                    .replaceAll("%x%", String.valueOf(pm.getLastLocation().getBlock().getX()))
                    .replaceAll("%y%", String.valueOf(pm.getLastLocation().getBlock().getY()))
                    .replaceAll("%z%", String.valueOf(pm.getLastLocation().getBlock().getZ()));
            try {
                msg = msg.replaceAll("%biome%", pm.getLastLocation().getBlock().getBiome().name());
            } catch (NullPointerException e) {
                DeathMessages.plugin.getLogger().log(Level.SEVERE, "Custom Biome detected. Using 'Unknown' for a biome name.");
                DeathMessages.plugin.getLogger().log(Level.SEVERE, "Custom Biomes are not supported yet.'");
                msg = msg.replaceAll("%biome%", "Unknown");
            }

            if (mob instanceof Player) {
                Player p = (Player) mob;
                msg = msg.replaceAll("%killer_display%", p.getDisplayName());
            }
            if (DeathMessages.plugin.placeholderAPIEnabled) {
                msg = PlaceholderAPI.setPlaceholders(pm.getPlayer(), msg);
            }
            return msg;
        }
    }

    public static String convertString(String string) {
        string = string.replaceAll("_", " ").toLowerCase();
        String[] spl = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spl.length; i++) {
            if (i == spl.length - 1) {
                sb.append(StringUtils.capitalize(spl[i]));
            } else {
                sb.append(StringUtils.capitalize(spl[i]) + " ");
            }
        }
        return sb.toString();
    }

    public static String getEnvironment(World.Environment environment) {
        switch (environment) {
            case NORMAL:
                return Messages.getInstance().getConfig().getString("Environment.normal");
            case NETHER:
                return Messages.getInstance().getConfig().getString("Environment.nether");
            case THE_END:
                return Messages.getInstance().getConfig().getString("Environment.the_end");
            default:
                return Messages.getInstance().getConfig().getString("Environment.unknown");
        }
    }

    public static String getSimpleProjectile(Projectile projectile) {
        if (projectile instanceof Arrow) {
            return "Projectile-Arrow";
        } else if (projectile instanceof DragonFireball) {
            return "Projectile-Dragon-Fireball";
        } else if (projectile instanceof Egg) {
            return "Projectile-Egg";
        } else if (projectile instanceof EnderPearl) {
            return "Projectile-EnderPearl";
        } else if (projectile instanceof Fireball) {
            return "Projectile-Fireball";
        } else if (projectile instanceof FishHook) {
            return "Projectile-FishHook";
        } else if (projectile instanceof LlamaSpit) {
            return "Projectile-LlamaSpit";
        } else if (projectile instanceof Snowball) {
            return "Projectile-Snowball";
        } else if (projectile instanceof Trident) {
            return "Projectile-Trident";
        } else if (projectile instanceof WitherSkull) {
            return "Projectile-WitherSkull";
        } else if (projectile instanceof ShulkerBullet) {
            return "Projectile-ShulkerBullet";
        } else {
            return "Projectile-Arrow";
        }
    }

    public static String getSimpleCause(EntityDamageEvent.DamageCause damageCause) {
        switch (damageCause) {
            case CONTACT:
                return "Contact";
            case ENTITY_ATTACK:
                return "Melee";
            case PROJECTILE:
                return "Projectile";
            case SUFFOCATION:
                return "Suffocation";
            case FALL:
                return "Fall";
            case FIRE:
                return "Fire";
            case FIRE_TICK:
                return "Fire-Tick";
            case MELTING:
                return "Melting";
            case LAVA:
                return "Lava";
            case DROWNING:
                return "Drowning";
            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
                return "Explosion";
            case VOID:
                return "Void";
            case LIGHTNING:
                return "Lightning";
            case SUICIDE:
                return "Suicide";
            case STARVATION:
                return "Starvation";
            case POISON:
                return "Poison";
            case MAGIC:
                return "Magic";
            case WITHER:
                return "Wither";
            case FALLING_BLOCK:
                return "Falling-Block";
            case THORNS:
                return "Thorns";
            case DRAGON_BREATH:
                return "Dragon-Breath";
            case CUSTOM:
                return "Custom";
            case FLY_INTO_WALL:
                return "Fly-Into-Wall";
            case HOT_FLOOR:
                return "Hot-Floor";
            case CRAMMING:
                return "Cramming";
            case DRYOUT:
                return "Dryout";
            case FREEZE:
                return "Freeze";
            default:
                return "Unknown";
        }
    }

    public static FileConfiguration getPlayerDeathMessages() {
        return PlayerDeathMessages.getInstance().getConfig();
    }

    public static FileConfiguration getEntityDeathMessages() {
        return EntityDeathMessages.getInstance().getConfig();
    }
}
