package com.particle.model.ui.score;

public class ScoreObjective {

    /**
     * 默认计分板排序
     */
    public static ObjectiveSortOrder defaultOrder = ObjectiveSortOrder.Ascending;

    /**
     * 默认准则
     * 只能通过命令更改
     */
    public static String defaultCriteria = "dummy";

    private String displaySlotName;

    /**
     * 必须是唯一的
     */
    private String objectiveName;

    private String objectiveDisplayName;

    private String criteriaName = defaultCriteria;

    private ObjectiveSortOrder sortOrder = defaultOrder;

    public String getDisplaySlotName() {
        return displaySlotName;
    }

    public void setDisplaySlotName(String displaySlotName) {
        this.displaySlotName = displaySlotName;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public String getObjectiveDisplayName() {
        return objectiveDisplayName;
    }

    public void setObjectiveDisplayName(String objectiveDisplayName) {
        this.objectiveDisplayName = objectiveDisplayName;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public ObjectiveSortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(ObjectiveSortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
