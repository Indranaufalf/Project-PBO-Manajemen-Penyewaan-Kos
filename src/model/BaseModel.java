package model;

public abstract class BaseModel {

    private int id;

    public BaseModel() {}

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public abstract String getInfoSingkat();

    @Override
    public String toString() {
        return getInfoSingkat();
    }
}
