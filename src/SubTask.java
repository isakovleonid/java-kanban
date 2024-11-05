public class SubTask extends Task{
    // конечный статус эпика
    private static final TaskStatus END_EPIC_STATUS = TaskStatus.DONE;
    // ссылка на идентификатор эпика
    private Integer epic;

    public SubTask(String name, String description, Integer epic) {
        super(name, description);
        this.epic = epic;
    }

    /*TODO: Конструктор для тестирования обновления*/
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
