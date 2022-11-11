package ToDo.models.days;

public class Day {

    private final DayType DAY_TYPE;
    private final DayTaskList dayTaskList;


    public Day(DayType day_type, DayTaskList dayTaskList) {
        DAY_TYPE = day_type;
        this.dayTaskList = dayTaskList;
    }
}
