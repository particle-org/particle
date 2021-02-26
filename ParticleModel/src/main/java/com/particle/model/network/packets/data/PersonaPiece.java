package com.particle.model.network.packets.data;

public class PersonaPiece {
    private String id;
    private String type;
    private String packId;
    private boolean isDefault;
    private String productId;

    public PersonaPiece() {
    }

    public PersonaPiece(String id, String type, String packId, boolean isDefault, String productId) {
        this.id = id;
        this.type = type;
        this.packId = packId;
        this.isDefault = isDefault;
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
