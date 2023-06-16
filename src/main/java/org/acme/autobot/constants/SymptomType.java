package org.acme.autobot.constants;

import lombok.Getter;
import org.acme.autobot.request.DiagnosisRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
public enum SymptomType {

    FEVER("C0015967"), PAIN("C0030193"), PAIN_IN_CHEST("C0008031"),
    SHORTNESS_OF_BREATH("C0392680"), DIZZINESS("C0012833"), NAUSEA("C0027497"),
    SWEAT("C0038990"), VOMITING("C0042963"), HEADACHE("C0018681"),
    FATIGUE("C0015672"), DIARRHEA("C0011991"), COUGH("C0010200"),
    CHILL("C0085593"), ASTHENIA("C0004093"), SYNCOPE("C0039070"),
    ORTHOPNEA("C0085619"), TACHYPNEA("C0231835"), WHEEZING("C0043144"),
    RALE("C0034642"), UNRESPONSIVENESS("C0241526"), SUICIDAL("C0438696"),
    FEELING_HOPELESSNESS("C0150041"), SLEEPLESSNESS("C0917801"), IRRITABLE_MOOD("C0022107"),
    BLACKOUT("C0312422"), WORRY("C0233481"), AGITATION("C0085631"),
    TREMOR("C0040822"), NIGHTMARE("C0028084"), HALLUCINATIONS_VISUAL("C0233763"),
    HALLUCINATIONS_AUDITORY("C0233762");

    private final String cui;

    SymptomType(String cui) {
        this.cui = cui;
    }

    public static List<SymptomType> getSymptomTypeList(DiagnosisRequest request) {
        var symptomTypeList = new ArrayList<SymptomType>();
        if (request.isHaveFever()) symptomTypeList.add(FEVER);
        if (request.isHaveHeadache()) symptomTypeList.add(HEADACHE);
        if (request.isHaveCough()) symptomTypeList.add(COUGH);
        if (request.isHaveChill()) symptomTypeList.add(CHILL);
        if (request.isFeelingTired()) symptomTypeList.add(FATIGUE);
        if (request.isFeelingBodyWeakness()) symptomTypeList.add(ASTHENIA);
        if (request.isHaveFainted()) symptomTypeList.add(SYNCOPE);
        if (request.isFeelingDizziness()) symptomTypeList.add(DIZZINESS);
        if (request.isHaveNausea()) symptomTypeList.add(NAUSEA);
        if (request.isHaveVomiting()) symptomTypeList.add(VOMITING);
        if (request.isHaveDiarrhea()) symptomTypeList.add(DIARRHEA);
        if (request.isHaveSweat()) symptomTypeList.add(SWEAT);
        if (request.isHavePainInChest()) symptomTypeList.add(PAIN_IN_CHEST);
        if (request.isHavePain()) symptomTypeList.add(PAIN);
        if (request.isFeelingShortnessOfBreath()) symptomTypeList.add(SHORTNESS_OF_BREATH);
        if (request.isHaveDifficultyBreathing()) symptomTypeList.add(ORTHOPNEA);
        if (request.isHaveRapidBreathing()) symptomTypeList.add(TACHYPNEA);
        if (request.isHaveBreathingSound()) symptomTypeList.add(WHEEZING);
        if (request.isHaveBreathingSound()) symptomTypeList.add(RALE);
        if (request.isFeelingUnresponsiveness()) symptomTypeList.add(UNRESPONSIVENESS);
        if (request.isFeelingSuicidal()) symptomTypeList.add(SUICIDAL);
        if (request.isFeelingHopeless()) symptomTypeList.add(FEELING_HOPELESSNESS);
        if (request.isHaveSleeplessness()) symptomTypeList.add(SLEEPLESSNESS);
        if (request.isHaveIrritableMood()) symptomTypeList.add(IRRITABLE_MOOD);
        if (request.isHaveLossOfConsciousness()) symptomTypeList.add(BLACKOUT);
        if (request.isFeelingWorried()) symptomTypeList.add(WORRY);
        if (request.isFeelingAgitated()) symptomTypeList.add(AGITATION);
        if (request.isHaveBodyShaking()) symptomTypeList.add(TREMOR);
        if (request.isHaveNightmare()) symptomTypeList.add(NIGHTMARE);
        if (request.isHaveAuditoryHallucinations()) symptomTypeList.add(HALLUCINATIONS_AUDITORY);
        if (request.isHaveVisualHallucination()) symptomTypeList.add(HALLUCINATIONS_VISUAL);
        return symptomTypeList;
    }

}
