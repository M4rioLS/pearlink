# Pearlink

**A Fabric mod for Minecraft 1.21.11 that turns any item in your inventory into a teleport destination.**

Tired of building nether highways, digging rail tunnels, or memorizing coordinates just to get across your base? Pearlink replaces all of it with a single block and a dead-simple idea: **two Pearlink Blocks holding the same item become linked.** Step on one, arrive at the other.

---

## What You Can Do

### Build a teleporter network out of whatever you have lying around
Drop a Pearlink Block at your home, another at your mine, and "key" both of them with the same item — a wheat seed, a stick, a specific sword, anything. The moment both blocks share a key, they form a link. Walk on one, instantly land on the other.

Want more routes? Key another pair with something else. Your hotbar becomes your dial-in menu: each item is a different destination.

### Instant, silent, low-ceremony travel
- **Step-on activation** — no right-clicks, no fuel, no charging up. Just walk over it.
- **Ender-pearl sound** and swirling **portal particles** for that classic teleport feel.
- **5-second cooldown** per player — prevents bouncing loops, still keeps travel snappy.
- **Safety check** — Pearlink refuses to drop you into solid blocks. If the exit is obstructed, you get a heads-up message instead of a suffocation death.

### Land softly — forgiving destination rules
The space above the destination block can be almost anything non-solid: air, water, tall grass, carpets, rails, signs, banners, torches, pressure plates, flowers, crops, ladders, kelp, vines — the full list is generous. Build around your teleporter without breaking it.

### A clean, familiar interface
Right-click a Pearlink Block to open a one-slot GUI. Drop in the item you want to use as the "key." Shift-click works exactly like you'd expect, and the slot is capped at a single item — no hoarding, no ambiguity.

### Automation-friendly
Pearlink Blocks expose their key slot to **hoppers and item pipes** on every side. Swap keys automatically, rotate destinations with a redstone clock, or build a selector out of hoppers and comparators. The slot is capped at one item to keep automated setups predictable.

### Lockable
Pearlink Blocks support a `locked` state — lock one down and the GUI refuses to open, with a sound cue and a message. Great for shared servers where you don't want visitors rekeying your network.

### Earn your travel card
First trip through a Pearlink earns you the **Slipgate Surfer** advancement. A small thing, but satisfying.

---

## Crafting

Shaped recipe, found under the **Functional Blocks** creative tab:

```
O D O
D E D
O D O
```

- `O` — Obsidian (×4)
- `D` — Amethyst Shard (×4)
- `E` — Ender Pearl (×1)

A single craft yields one Pearlink Block. Mid-game accessible — you need a nether trip for the pearl and obsidian, and a geode for the shards.

---

## Tips and Tricks

- **One-way stations.** Key a "sender" block and a matching "receiver" — no obstruction above the sender means one-way-only travel by simply not placing a return block.
- **Item-dial hotbar.** Keep a row of keyed items in your hotbar. Swap one into a portable Pearlink Block on the fly for a rerouteable network.
- **Hopper selector.** Feed different keys through a hopper filter to swap where your teleporter goes mid-run — useful for hub bases with many outbound links.
- **Don't lose the key.** Breaking a Pearlink Block drops whatever was inside, so you never lose a rare "address" item.

---

## Compatibility

- **Minecraft:** 1.21.11
- **Loader:** Fabric (requires Fabric API)
- **Java:** 21+
- **Environment:** Client + Server

---

## Links

- Homepage: https://m4riols.github.io/
- Source: https://github.com/M4rioLS/pearlink
- License: MIT — see [LICENSE](LICENSE).

Built by **M4rioLS**. Feedback and PRs welcome.
