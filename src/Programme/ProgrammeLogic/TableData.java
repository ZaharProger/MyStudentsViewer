package Programme.ProgrammeLogic;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

//Представление данных запросов в таблице
public class TableData<T> {
    private String tableHead; // шапка таблицы
    private ArrayList<T> data; //данные

    public TableData()
    {
        tableHead = "";
        data = new ArrayList<>();
    }

    public TableData (String tableHead, ArrayList<T> data){
        this.tableHead = tableHead;
        this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    public void setTableHead(String tableHead) {
        this.tableHead = tableHead;
    }

    public void setData(ArrayList<T> data) {
        this.data.addAll(data);
    }

    public String getTableHead() {
        return tableHead;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void createTableHead(ResultSetMetaData metaData) throws SQLException {
        tableHead = "|    ";
        for (int columnNumber = 1; columnNumber <= metaData.getColumnCount(); ++columnNumber){
            tableHead += String.format("%s    |", metaData.getColumnName(columnNumber));
            tableHead += (columnNumber != metaData.getColumnCount())? "    " : '\n';
        }
        int headLength = tableHead.length();
        for (int i = 0; i < headLength; ++i){
            tableHead += "-";
        }
    }
}
