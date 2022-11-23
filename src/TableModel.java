import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Username", "Score", "Date"};
	private String[][] data;
	
	public static void main(String[] args) {
		SQL sql = new SQL();
		ResultSet result = null;
		try {
			result = sql.selectAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TableModel tablemodel = new TableModel(result);
		
		JTable table = new JTable(tablemodel);
		
		JScrollPane scrollpane = new JScrollPane(table);
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		
		frame.setContentPane(panel);
		panel.add(scrollpane,BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public TableModel(ResultSet results){
		ArrayList<String[]> rows = new ArrayList<String[]>();
		String[] temp;
		
		try {
			while(results.next()) {
				temp = new String[3];
				temp[0] = results.getString("username");
				temp[1] = results.getString("score");
				temp[2] = results.getString("date");
				rows.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String[][] tempdata = new String[rows.size()][3];
		
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