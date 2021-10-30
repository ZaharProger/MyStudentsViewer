package Programme.DataBaseModel;

//Представление данных таблицы Группы
public class Group {
    private int id;
    private String name;

    public Group(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Group(Group group){
        id = group.id;
        name = group.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        String tableRow = String.format("|    %d    |    %s    |\n", id, name);
        int rowLength = tableRow.length();
        for (int i = 0; i < rowLength; ++i){
            tableRow += "-";
        }
        return tableRow;
    }
}
