package com.quotehunters.quotecollection.model.dto;

public class PeriodDTO {

    private int periodId;
    private String periodName;

    public PeriodDTO() {
    }

    public PeriodDTO(int periodId, String periodName) {
        this.periodId = periodId;
        this.periodName = periodName;
    }

    public int getPeriodId() {
        return periodId;
    }
    public String getPeriodName() {
        return periodName;
    }


    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    @Override
    public String toString() {
        return "PeriodDTO{" +
                "periodId=" + periodId +
                ", periodName='" + periodName + '\'' +
                '}';

        }
    }


