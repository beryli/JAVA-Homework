//資管二A
//105403002
//李美慧


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class MemberDisplay extends JFrame
{
	//
	private People currentEntry;   
	private List<People> results;   
	private int numberOfEntries = 0;
	private int currentEntryIndex;
	//
	
	private JFrame window;
	private static ResultSetTableModel tableModel;
	private JTable resultTable;
	
	
	private JPanel queryPanel;
	private JLabel queryLabel;
	private JComboBox<String> queryComboBox;
	private static final String[] field = {"MemberID", "Name", "Phone Number", "Email", "Sex"};
	private JTextField queryField;
	private JButton queryButton;
	
	private JPanel editPanel;
	private JLabel IDLabel;
	private JLabel nameLabel;
	private JLabel phoneLabel;
	private JLabel emailLabel;
	private JLabel sexLabel;
	private JTextField IDField;
	private JTextField nameField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField sexField;
	
	private JPanel buttonPanel;
	private JButton browseButton;
	private JButton insertButton;
	private JButton updateButton;
	private JButton deleteButton;
	
	
	public MemberDisplay()
	{
		try
		{
			window = new JFrame("Member");
			tableModel =  new ResultSetTableModel();
			resultTable = new JTable(tableModel);
		
			queryPanel = new JPanel();
			queryLabel = new JLabel();
			queryComboBox = new JComboBox<String>(field);
			queryField = new JTextField(20);
			queryButton = new JButton();
		
			editPanel = new JPanel();
			IDLabel = new JLabel();
			nameLabel = new JLabel();
			phoneLabel = new JLabel();
			emailLabel = new JLabel();
			sexLabel = new JLabel();
			IDField = new JTextField(8);
			nameField = new JTextField(8);
			phoneField = new JTextField(8);
			emailField = new JTextField(8);
			sexField = new JTextField(8);
		
			buttonPanel = new JPanel();
			browseButton = new JButton();
			insertButton = new JButton();
			updateButton = new JButton();
			deleteButton = new JButton();
			//**************************************************//
			
			results = tableModel.getAllPeople();
			
			
			queryLabel.setText("Field Name:");
			queryPanel.add(Box.createHorizontalStrut(10));
			queryPanel.add(queryLabel);
			queryPanel.add(Box.createHorizontalStrut(10));
			queryPanel.add(queryComboBox);
			queryPanel.add(Box.createHorizontalStrut(10));
			queryPanel.add(queryField);
			queryPanel.add(Box.createHorizontalStrut(10));
			queryButton.setText("Find");
			queryPanel.add(queryButton);
			queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.X_AXIS));
			queryPanel.setBorder(BorderFactory.createTitledBorder("Find an entry by a field"));
			
			editPanel.setLayout(new GridLayout(5, 2, 1, 1));
			IDLabel.setText("MemberID:");
			nameLabel.setText("Name:");
			phoneLabel.setText("Phone Number:");
			emailLabel.setText("Email:");
			sexLabel.setText("Sex:");
			IDField.setEditable(false);
			editPanel.add(IDLabel);
			editPanel.add(IDField);
			editPanel.add(nameLabel);
			editPanel.add(nameField);
			editPanel.add(phoneLabel);
			editPanel.add(phoneField);
			editPanel.add(emailLabel);
			editPanel.add(emailField);
			editPanel.add(sexLabel);
			editPanel.add(sexField);
			
			buttonPanel.setLayout(new GridLayout(1, 4));
			browseButton.setText("Browse All Entries");
			insertButton.setText("Insert New Entry");
			updateButton.setText("Update");
			deleteButton.setText("Delete");
			buttonPanel.add(browseButton);
			buttonPanel.add(insertButton);
			buttonPanel.add(updateButton);
			buttonPanel.add(deleteButton);
			
        	window.add(queryPanel, BorderLayout.NORTH);
        	window.add(editPanel, BorderLayout.WEST);
        	window.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        	window.add(buttonPanel, BorderLayout.SOUTH);
        
        	window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        	window.setSize(800, 400); 
        	window.setVisible(true);
        	//window.setResizable(false);
        	
         	final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        	resultTable.setRowSorter(sorter);
        	
        	
            resultTable.getSelectionModel().addListSelectionListener
            (
            	new ListSelectionListener()
                {
					public void valueChanged(ListSelectionEvent evt) {
						tableModelActionPerformed(evt);
					} 
                } 
            );
            
        	queryButton.addActionListener
        	(
        		new ActionListener()
                {
                	public void actionPerformed(ActionEvent evt)
                	{
                		queryButtonActionPerformed(evt);
               		} 
                }         			
        	);
        	
            browseButton.addActionListener
            (
            	new ActionListener()
                {
            		public void actionPerformed(ActionEvent evt)
            		{
            			browseButtonActionPerformed(evt);
            		} 
                } 
            );
            
            insertButton.addActionListener
            (
            	new ActionListener()
                {
            		public void actionPerformed(ActionEvent evt)
            		{
            			insertButtonActionPerformed(evt);
            			browseButtonActionPerformed(evt);
            		} 
                } 
            );
            
            deleteButton.addActionListener
            (
            	new ActionListener()
                {
            		public void actionPerformed(ActionEvent evt)
            		{
            			deleteButtonActionPerformed(evt);
            			browseButtonActionPerformed(evt);
            		} 
                } 
            );
            
            updateButton.addActionListener
            (
            	new ActionListener()
                {
            		public void actionPerformed(ActionEvent evt)
            		{
            			updateButtonActionPerformed(evt);
            			browseButtonActionPerformed(evt);
            		} 
                } 
            );
 
		}
		catch (SQLException sqlException) 
		{
			JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
			tableModel.disconnectFromDatabase();
			System.exit(1); // terminate application
		}
	}
	
	private void updateButtonActionPerformed(ActionEvent evt)
	{
		currentEntryIndex = Integer.parseInt(IDField.getText());

		 if((phoneField.getText().matches("09[0-9]{2}-[0-9]{6}"))
				 &&(emailField.getText().matches( "^[a-zA-Z]+[_a-z0-9-]+([.][_a-z0-9-]+)*"
											+ "@[a-z0-9-]+.([a-z0-9-]+[.])*([a-z0-9-]+)$")) )
		 {
				tableModel.updatePeople(currentEntryIndex, nameField.getText(), phoneField.getText(), emailField.getText(), sexField.getText());
				JOptionPane.showMessageDialog(this, "Member update!");
				results = tableModel.getAllPeople();
		 }
		 else 
		 {
			 JOptionPane.showMessageDialog(this, "格式錯誤");
			 System.out.println("");
		 }
	}	
	
	private void deleteButtonActionPerformed(ActionEvent evt)
	{

		tableModel.deletePeople(Integer.parseInt(IDField.getText()));
		JOptionPane.showMessageDialog(this, "Member deleted!");
		results = tableModel.getAllPeople();
	}	
	
	private void insertButtonActionPerformed(ActionEvent evt)
	{
		 if((phoneField.getText().matches("09[0-9]{2}-[0-9]{6}"))
				 &&(emailField.getText().matches( "^[a-zA-Z]+[_a-z0-9-]+([.][_a-z0-9-]+)*"
											+ "@[a-z0-9-]+.([a-z0-9-]+[.])*([a-z0-9-]+)$")) )
		 {
			 tableModel.addPeople(nameField.getText(), phoneField.getText(), emailField.getText(), sexField.getText());
			 JOptionPane.showMessageDialog(this, "Member added!");
			 results = tableModel.getAllPeople();
		 }
		 else 
		 {
			 JOptionPane.showMessageDialog(this, "格式錯誤");
			 System.out.println("");
		 }
	}
	
	private void browseButtonActionPerformed(ActionEvent evt)
	{
			try 
	        {
	            tableModel.setQuery("SELECT * FROM people");
	        }
	        catch (SQLException sqlException) 
	        {
	           JOptionPane.showMessageDialog(null, 
	              sqlException.getMessage(), "Database error", 
	              JOptionPane.ERROR_MESSAGE);
	           
	           // try to recover from invalid user query 
	           // by executing default query
	           try 
	           {
	              tableModel.setQuery("SELECT * FROM people");
	           } 
	           catch (SQLException sqlException2) 
	           {
	              JOptionPane.showMessageDialog(null, 
	                 sqlException2.getMessage(), "Database error", 
	                 JOptionPane.ERROR_MESSAGE);

	              // ensure database connection is closed
	              tableModel.disconnectFromDatabase();

	              System.exit(1); // terminate application
	           }                 
	        }
	}
	
	private void queryButtonActionPerformed(ActionEvent evt) 
	{
		String query = "";
		String columnName = queryComboBox.getSelectedItem().toString();
		String find = queryField.getText();
		
		if(columnName.equals("Email"))
		{
			query = "SELECT * FROM people WHERE e_mail LIKE '%" + find + "%'";
			//System.out.println(query);
		}else if(columnName.equals("Phone Number"))
		{
			query = "SELECT * FROM people WHERE phone LIKE '%" + find + "%'";
			//System.out.println(query);
		}else
		{
			query = "SELECT * FROM people WHERE " + columnName + " LIKE '%" + find + "%'";
			//System.out.println(query);
		}
		
		try 
        {
			tableModel.setQuery(query);//SELECT * FROM authors WHERE lastName LIKE 'Deitel'
        }
        catch (SQLException sqlException) 
        {
           JOptionPane.showMessageDialog(null, 
              sqlException.getMessage(), "Database error", 
              JOptionPane.ERROR_MESSAGE);
           
           // try to recover from invalid user query 
           // by executing default query
           try 
           {
              tableModel.setQuery("SELECT * FROM people");
           } 
           catch (SQLException sqlException2) 
           {
              JOptionPane.showMessageDialog(null, 
                 sqlException2.getMessage(), "Database error", 
                 JOptionPane.ERROR_MESSAGE);

              // ensure database connection is closed
              tableModel.disconnectFromDatabase();

              System.exit(1); // terminate application
           }                 
        }
	}//搜尋
	
	private void tableModelActionPerformed(ListSelectionEvent evt)
	{
		try
	    {
	        currentEntryIndex = resultTable.getSelectedRow();;
	        currentEntry = results.get(currentEntryIndex);
	        IDField.setText("" + currentEntry.getMemberID());
	        nameField.setText(currentEntry.getName());
	        phoneField.setText(currentEntry.getPhone());
	        emailField.setText(currentEntry.getEmail());
	        sexField.setText(currentEntry.getSex());
	    } 
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}

	
	
	public static void main(String args[])
	{
		new MemberDisplay();
	} 
}
