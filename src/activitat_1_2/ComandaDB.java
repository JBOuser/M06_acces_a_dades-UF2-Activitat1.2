package activitat_1_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ComandaDB {

	//Create a the Table "Clients"
	public Connection createTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "comandes";
		
		String sentenceSQL = "CREATE TABLE COMANDES ("
				+"num_comanda VARCHAR(8),"
				+"preu_total FLOAT(2),"
				+"data DATE,"
				+"dni_client VARCHAR(8),"
				+"CONSTRAINT num_comanda_pk PRIMARY KEY (num_comanda),"
				+"CONSTRAINT dni_client_fk FOREIGN KEY (dni_client) REFERENCES clients(dni)"
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
	
	
	//Delete the content of table "Comandes"
	public Connection clearTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "comandes";
		
		String sentenceSQL = "DELETE FROM comandes";
		
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
	public void insertComanda(Client client, Comanda comanda)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "INSERT INTO COMANDES (num_comanda,preu_total,data, dni_client) values (?,?,?,?);";
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 

				ps.setInt(1, comanda.getNum_comanda());
				ps.setFloat(2, comanda.getPreu_total());
				ps.setDate(3, java.sql.Date.valueOf(comanda.getData())); //get LocalDate and set in as Date
				ps.setString(4, client.getDni());
				ps.executeUpdate(); //execute the Update in the DB
				
				System.out.println("Comanda "+comanda.getNum_comanda()+" created -- OK");
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
	public List<Comanda> getComandes()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String query = "SELECT * FROM COMANDES;";
		
		List<Comanda> comandes = new ArrayList<Comanda>();
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()){ //avoid SQL injection instead of using String concatenation 
				
				ResultSet rs = statement.executeQuery(query); //execute the Update in the DB
				while(rs.next())
				{
					Comanda comanda = new Comanda();
					comanda.setNum_comanda(rs.getInt("num_comanda"));
					comanda.setPreu_total(rs.getFloat("preu_total"));
					comanda.setData(rs.getDate("data").toLocalDate()); //convert LocalDate to Date
					comandes.add(comanda);
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
		return comandes;
	}	
	
	
	//Print all Clients
	public void printComandes()
	{
		if(0 < getComandes().size())
		{
			for(Comanda comanda : getComandes())
			{
				System.out.println(comanda.toString());
			}			
		}
	}
	
	
	//get the Comandes of an specified Client
	public HashSet<Comanda> getAllComandes(Client client)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		//get all comandes that match the query
		String query = "SELECT * FROM comandes WHERE dni_client='"+client.getDni()+"';"; 
		
		HashSet<Comanda> comandes = new HashSet<Comanda>();
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()){ //avoid SQL injection instead of using String concatenation 
				
				ResultSet rs = statement.executeQuery(query); //execute the Update in the DB
				while(rs.next())
				{
					Comanda comanda = new Comanda();
					comanda.setNum_comanda(rs.getInt("num_comanda"));
					comanda.setPreu_total(rs.getFloat("preu_total"));
					comanda.setData(rs.getDate("data").toLocalDate()); //convert LocalDate to Date
					comanda.setDni_client(client.getDni());
					comandes.add(comanda);
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
		return comandes;
	}	
	
}
