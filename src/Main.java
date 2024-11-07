public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager tm = new TaskManager();

        /* Создание Task*/
        Task t1 = new Task("задача 1", "описание задачи 1");
        tm.addTask(t1);

        t1 = new Task("задача 2", "описание задачи 2");
        tm.addTask(t1);

        /* Создание Epic*/
        Epic epic = new Epic("эпик 1", "описание эпика 1");
        tm.addEpic(epic);

        /* Создание SubTask уже созданому Epic*/
        Integer epicNum = 3;
        SubTask st = new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", epicNum);
        tm.addSubTask(st);

        st = new SubTask("подзадача 5", "описание подзадачи 5 эпика 3", epicNum);
        tm.addSubTask(st);

        System.out.println(tm.toString());

        System.out.println("Меням статус Task с id = 1");

        t1 = new Task(1, "Задача 1","обновление задачи 1", TaskStatus.DONE);
        tm.updateTask(t1);

        System.out.println(tm.toString());

        System.out.println("Меняем статус подзадачи с id = 4. Статус эпика должен стать IN_PROGRESS");
        st = new SubTask(4, "подзадача 4","обновление подзадачи 4 эпика 3", TaskStatus.DONE, epicNum);
        tm.updateSubTask(st);

        System.out.println(tm.toString());

        System.out.println("Меням статус подзадачи с id = 4 и id = 5. Статус эпика должен стать DONE");
        st = new SubTask(4, "подзадача 4","обновление подзадачи 4 эпика 3", TaskStatus.DONE, epicNum);
        tm.updateSubTask(st);
        st = new SubTask(5, "подзадача 5","обновление подзадачи 5 эпика 3", TaskStatus.DONE, epicNum);
        tm.updateSubTask(st);

        System.out.println("Удаляем с отсутствующим Id");
        tm.deleteById(10);

        System.out.println("Удаляем задачу с Id = 1");
        tm.deleteById(1);

        System.out.println("Удаляем подзадача с Id = 4");
        tm.deleteById(4);

        System.out.println("Удаляем эпик с Id = 3");
        tm.deleteById(3);

        System.out.println(tm.toString());
    }
}
