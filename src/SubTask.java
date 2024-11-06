public class SubTask extends Task{
    private Integer epic;

    public SubTask(String name, String description, Integer epic) {
        super(name, description);
        this.epic = epic;
    }

    /*Конструктор для тестирования обновления через создание нового объекта с тем же id*/
    public SubTask(Integer id, String name, String description, TaskStatus status, Integer epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    public Integer getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                " id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", epic=" + epic + '\'' +
                "} ";
    }
}
