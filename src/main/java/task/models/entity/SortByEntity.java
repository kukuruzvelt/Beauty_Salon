package task.models.entity;

public class SortByEntity{
    private String callback;
    private String name;

    public SortByEntity(String callback, String name){
        this.callback = callback;
        this.name = name;
    }

    public String getCallback() {
        return callback;
    }

    public String getName() {
        return name;
    }
}
