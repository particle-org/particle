package com.particle.model.player;

import com.particle.model.entity.model.skin.PlayerSkinData;

public class BasicModelSkin extends PlayerSkinData {
    /**
     * json文件，读取
     */
    private String skinDataEncoded;

    /**
     * json文件读取
     */
    private String capeDataEncoded;

    public String getSkinDataEncoded() {
        return skinDataEncoded;
    }

    public void setSkinDataEncoded(String skinDataEncoded) {
        this.skinDataEncoded = skinDataEncoded;
    }

    public String getCapeDataEncoded() {
        return capeDataEncoded;
    }

    public void setCapeDataEncoded(String capeDataEncoded) {
        this.capeDataEncoded = capeDataEncoded;
    }

    /**
     * 克隆骨架和皮肤
     *
     * @return
     */
    public BasicModelSkin clone() {
        BasicModelSkin basicModelSkin = new BasicModelSkin();

        byte[] skinDataB = new byte[this.getSkinData().length];
        System.arraycopy(this.getSkinData(), 0, skinDataB, 0, skinDataB.length);
        basicModelSkin.setSkinData(skinDataB);

        byte[] capeDataB = new byte[this.getCapeData().length];
        System.arraycopy(this.getCapeData(), 0, capeDataB, 0, capeDataB.length);
        basicModelSkin.setCapeData(capeDataB);

        // 复制皮肤
        basicModelSkin.setSkinId(this.getSkinId());
        basicModelSkin.setSkinName(this.getSkinName());
        basicModelSkin.setSkinHeight(this.getSkinHeight());
        basicModelSkin.setSkinWidth(this.getSkinWidth());
        basicModelSkin.setCapeId(this.getCapeId());
        basicModelSkin.setCapeHeight(this.getCapeHeight());
        basicModelSkin.setCapeWidth(this.getCapeWidth());
        basicModelSkin.setPremiumSkin(this.isPremiumSkin());
        basicModelSkin.setPersonSkin(this.isPersonSkin());
        basicModelSkin.setPersonCapeOnClassicSkin(this.isPersonCapeOnClassicSkin());
        basicModelSkin.setPlayerSkinAnimationData(this.getPlayerSkinAnimationData());
        basicModelSkin.setSerializedAnimationData(this.getSerializedAnimationData());

        // 骨骼
        basicModelSkin.setSkinGeometry(this.getSkinGeometry());
        basicModelSkin.setSkinGeometryName(this.getSkinGeometryName());
        basicModelSkin.setSkinResourcePatch(this.getSkinResourcePatch());
        basicModelSkin.setNewVersion(this.isNewVersion());

        return basicModelSkin;
    }

    /**
     * 只克隆骨架
     *
     * @return
     */
    public BasicModelSkin cloneGeometry() {
        BasicModelSkin basicModelSkin = new BasicModelSkin();
        basicModelSkin.setSkinGeometry(this.getSkinGeometry());
        basicModelSkin.setSkinGeometryName(this.getSkinGeometryName());
        basicModelSkin.setSkinResourcePatch(this.getSkinResourcePatch());
        return basicModelSkin;
    }
}
