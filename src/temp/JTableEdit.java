package temp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JTableEdit {
    
    private static final String TABLE_NAME = "spr_Model";
    
    public static void main(String[] args) {
        try {
            
            final Connection con = SQLiteConnection.getConnection();
            
            final MyTableModel mod = new MyTableModel(con, TABLE_NAME);
            
            JTable jtable = new JTable(mod);
            //jtable.setAutoCreateRowSorter(true);
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(mod);
            jtable.setRowSorter(sorter);
            
            MyTableRenderer cellRenderer = new MyTableRenderer();
            jtable.setDefaultRenderer(Object.class, cellRenderer);
            
            JScrollPane scroller = new JScrollPane(jtable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            JFrame frame = new JFrame("Загрузка данных в JTable");
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JButton refreshButton = new JButton("Загрузить в базу данных");
            
            JPanel panel = new JPanel();
            panel.add(refreshButton);
            panel.add(scroller);
            
            frame.getContentPane().add(panel);
            
            frame.addWindowStateListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    if (con!=null) try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(JTableEdit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            });
            
            refreshButton.addActionListener(new ActionListener() {
                

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (mod.updateDB(TABLE_NAME)) JOptionPane.showMessageDialog(null, "Данные упешно обновлены!");
                    else JOptionPane.showMessageDialog(null, "Ошибка при обновлении данных!");
                    
                }
            });
            
            jtable.getModel().addTableModelListener(new TableModelListener() {
                
                @Override
                public void tableChanged(TableModelEvent e) {
                    int row = e.getFirstRow();
                    
                    int column = e.getColumn();
                    
                    System.out.println(row+" "+column);
                    
                    TableModel model = (TableModel) e.getSource();
                    String columnName = model.getColumnName(column);
                    Object data = model.getValueAt(row, column);
                }
                
                
            }); 
            
            
            frame.setVisible(true);
            
//            con.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
