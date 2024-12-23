package dev.skullition.lockium.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.skullition.lockium.deserializer.ItemPropertyDeserializer;
import dev.skullition.lockium.util.AppEmojis;
import dev.skullition.lockium.util.ItemUtils;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

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
    @Nullable StoreItem storeItem,
    @Nullable LockeItem lockeItem,
    @Nullable GuildChest guildChestReward,
    @Nullable DailyChallenge dailyChallengeReward,
    @Nullable LegendaryQuestReward legendaryQuestReward,
    @Nullable @JsonProperty("fishingData") FishingItem fishingItem) {
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

  /** Interface for items with standard recipes. */
  public interface ItemRecipe {
    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    void addToEmbed(@NotNull EmbedBuilder embedBuilder);
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
      @NotNull String name, @NotNull String onUseMessage, @NotNull String onRemoveMessage)
      implements ItemRecipe {

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {

      String missingEffectText = "(Missing! Report if you have it!)";
      String onAdd = StringUtils.hasText(onUseMessage) ? onUseMessage : missingEffectText;
      String onRemove = StringUtils.hasText(onRemoveMessage) ? onRemoveMessage : missingEffectText;
      embedBuilder.addField(
          "%s Item Effect (%s)".formatted(AppEmojis.FAIRY_WINGS, name),
          """
              %s %s
              %s %s
              """
              .formatted(AppEmojis.CHECKBOX_ENABLED, onAdd, AppEmojis.CHECKBOX_DISABLED, onRemove),
          false);
    }
  }

  /** Record to store abilities of pets that can be used in Pet Battle. */
  public record PetBattleAbility(
      @NotNull String abilityName,
      @NotNull String abilityDescription,
      int powerCooldown,
      @NotNull String petPrefix,
      @NotNull String petSuffix,
      @NotNull String elementType)
      implements ItemRecipe {

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    // NOTE: Might need to escape abilityName
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {
      String text = asFormattedString();

      embedBuilder.addField(
          "%s Pet Battles".formatted(AppEmojis.BATTLE_LEASH),
          "%s %s".formatted(ItemUtils.stringChiToEmoji(elementType), text),
          false);
    }

    @NotNull
    private String asFormattedString() {
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
      @NotNull CurrencyType currency, int price, float priceUsd, @Nullable String info)
      implements ItemRecipe {

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {
      var text = asFormattedString();
      embedBuilder.addField(
          "%s From the Store"
              .formatted(
                  switch (currency) {
                    case GEMS -> AppEmojis.GEMS;
                    case GROWTOKENS -> AppEmojis.GROWTOKEN;
                    case MONEY ->
                        throw new IllegalStateException(
                            "Currency Money is currently unimplemented."); // TODO Implement this
                  }),
          text,
          false);
    }

    @NotNull
    private String asFormattedString() {
      var stringBuilder = new StringBuilder();
      stringBuilder.append(
          currency == CurrencyType.MONEY
              ? "Costs $%s USD from the store.".formatted(price)
              : "Costs %s %s from the store.".formatted(price, currency.toString()));
      if (StringUtils.hasText(info)) {
        stringBuilder.append(" (%s)".formatted(info));
      }
      return stringBuilder.toString();
    }

    /** enum of available currency types. */
    public enum CurrencyType {
      GEMS,
      GROWTOKENS,
      MONEY;

      @JsonCreator
      public static CurrencyType fromInt(int index) {
        return values()[index];
      }

      @Override
      public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
      }
    }
  }

  /**
   * Record to store items that can be found in Locke, the traveling merchant.
   *
   * @param itemToBuy the item which is the currency of this item in Locke's trade
   * @param cost the price of the item
   * @param canBePurchasedFromLockBot whether the item is also found in LockBot's trades
   */
  public record LockeItem(
      @NotNull SimpleGrowtopiaItem itemToBuy, int cost, boolean canBePurchasedFromLockBot)
      implements ItemRecipe {
    private String asFormattedString() {
      var stringBuilder = new StringBuilder();
      // if item is world lock
      if (itemToBuy.id() == 242) {
        if (cost > 100) {
          int remaining = cost % 100;

          stringBuilder.append("For %s Diamond Locks".formatted(cost / 100));
          if (remaining > 0) {
            stringBuilder.append("and %s World Locks".formatted(remaining));
          }
          return stringBuilder.toString();
        }
        stringBuilder.append("For %s World Lock%s".formatted(cost, cost == 1 ? "" : "s"));
      } else {
        stringBuilder.append("For %s %s".formatted(cost, itemToBuy.name()));
      }

      if (canBePurchasedFromLockBot) {
        stringBuilder.append(". Available from Lock-Bot.");
      } else {
        stringBuilder.append(". Not available from Lock-Bot.");
      }
      return stringBuilder.toString();
    }

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {
      embedBuilder.addField("%s From Locke".formatted(AppEmojis.LOCKE), asFormattedString(), false);
    }
  }

  /** Record to store which guild chest this item comes from. */
  public record GuildChest(
      int quantityGiven,
      @NotNull GuildSeason season,
      @NotNull ChestContributionType contribType,
      @NotNull PersonalTier personalChestTier,
      @NotNull GuildTier guildChestTier,
      @Nullable String extraNote)
      implements ItemRecipe {
    @NotNull
    private String asFormattedString() {
      var stringBuilder = new StringBuilder();

      boolean isGuildChest = contribType == ChestContributionType.GUILD;
      String chestName = isGuildChest ? guildChestTier.toString() : personalChestTier.toString();
      int tier = isGuildChest ? guildChestTier.value : personalChestTier.value;

      stringBuilder.append(
          "%sx from: %s %s's Reward (#%s %s)"
              .formatted(quantityGiven, season, chestName, tier, contribType));
      if (StringUtils.hasText(extraNote)) {
        stringBuilder.append("\n");
        stringBuilder.append(extraNote);
      }
      return stringBuilder.toString();
    }

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {
      embedBuilder.addField(
          "%s Seasonal Guild Chest Reward".formatted(ItemUtils.seasonToEmoji(season)),
          asFormattedString(),
          false);
    }

    /** Which season the chest is from. */
    public enum GuildSeason {
      SUMMER,
      WINTER,
      SPRING;

      @NotNull
      @JsonCreator
      public static GuildSeason fromInt(int index) {
        return values()[index];
      }

      @Override
      public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
      }
    }

    /** Whether this is obtained from the personal or guild reward. */
    public enum ChestContributionType {
      PERSONAL,
      GUILD;

      @NotNull
      @JsonCreator
      public static ChestContributionType fromInt(int index) {
        return values()[index];
      }

      @Override
      public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
      }
    }

    /** Enum to store which personal tier the chest is from. */
    public enum PersonalTier {
      NONE(0),
      STUDENT(5),
      TRAINER(10),
      CONTENDER(15),
      MASTER(20),
      CHAMPION(25);
      private final int value;

      PersonalTier(int value) {
        this.value = value;
      }

      /**
       * Convert an int index to an instance of this enum.
       *
       * @param index the int index
       * @return the personal tier enum
       */
      @NotNull
      @JsonCreator
      public static PersonalTier fromInt(int index) {
        return Arrays.stream(values())
            .filter(tier -> tier.value == index)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid index: %s".formatted(index)));
      }

      @Override
      public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
      }
    }

    /** Enum to store which guild tier the chest is from. */
    public enum GuildTier {
      NONE(0),
      SQUIRE(5),
      KNIGHT(10),
      NOBLE(15),
      KING(20),
      EMPEROR(25);
      private final int value;

      GuildTier(int value) {
        this.value = value;
      }

      /**
       * Convert an int index to an instance of this enum.
       *
       * @param index the int index
       * @return the guild tier enum
       */
      @NotNull
      @JsonCreator
      public static GuildTier fromInt(int index) {
        return Arrays.stream(values())
            .filter(tier -> tier.value == index)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid index: %s".formatted(index)));
      }

      @Override
      public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
      }
    }
  }

  /**
   * Record to store which daily challenge this item comes from.
   *
   * @param challengeName the challenge name
   * @param challengeRules what rules the challenge has
   * @param isGuildReward whether this item is a guild or personal reward
   */
  public record DailyChallenge(
      @NotNull String challengeName, @NotNull String challengeRules, boolean isGuildReward)
      implements ItemRecipe {
    @NotNull
    private String asFormattedString() {
      if (isGuildReward) {
        return """
               Obtained from the %s guild daily challenge (Top 3 guilds).
               Rules: %s
               """
            .formatted(challengeName, challengeRules);
      }
      return """
               Obtained from the %s personal daily challenge (Top 3 personal).
               Rules: %s
               """
          .formatted(challengeName, challengeRules);
    }

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {
      embedBuilder.addField(
          "%s Daily Challenge Reward".formatted(AppEmojis.CHALLENGE_BOARD),
          asFormattedString(),
          false);
    }
  }

  /**
   * Record to store which legendary quest rewards this item.
   *
   * @param description the description of the quest.
   * @param wikiUrl where the user can read more about the quest.
   */
  public record LegendaryQuestReward(
      @NotNull String description, @JsonProperty("wikiURL") @NotNull String wikiUrl)
      implements ItemRecipe {
    @NotNull
    private String asFormattedString() {
      return """
             %s
             [All the quest steps here](%s)
             """
          .formatted(description, wikiUrl);
    }

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {
      embedBuilder.addField(
          "%s Legendary Quest Reward".formatted(AppEmojis.LEGENDARY_WIZARD),
          asFormattedString(),
          false);
    }
  }

  /**
   * Record to store items that can be obtained by fishing.
   *
   * @param obtaining the method to obtain this item
   * @param isFish whether this item is a fish.
   * @param maxSize how big this fish is in lbs, 0 if not a fish.
   */
  public record FishingItem(
      @JsonProperty("description") @NotNull String obtaining,
      @JsonProperty("isAFish") boolean isFish,
      int maxSize)
      implements ItemRecipe {
    @NotNull
    private String asFormattedString() {
      String fishText = isFish ? "Perfect: **`%slb`**".formatted(maxSize) : "";
      return """
             Obtained by fishing with: %s
             %s
             """
          .formatted(obtaining, fishText);
    }

    /**
     * Adds this item recipe to the embed.
     *
     * @param embedBuilder the embed to be added to
     */
    @Override
    public void addToEmbed(@NotNull EmbedBuilder embedBuilder) {

      embedBuilder.addField(
          "%s Fishing Data".formatted(AppEmojis.FISHING_ROD), asFormattedString(), false);
    }
  }
}
