package pl.vertty.fastbreak.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPickaxeDiamond;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.potion.Effect;
import pl.vertty.fastbreak.managers.UserManager;
import pl.vertty.fastbreak.objects.Cooldown;
import pl.vertty.fastbreak.objects.User;
import pl.vertty.fastbreak.utils.ChatUtilities;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpeedmineModule implements Listener
{
    private final Map<String, Long> changes = new ConcurrentHashMap<>();

    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            this.changes.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(final BlockBreakEvent e) {
        if (e.isFastBreak()) {
            this.handleBreak(e);
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        this.handleBreak(e);
    }

    private void handleBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (p.getGamemode() == 0) {
            final Item item = e.getItem();
            final Enchantment eff = item.getEnchantment(15);
            if (!this.changes.containsKey(p.getName())) {
                if (!Cooldown.getInstance().has(e.getPlayer(), "speedmine2")) {
                    final User u = UserManager.getUser(e.getPlayer());
                    if (u != null) {
                        if (u.speedmineLimit((eff == null || eff.getLevel() < 6) && (!p.hasEffect(3) || p.getEffect(3) == null || p.getEffect(3).getAmplifier() < 1))) {
                            e.setCancelled();
                            ChatUtilities.sendMessageToAdmins(Server.getInstance().getConfig().getString("notify.alert").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "Speedmine - A (Banned)"));
                            Cooldown.getInstance().add(e.getPlayer(), "speedmine2", 10.0f);
                            if(Server.getInstance().getConfig().getBoolean("bans.status")){
                                Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), Server.getInstance().getConfig().getString("bans.ban-command"));
                            }
                        }
                        else if (!Cooldown.getInstance().has(e.getPlayer(), "speedmine")) {
                            ChatUtilities.sendMessageToAdmins(Server.getInstance().getConfig().getString("notify.alert").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "Speedmine - A " + (e.getPlayer().hasEffect(3) ? "HASTE" : "") + " Eff: " + (item.isNull() ? "0" : (item.hasEnchantment(15) ? Integer.valueOf(item.getEnchantment(15).getLevel()) : "0"))));
                            Cooldown.getInstance().add(e.getPlayer(), "speedmine", ((eff != null && eff.getLevel() >= 6) || p.hasEffect(3)) ? 6.0f : 3.0f);
                        }
                    }
                    else {
                        e.setCancelled();
                    }
                }
                else {
                    e.setCancelled();
                }
                return;
            }
            double expectedTime = Math.ceil(b.getBreakTime(item) * 20.0);
            if (p.hasEffect(4)) {
                expectedTime *= 1.0 + 0.5 * p.getEffect(4).getAmplifier();
            }
            if (eff != null && eff.getLevel() >= 6) {
                expectedTime = 0.0;
            }
            else if (p.hasEffect(3)) {
                final Effect haste = p.getEffect(3);
                if (haste != null) {
                    if (haste.getAmplifier() >= 1) {
                        if (!item.isNull() && item.isPickaxe() && item instanceof ItemPickaxeDiamond) {
                            if (eff != null) {
                                if (eff.getLevel() >= 4) {
                                    expectedTime = 0.0;
                                }
                                else {
                                    expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                                }
                            }
                            else {
                                expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                            }
                        }
                        else {
                            expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                        }
                    }
                    else {
                        expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                    }
                }
            }
            --expectedTime;
            this.changes.remove(p.getName());
        }
    }
}

