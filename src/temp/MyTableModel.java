package temp;

import javax.swing.table.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyTableModel extends AbstractTableModel {

    private static Connection conn;// соединение с базой данных

    private Object[][] contents;// хранит данные
    private String[] columnNames; // хранит имена столбцов
    private Class[] columnClasses; // хранит типы столбцов
    private HashSet<String> cacheSqlList = new HashSet<>();

    public MyTableModel(Connection conn,
                        String tableName)
            throws SQLException {
        super();
        MyTableModel.conn = conn;
        getTableContents(tableName);

    }

    private void getTableContents(String tableName)
            throws SQLException {

        DatabaseMetaData meta = conn.getMetaData();

        ResultSet rs = meta.getColumns(null, null, tableName, null);// получить методанные по столбцам

        ArrayList colNamesList = new ArrayList();// список имен столбцов
        ArrayList colTypesList = new ArrayList();// список типов столбцов

        // цикл по всем столбцам таблицы
        // для каждого столбца определить имя и тип 
        while (rs.next()) {

            colNamesList.add(rs.getString("COLUMN_NAME"));// добавить в список имя столбца

            int dbType = rs.getInt("DATA_TYPE");// определить тип столбца
            System.out.println(rs.getString("DATA_TYPE"));

            // выбрать нужный тип
            switch (dbType) {
                case Types.INTEGER:
                    colTypesList.add(Integer.class);
                    break;
                case Types.FLOAT:
                    colTypesList.add(Float.class);
                    break;
                case Types.DOUBLE:
                case Types.REAL:
                    colTypesList.add(Double.class);
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    colTypesList.add(java.sql.Date.class);
                    break;
                default:
                    colTypesList.add(String.class);
                    break;
            }
        }

        // имена столбцов сохранить в отдельный массив columnNames
        columnNames = new String[colNamesList.size()];
        colNamesList.toArray(columnNames);

        // типы столбцов сохранить в отдельный массив columnClasses
        columnClasses = new Class[colTypesList.size()];
        colTypesList.toArray(columnClasses);


        Statement statement = conn.createStatement();
        rs = statement.executeQuery("SELECT * FROM " + tableName);

        ArrayList rowList = new ArrayList(); // хранит записи из таблицы

        // цикл по всем записям таблицы
        while (rs.next()) {
            ArrayList cellList = new ArrayList();// хранит данные по каждому столбцу (ячейки)

            for (int i = 0; i < columnClasses.length; i++) {
                Object cellValue = null;

                if (columnClasses[i] == String.class) {
                    cellValue = rs.getString(columnNames[i]);
                } else if (columnClasses[i] == Integer.class) {
                    cellValue = new Integer(rs.getInt(columnNames[i]));
                } else if (columnClasses[i] == Float.class) {
                    cellValue = new Float(rs.getFloat(columnNames[i]));
                } else if (columnClasses[i] == Double.class) {
                    cellValue = new Double(rs.getDouble(columnNames[i]));
                } else if (columnClasses[i] == java.sql.Date.class) {
                    cellValue = rs.getDate(columnNames[i]);
                } else {
                    System.out.println("Не могу определить тип поля " + columnNames[i]);
                }

                cellList.add(cellValue);
            }

            Object[] cells = cellList.toArray();
            rowList.add(cells);
        }

        contents = new Object[rowList.size()][];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = (Object[]) rowList.get(i);
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();

    }

    private void addToSqlStatementCache(Object[] objects){
        System.out.println("Prepare to update="+"update " + JTableEdit.TABLE_NAME + " set name_ru='" + objects[1] + "', name_en='" + objects[2] + "' where id=" + objects[0] + ";" );
        cacheSqlList.add("update " + JTableEdit.TABLE_NAME + " set name_ru='" + objects[1] + "', name_en='" + objects[2] + "' where id=" + objects[0] + ";");
    }


    public boolean updateDB(String tableName) {

//        ArrayList<String> sqlList = new ArrayList();
//
//        for (int i = 0; i < contents.length; i++) {
//            Object[] objects = contents[i];
//            sqlList.add("update " + tableName + " set name_ru='" + objects[1] + "', name_en='" + objects[2] + "' where id=" + objects[0] + ";");
//        }

        Statement statement = null;

        try {
            statement = conn.createStatement();

            if (cacheSqlList.isEmpty()) {
                System.out.println("List is empty");
                return false;
            }

            for (String sql : cacheSqlList) {
                statement.executeUpdate(sql);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MyTableModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (cacheSqlList !=null ) cacheSqlList.clear();
                if (statement != null) statement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MyTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }


    @Override
    public int getRowCount() {
        return contents.length;
    }

    @Override
    public int getColumnCount() {
        if (contents.length == 0) {
            return 0;
        } else {
            return contents[0].length;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        return contents[row][column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        contents[rowIndex][columnIndex] = aValue;
        addToSqlStatementCache(contents[rowIndex]);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (getColumnName(columnIndex).equals("id")){
            System.out.println("columnName="+getColumnName(columnIndex));
            return false;
        }
//        if (columnIndex == 0) {
//            return false;
//        }
        return true;
    }
}