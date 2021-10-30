package Programme.ProgrammeLogic;

//Результат работы программы/подпрограммы
public class ProgrammeResult<T>{
    private String message;
    private boolean isSuccessful;
    private T value;

    public ProgrammeResult(){
        message = "Не были получены входные данные!";
        isSuccessful = false;
    }

    public ProgrammeResult(String message, boolean isSuccessful, T value){
        this.message = message;
        this.isSuccessful = isSuccessful;
        this.value = value;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setSuccessStatus(boolean status){
        isSuccessful = status;
    }

    public String getMessage(){
        return message;
    }

    public T getValue() {
        return value;
    }

    public boolean getSuccessStatus(){
        return isSuccessful;
    }
}
