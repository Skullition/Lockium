package dev.skullition.lockium.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.skullition.lockium.deserializer.ItemPropertyDeserializer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** DTO to store internal Sinister item response. */
public record GrowtopiaItem(
    int id,
    @NotNull String name,
    @Nullable String description,
    @JsonDeserialize(using = ItemPropertyDeserializer.class) EnumSet<ItemProperty> properties,
    @NotNull ItemType type,
    int health,
    int secondsToHeal,
    int rarity,
    @Nullable ItemClothingType clothingType,
    @Nullable String releaseDateInfo,
    @Nullable ItemEffect itemEffect,
    @Nullable PetBattleAbility pbAbility,
    @Nullable String extraNote,
    @NotNull String wikiItemSprite,
    @NotNull String wikiSeedSprite,
    int baseColor,
    @Nullable StoreItem storeItem) {
  /** Enum to store item flags. */
  public enum ItemProperty {
    MULTI_FACING(0x01),
    WRENCHABLE(0x02),
    NO_SEED(0x04),
    PERMANENT(0x08),
    NO_DROP(0x10),
    NO_SELF(0x20),
    NO_SHADOW(0x40),
    WORLD_LOCK(0x80),
    BETA(0x100),
    AUTO_PICKUP(0x200),
    MOD(0x400),
    RANDOM_GROW(0x800),
    PUBLIC(0x1000),
    FOREGROUND(0x2000),
    HOLIDAY(0x4000),
    UNTRADABLE(0x8000);

    private final int value;

    ItemProperty(int flag) {
      this.value = flag;
    }

    /**
     * Convert an int flag to a {@link EnumSet} of {@link ItemProperty}.
     *
     * @param flags the int flags.
     * @return an EnumSet of ItemProperties.
     */
    public static EnumSet<ItemProperty> fromInt(int flags) {
      return Arrays.stream(values())
          .filter(property -> (flags & property.value) != 0)
          .collect(Collectors.toCollection(() -> EnumSet.noneOf(ItemProperty.class)));
    }
  }

  /** Enum to store all possible types of an item. */
  public enum ItemType {
    FIST,
    WRENCH,
    DOOR,
    LOCK,
    GEMS,
    TREASURE,
    DEADLY_BLOCK,
    TRAMPOLINE_BLOCK,
    CONSUMABLE,
    GATEWAY,
    SIGN,
    SFX_FOREGROUND,
    TOGGLEABLE_FOREGROUND,
    MAIN_DOOR,
    PLATFORM,
    BEDROCK,
    PAIN_BLOCK,
    FOREGROUND_BLOCK,
    BACKGROUND_BLOCK,
    SEED,
    CLOTHING,
    ANIMATED_FOREGROUND_BLOCK,
    SFX_BACKGROUND,
    TOGGLEABLE_BACKGROUND,
    BOUNCY,
    SPIKE,
    PORTAL,
    CHECKPOINT,
    SHEET_MUSIC,
    SLIPPERY_BLOCK,
    UNKNOWN_30,
    SWITCH_BLOCK,
    CHEST,
    MAILBOX,
    BULLETIN_BOARD,
    EVENT_MYSTERY_BLOCK,
    RANDOM_BLOCK,
    COMPONENT,
    PROVIDER,
    CHEMICAL_COMBINER,
    ACHIEVEMENT_BLOCK,
    WEATHER_MACHINE,
    SCOREBOARD,
    SUNGATE,
    PROFILE,
    TOGGLEABLE_DEADLY_BLOCK,
    HEART_MONITOR,
    DONATION_BOX,
    TOY_BOX,
    MANNEQUIN,
    SECURITY_CAMERA,
    MAGIC_EGG,
    GAME_BLOCK,
    GAME_GENERATOR,
    XENONITE,
    PHONE_BOOTH,
    CRYSTAL,
    CRIME_VILLAIN,
    CLOTHING_COMPACTOR,
    SPOTLIGHT,
    PUSHING_BLOCK,
    DISPLAY,
    VENDING,
    FISH_TANK_PORT,
    FISH,
    SOLAR_COLLECTOR,
    FORGE,
    GIVING_TREE,
    GIVING_TREE_STUMP,
    STEAM_BLOCK,
    STEAM_PAIN_BLOCK,
    STEAM_MUSIC_BLOCK,
    SILKWORM,
    SEWING_MACHINE,
    COUNTRY_FLAG,
    LOBSTER_TRAP,
    PAINTING_EASEL,
    BATTLE_PET_CAGE,
    PET_TRAINER,
    STEAM_ENGINE,
    LOCK_BOT,
    WEATHER_MACHINE_S1,
    SPIRIT_STORAGE,
    DISPLAY_SHELF,
    VIP,
    CHALLENGE_TIMER,
    CHALLENGE_FLAG,
    FISH_MOUNT,
    PORTRAIT,
    WEATHER_MACHINE_S2,
    FOSSIL,
    FOSSIL_PREP_STATION,
    DNA_PROCESSOR,
    BLASTER,
    VALHOWLA_TREASURE,
    CHEMSYNTH,
    CHEMSYNTH_TANK,
    UNTRADE_A_BOX,
    OVEN,
    AUDIO,
    GEIGER_CHARGER,
    ADVENTURE_RESET,
    TOMB_ROBBER,
    FACTION,
    RED_FACTION,
    GREEN_FACTION,
    BLUE_FACTION,
    ARTIFACT,
    TRAMPOLINE_MOMENTUM,
    FISH_TRAINING_TANK,
    FISHING_BLOCK,
    ITEM_SUCKER,
    PLANTER,
    ROBOT,
    COMMAND,
    TICKET,
    STATS_BLOCK,
    FIELD_NODE,
    OUIJA_BOARD,
    ARCHITECT_MACHINE,
    STARSHIP,
    AUTO_DELETE,
    BOOMBOX2,
    AUTOMATIC_ACTION_BREAK,
    AUTOMATIC_ACTION_HARVEST,
    AUTOMATIC_ACTION_SUCK,
    LIGHTNING_IF_ON,
    PHASED_BLOCK,
    MUD,
    ROOT_CUTTING,
    UNKNOWN_130,
    PHASED_BLOCK_2,
    BOMB,
    PVE_NPC,
    UNKNOWN;

    @JsonCreator
    public static ItemType fromInt(int index) {
      return values()[index];
    }
  }

  /** Enum to store all possible clothing types. */
  public enum ItemClothingType {
    HAT,
    SHIRT,
    PANTS,
    FEET,
    FACE,
    HAND,
    BACK,
    HAIR,
    CHEST,
    UNKNOWN;

    @JsonCreator
    public static ItemClothingType fromInt(int index) {
      return values()[index];
    }
  }

  /**
   * Record to store effects when a player uses an item with effects (null if item does not give
   * effects).
   *
   * @param name player mod obtained when a player uses an item with an effect.
   * @param onUseMessage message shown when a player uses an item with an effect.
   * @param onRemoveMessage message shown when a player removes an item with an effect.
   */
  public record ItemEffect(
      @NotNull String name, @NotNull String onUseMessage, @NotNull String onRemoveMessage) {}

  /** Record to store abilities of pets that can be used in Pet Battle. */
  public record PetBattleAbility(
      @NotNull String abilityName,
      @NotNull String abilityDescription,
      int powerCooldown,
      @NotNull String petPrefix,
      @NotNull String petSuffix,
      @NotNull String elementType) {
    /**
     * Formats this into a readable string.
     *
     * @return the formatted string
     */
    // NOTE: Might need to escape abilityName
    @NotNull
    @Contract(pure = true)
    public String asFormattedString() {
      return """
             **%s**
             %s
             Cooldown: %ss
             """
          .formatted(abilityName, abilityDescription, powerCooldown);
    }
  }

  /** Record to store items that can be bought in-game in the store. */
  public record StoreItem(
      CurrencyType currencyType, int price, float priceUsd, @Nullable String info) {
    /** enum of available currency types. */
    public enum CurrencyType {
      GEMS,
      GROWTOKENS,
      MONEY;

      @JsonCreator
      public static CurrencyType fromInt(int index) {
        return values()[index];
      }
    }
  }
}
