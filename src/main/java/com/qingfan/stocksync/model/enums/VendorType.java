package com.qingfan.stocksync.model.enums;

public enum VendorType {

    VENDOR_A("VendorA"),
    VENDOR_B("VendorB");

    private final String displayName;

    VendorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
