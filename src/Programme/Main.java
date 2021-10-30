package Programme;

import Programme.ProgrammeLogic.MainMenu;

public class Main {
    public static void main(String[] args){
        //Запуск меню программы
        byte ApplicationCode = MainMenu.runMenu();
        System.out.print("Завершение работы...");
    }
}
