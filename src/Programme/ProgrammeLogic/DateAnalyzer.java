package Programme.ProgrammeLogic;

//Анализатор даты
public class DateAnalyzer implements Analyzer<String>{
    public ProgrammeResult<String> analyze(String data) throws NumberFormatException{
        String[] splittedData = data.split("[\\s.]+");
        ProgrammeResult<String> result = new ProgrammeResult<>("Получены корректные данные!", true, "");

        if (splittedData.length == 3){//Проверка на соответствие формату дд.мм.гггг (при разбиении строки получится 3 элемента)
            //Установка нулевых разрядов по маске
            if (splittedData[0].length() == 1)
                splittedData[0] = "0" + splittedData[0].charAt(0);
            if (splittedData[1].length() == 1)
                splittedData[1] = "0" + splittedData[1].charAt(0);
            if (splittedData[2].length() == 1)
                splittedData[2] = "000" + splittedData[2].charAt(0);

            //Определение корректности дня в месяце
            byte daysAmount = switch (Byte.parseByte(splittedData[1])) {
                case 2 -> (byte)29;
                case 4, 6, 9, 11 -> (byte)30;
                case 1, 3, 5, 7, 8, 10, 12 -> (byte)31;
                default -> (byte)-1;
            };
            if (daysAmount == -1){
                result.setMessage("Значение месяца должно быть в пределах 1-12!");
                result.setSuccessStatus(false);
            }
            else if (!(Byte.parseByte(splittedData[0]) >= 1 && Byte.parseByte(splittedData[0]) <= daysAmount)){
                result.setMessage(String.format("Значение дня должно быть в диапазоне %d-%d", 1, daysAmount));
                result.setSuccessStatus(false);
            }
        }
        else{
            result.setMessage("Неверный формат даты!");
            result.setSuccessStatus(false);
        }

        //Обновление даты по маске если проверка прошла успешно
        if (result.getSuccessStatus())
            result.setValue(String.format("%s.%s.%s", splittedData[0], splittedData[1], splittedData[2]));

        return result;
    }
}
