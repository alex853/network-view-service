package net.simforge.networkview.map;

public enum Network {
    VATSIM(1),
    IVAO(2);

    private int code;

    Network(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
