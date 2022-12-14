import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"ID", "Username", "Score", "Date"};
	private String[][] data;
	
	public TableModel(ResultSet results){
		ArrayList<String[]> rows = new ArrayList<String[]>();
		String[] temp;
		
		try {
			while(results.next()) {
				temp = new String[4];
				temp[0] = results.getString("id");
				temp[1] = results.getString("username");
				temp[2] = results.getString("score");
				temp[3] = results.getString("date");
				rows.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String[][] tempdata = new String[rows.size()][4];
		
		for(int i = 0;i<rows.size();i++) {
			tempdata[i] = rows.get(i);
		}
		
		data = tempdata;
	}
	
	public int getRowCount() {
		return data.length;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		return false;
	}
	
	public Class<?> getColumnClass(int col){
		return getValueAt(0,col).getClass();
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public void setValueAt(Object aValue, int row, int col) {
		data[row][col] = (String) aValue;
	}
}