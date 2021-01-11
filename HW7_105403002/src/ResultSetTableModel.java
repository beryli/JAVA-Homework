//資管二A
//105403002
//李美慧


import java.util.regex.PatternSyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ResultSetTableModel extends AbstractTableModel
{
	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	private boolean connectedToDatabase = false;

	
	//
	private static final String URL = "jdbc:mysql://localhost/member";
	private static final String USERNAME = "java";
	private static final String PASSWORD = "java";
	private PreparedStatement selectAllPeople;
	//
	
	
	//Constructor
	public ResultSetTableModel() 
			throws SQLException
	{
		connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		connectedToDatabase = true;
		
		setQuery("SELECT * FROM people");
		
		//
		try 
		{
			selectAllPeople = connection.prepareStatement("SELECT * FROM people");
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1);
		}
		//
	}
	
	
	public List<People> getAllPeople()
	{
		List<People> results = null;
		ResultSet resultSet = null;
	      
		try 
		{
			resultSet = selectAllPeople.executeQuery(); 
			results = new ArrayList<People>();
	         
			while (resultSet.next())
			{
				results.add(new People(
					resultSet.getInt("MemberID"),
					resultSet.getString("name"),
					resultSet.getString("phone"),
					resultSet.getString("e_mail"),
					resultSet.getString("sex")));
			} 
		} 
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();         
		} 
		finally
		{
			try 
			{
				resultSet.close();
			} 
			catch (SQLException sqlException)
			{
				sqlException.printStackTrace();         
				close();
			}
		}
		return results;
	}//回傳的List是用在TableModel上的
	public void close()
	{
		try 
		{
			connection.close();
		} 
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		} 
	} 	

	public void addPeople(String name, String phone, String e_mail, String sex)
	{
			      
		// set parameters, then execute insertNewPerson
		try 
		{
			String query = "INSERT INTO people (name, phone, e_mail, sex) "
					+ "VALUES ('" + name + "', '" + phone + "', '" + e_mail + "', '" + sex + "')";
			
			//System.out.println(query);
			statement.executeUpdate(query);

		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			close();
		} 
	} 

	public void deletePeople(int index)
	{
			      
		// set parameters, then execute insertNewPerson
		try 
		{
			String query = "DELETE FROM people WHERE memberID = '" + index + "'";
			
			System.out.println(query);
			statement.executeUpdate(query);

		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			close();
		} 
	}
	
	public void updatePeople(int index, String name, String phone, String e_mail, String sex)
	{
			      
		// set parameters, then execute insertNewPerson
		try 
		{
			String query = "UPDATE people "
					+ "SET name = '" + name + "' , phone = '" + phone + "' , e_mail  = '" + e_mail + "' , sex  = '" + sex
					+ "' WHERE memberID = '" + index + "' ";

			
			System.out.println(query);
			statement.executeUpdate(query);

		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			close();
		} 
	} 	
	
	//以下是TableModel的Method
	public void setQuery(String query) 
			throws SQLException, IllegalStateException 
	{
		
		if (!connectedToDatabase) 
			throw new IllegalStateException("Not Connected to Database");

		resultSet = statement.executeQuery(query);
		metaData = resultSet.getMetaData();

		// determine number of rows in ResultSet
		resultSet.last(); // move to last row
		numberOfRows = resultSet.getRow();// get row number
		
		
		
		fireTableStructureChanged();
	} 

	
	public int getColumnCount() 
			throws IllegalStateException
	{
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine number of columns
		try
		{
			return metaData.getColumnCount();
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}

		return 0; // if problems occur above, return 0 for number of columns
	} 

	public Object getValueAt(int row, int column) 
			throws IllegalStateException
	{
		// ensure database connection is available
		if (!connectedToDatabase) 
			throw new IllegalStateException("Not Connected to Database");

		// obtain a value at specified ResultSet row and column
		try 
		{
			resultSet.absolute(row + 1);
			return resultSet.getObject(column + 1);
		}
		catch (SQLException sqlException) 
		{
			sqlException.printStackTrace();
		} 
		      
		return ""; // if problems, return empty string object
	} 
	
	public int getRowCount() throws IllegalStateException
	{      
		// ensure database connection is available
		if (!connectedToDatabase) 
			throw new IllegalStateException("Not Connected to Database");
	 
		return numberOfRows;
	}

	public void disconnectFromDatabase()
	{
		if (connectedToDatabase)
		{
			// close Statement and Connection
			try
			{
				resultSet.close();
				statement.close();
				connection.close();
			}                                  
			catch (SQLException sqlException)
			{
				sqlException.printStackTrace();
			}
			finally  // update database connection status
			{
				connectedToDatabase = false;
			}
		}
	}//disconnect
	
	public String getColumnName(int column) throws IllegalStateException
	{    
		// ensure database connection is available
		if (!connectedToDatabase) 
			throw new IllegalStateException("Not Connected to Database");

		// determine column name
		try 
		{
			return metaData.getColumnName(column + 1);  
		} 
		catch (SQLException sqlException) 
		{
			sqlException.printStackTrace();
		} 
	      
		return ""; // if problems, return empty string for column name
	}//有了會顯示columnName

	public Class getColumnClass(int column) throws IllegalStateException
	{
		// ensure database connection is available
		if (!connectedToDatabase) 
			throw new IllegalStateException("Not Connected to Database");

		// determine Java class of column
		try 
		{
			String className = metaData.getColumnClassName(column + 1);
	         
			// return Class object that represents className
			return Class.forName(className);
		}
		catch (Exception exception) 
		{
			exception.printStackTrace();
		} 
	      
		return Object.class; // if problems occur above, assume type Object
	}//有了memberID會置右
	//getColumnCount(), getRowCount(), getValueAt(), getColumnName(int column), getColumnClass(int column)
	//以上是TableModel的Method
}
