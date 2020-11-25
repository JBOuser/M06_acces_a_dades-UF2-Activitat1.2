package activitat_1_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDB {

	//Create a the Table "Clients"
	public Connection createTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "clients";
		
		String sentenceSQL = "CREATE TABLE CLIENTS ("
				+"dni VARCHAR(8),"
				+"nom VARCHAR(30),"
				+"premium VARCHAR(1),"
				+"CONSTRAINT dni_pk PRIMARY KEY (dni)"
				+")";
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()) //Resource try-catch
			{
				if(statement != null)
				{
					statement.execute(sentenceSQL); //execute SQL sentence
					System.out.println("Table "+tableName+" created -- OK");
				}
				else
				{
					System.out.println("Table "+tableName+" not created -- ERROR");
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}			
		}
				
		return connection;
	}
	
	
	//Delete the content of table "Clients"
	public Connection clearTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "clients";
		
		String sentenceSQL = "DELETE FROM clients";
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()) //Resource try-catch
			{
				if(statement != null)
				{
					statement.execute(sentenceSQL); //execute SQL sentence
					System.out.println("Table "+tableName+" cleaned -- OK");
				}
				else
				{
					System.out.println("Table "+tableName+" not cleaned -- ERROR");
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}			
		}
				
		return connection;
	}	
		
	//Insert the Client to table Clients
	public void insertClient(Client client)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "INSERT INTO CLIENTS (dni,nom,premium) values (?,?,?);";
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 

				ps.setString(1, client.getDni());
				ps.setString(2, client.getNom());
				ps.setString(3, ((client.isPremium())?"1":"0")); //if true "1", else "0"	
				ps.executeUpdate(); //execute the Update in the DB
				

				System.out.println("Client "+client.getDni()+" inserted -- OK");
			}
			catch(Exception e)
			{
				System.out.println("PreparedStatement error -- ERROR ("+e+")");
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}				
		}
	}	
	
	
	//Print all Clients
	public List<Client> getClients()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String query = "SELECT * FROM CLIENTS;";
		
		List<Client> clients = new ArrayList<Client>();
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()){ //avoid SQL injection instead of using String concatenation 
				
				ResultSet rs = statement.executeQuery(query); //execute the Update in the DB
				while(rs.next())
				{
					Client client = new Client();
					client.setDni(rs.getString("dni"));
					client.setNom(rs.getString("nom"));
					client.setPremium( !rs.getString("premium").matches("0") );
					clients.add(client);
				}	
			}
			catch(Exception e)
			{
				System.out.println(e);
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}				
		}
		return clients;
	}	
	
	
	//Check if "Client" exist
	public int findClient(String dni)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "SELECT * FROM clients WHERE dni='"+dni+"';";
		
		int clientFoundTimes = 0;
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()){ //avoid SQL injection instead of using String concatenation 
				
				ResultSet rs = statement.executeQuery(query); //execute the Update in the DB
				while(rs.next())
				{
					clientFoundTimes++;
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}	
		}
		return clientFoundTimes;
	}		
	
	
	//Print all Clients
	public void printClients()
	{
		if(0 < getClients().size())
		{
			for(Client client : getClients())
			{
				System.out.println(client.toString());
			}			
		}
	}	
	
}
