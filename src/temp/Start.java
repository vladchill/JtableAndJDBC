package temp;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Start {
  
    public static void main(String[] args) {
        try {

            Connection con = SQLiteConnection.getConnection();

            TableModel mod = new MyTableModel(con, "CarBrand");

            JTable jtable = new JTable(mod);

            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(mod);
//            sorter.setComparator(2, new MyComparator());
            jtable.setRowSorter(sorter);

            MyTableRenderer cellRenderer = new MyTableRenderer();
            jtable.setDefaultRenderer(Object.class, cellRenderer);
            jtable.setDefaultRenderer(Float.class, cellRenderer);
            
            JScrollPane scroller = new JScrollPane(jtable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JFrame frame = new JFrame("Загрузка данных в JTable");

            frame.add(scroller, BorderLayout.CENTER);
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Filter");
            panel.add(label, BorderLayout.WEST);
            final JTextField filterText = new JTextField("A");
            panel.add(filterText, BorderLayout.CENTER);
            frame.add(panel, BorderLayout.NORTH);
            JButton filterButton = new JButton("Filter");
            filterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String text = filterText.getText();
                    if (text.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter(text));
                    }
                }
            });
            frame.add(filterButton, BorderLayout.SOUTH);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.getContentPane().add(scroller);
            frame.pack();
            frame.setVisible(true);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

