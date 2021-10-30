package Programme.DataBaseModel;

//Представление данных таблицы Студенты
public class Student {
    private int id;
    private String name;
    private String surname;
    private String birthday;
    private String group;

    public Student()
    {};

    public Student(int id, String name, String surname, String birthday, String group){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.group = group;
    }

    public Student (Student student){
        id = student.id;
        name = student.name;
        surname = student.surname;
        birthday = student.birthday;
        group = student.group;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGroup() {
        return group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String toString(){
        String tableRow = String.format("|    %d    |       %s       |       %s       |       %s       |       %s       |\n",
                                        id, name, surname, birthday, group);
        int rowLength = tableRow.length();
        for (int i = 0; i < rowLength; ++i){
            tableRow += "-";
        }
        return tableRow;
    }
}
