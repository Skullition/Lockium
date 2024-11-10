package dev.skullition.lockium.builder;

import dev.skullition.lockium.model.GrowtopiaWikiItem;

/**
 * Builder to create an instance of {@link
 * dev.skullition.lockium.model.GrowtopiaWikiItem.GrowtopiaItemField}.
 */
public class GrowtopiaItemFieldBuilder {
  private String type;
  private String chi;
  private String textureType;
  private String collisionType;
  private String hitsToBreak;
  private String seedColor;
  private String growTime;
  private String gems;

  public void setType(String type) {
    this.type = type;
  }

  public GrowtopiaWikiItem.GrowtopiaItemField build() {
    return new GrowtopiaWikiItem.GrowtopiaItemField(
        type, chi, textureType, collisionType, hitsToBreak, seedColor, growTime, gems);
  }

  public void setChi(String chi) {
    this.chi = chi;
  }

  public void setTextureType(String textureType) {
    this.textureType = textureType;
  }

  public void setCollisionType(String collisionType) {
    this.collisionType = collisionType;
  }

  public void setHitsToBreak(String hitsToBreak) {
    this.hitsToBreak = hitsToBreak;
  }

  public void setSeedColor(String seedColor) {
    this.seedColor = seedColor;
  }

  public void setGrowTime(String growTime) {
    this.growTime = growTime;
  }

  public void setGems(String gems) {
    this.gems = gems;
  }
}
