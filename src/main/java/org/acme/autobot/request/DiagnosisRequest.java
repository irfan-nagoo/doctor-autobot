package org.acme.autobot.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiagnosisRequest {

    private boolean haveFever;
    private boolean haveHeadache;
    private boolean haveChill;
    private boolean haveCough;
    private boolean feelingTired;
    private boolean feelingBodyWeakness;
    private boolean haveFainted;
    private boolean feelingDizziness;
    private boolean haveVomiting;
    private boolean haveDiarrhea;
    private boolean haveNausea;
    private boolean haveSweat;
    private boolean havePainInChest;
    private boolean havePain;
    private boolean feelingShortnessOfBreath;
    private boolean haveDifficultyBreathing;
    private boolean haveRapidBreathing;
    private boolean haveBreathingSound;
    private boolean feelingUnresponsiveness;
    private boolean feelingSuicidal;
    private boolean feelingHopeless;
    private boolean haveSleeplessness;
    private boolean haveIrritableMood;
    private boolean haveLossOfConsciousness;
    private boolean feelingWorried;
    private boolean feelingAgitated;
    private boolean haveBodyShaking;
    private boolean haveNightmare;
    private boolean haveVisualHallucination;
    private boolean haveAuditoryHallucinations;
}
