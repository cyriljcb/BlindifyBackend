package com.cyriljcb.blindify.domain.blindtestsettings;

public class BlindtestSettings {
    private int revealTimeSec;
    private int discoveryTimeSec;

    public BlindtestSettings(int rvl, int dscvr) {
        if (dscvr <= 0 || rvl <= 0 ) {
            throw new InvalidBlindtestSettingsException("The durations must be strictly positive");
        }
        this.revealTimeSec = rvl;
        this.discoveryTimeSec = dscvr;
    }

    public int getDiscoveryTimeSec() {
        return this.discoveryTimeSec;
    }

    public int getRevealTimeSec() {
        return this.revealTimeSec;
    }    
}
