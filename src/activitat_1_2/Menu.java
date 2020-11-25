package activitat_1_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Menu {
	
	private final String url = "jdbc:postgresql://localhost:5432/jdbcapp";
	private final String user = "jdbcapp_admin";
	private final String password = "jdbcapp_admin";	
	private final String classForName = "org.postgresql.Driver";
	
	private HashMap<Integer, Client> clients = new HashMap<Integer, Client>(); //unique clients
	private HashMap<String, HashSet<Comanda>> comandes = new HashMap<String, HashSet<Comanda>>(); //unique clients and unique comandes
	private boolean dataLoadedOnMemory = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Menu menu = new Menu();
		menu = menu.menu(menu);
	}

	
	public Menu menu (Menu menu)
	{
		System.out.println("--MENU--");
		System.out.println("1.Creació del model (Creació de les taules 'CLIENTS' i 'COMANDES')");
		System.out.println("2.Recuperació de les dades de la BBDD (Mostrar els clients i les seves comandes)");
		System.out.println("3.Emmagatzemat de dades a la BBDD (Eliminar les BBDD prèvies i afegir la actual)");
		System.out.println("4.Alta d'un nou client (s'afegeix a la llista clients pero no es guarda a la BBDD)");
		System.out.println("5.Alta d'una nova comanda (mostrar els clients disponibles, escollir un i demanar les dades de la nova comanda)");		
		System.out.println("6.Mostrar per pantalla les dades d'un client");
		System.out.println("7.Mostrar totes les dades 'Client-Comandes'");
		System.out.println("0.Exit");
		String option = enterData("Choose an option:"); //static method
		
		ClientDB clientDB = null;
		ComandaDB comandaDB = null;
		
		if(isInteger(option))
		{
			switch(Integer.parseInt(option))
			{
				case 1:
					//Create table 'clients' if not exist
					clientDB = new ClientDB();
					clientDB.createTable();
					
					//Create table 'comandes' if not exist
					comandaDB = new ComandaDB();
					comandaDB.createTable();
					
					System.out.println("");
					
					return menu.menu(menu);
					
				case 2:
					clientDB = new ClientDB();
					comandaDB = new ComandaDB();

					//get all Clients from DB
					List<Client> clientsTmp = clientDB.getClients();
					if(0 < clientsTmp.size())
					{
						int index = 0;
						for(Client client2 : clientsTmp)
						{
							//add all Clients to 'clients'
							clients.put(index, client2);
							
							//get all Comandes from DB
							HashSet<Comanda> comandes2 = comandaDB.getAllComandes(client2);
							if(0 < comandes2.size())
							{
								//add all client's Comandes if exist to 'comandes'
								comandes.put(client2.getDni(),comandes2);
							}
							index++;
						}
								
						//show if data as has been loaded
						setDataLoadedOnMemory(true);
						
						System.out.println("Data loaded on memory");						
					}
					else
					{
						System.out.println("No data in memory");
					}

					System.out.println("");
					
					return menu.menu(menu);
				
					
				case 3:
					if(isDataLoadedOnMemory())
					{
						//delete all Comandes
						comandaDB = new ComandaDB();
						comandaDB.clearTable();
						//delete all Clients
						clientDB = new ClientDB();
						clientDB.clearTable();	
						
						//iterate over all saved Clients and add all of them to the table Clients
						for(Map.Entry<Integer,Client> client3 : clients.entrySet())
						{
							//SQL Client insertion
							clientDB.insertClient(client3.getValue());
							
							//iterate over all saved Clients and add all of them to the table Clients
							HashSet<Comanda> comandesClient3 = comandes.getOrDefault(client3.getValue().getDni(), null);
							if(comandesClient3 != null)
							{
								for(Comanda comandaTmp : comandesClient3)
								{
									//SQL Comanda insertion
									comandaDB.insertComanda(client3.getValue(), comandaTmp);
								}
							}
						}						
					}
					else
					{
						System.out.println("The DB has not been loaded (choose option 2)");
					}
					System.out.println("");
					
					return menu.menu(menu);		
				
					
				case 4:
					if(isDataLoadedOnMemory())
					{
						Client client4 = new Client();
						client4.setNewClient();
						
						boolean clientFound4 = false;
						for(Map.Entry<Integer,Client> client5: clients.entrySet())
						{
							if(client5.getValue().getDni().matches(client4.getDni()))
							{
								clientFound4 = true;
							}
						}					
						
						if(!clientFound4)
						{
							//if not found add the client at the end of 'clients'
							clients.put(clients.size(), client4);
							System.out.println("Client '"+client4.getDni()+"' added");
						}
						else
						{
							System.out.println("Client '"+client4.getDni()+"' already exists");
							
						}						
					}
					else
					{
						System.out.println("The DB has not been loaded (choose option 2)");
					}					
					System.out.println("");
					
					return menu.menu(menu);
					
					
				case 5:
					if(isDataLoadedOnMemory())
					{
						clientDB = new ClientDB();
						
						//print available clients
						System.out.println("--- Available CLIENTS ---");
						for(Map.Entry<Integer,Client> client5: clients.entrySet())
						{
							System.out.println(client5.getKey()+"."
									+client5.getValue().getDni()
									+" - "+client5.getValue().getNom());
						}
						
						//request the number asociated to a client of previous list 
						String client_id_string = enterData("Escull un client pel número:");
						try{
							int client_id5 = Integer.parseInt(client_id_string);
							
							//get client if exists. Otherwise null
							Client client5 = clients.getOrDefault(client_id5, null);
							if(client5!=null)
							{
								boolean isExistingComanda = false;
								
								System.out.println("Client '"+client5.getDni()+"' seleccionat.");
								System.out.println("Introdueix les dades de la nova comanda:");
								Comanda newComanda = new Comanda();
								
								//set the data for the new Comanda
								newComanda.setNewComanda(client5);

								//check if new Comanda already exists among all Comandes
								for(Map.Entry<String,HashSet<Comanda>> comandes5 : comandes.entrySet())
								{
									HashSet<Comanda> clientComandes = comandes5.getValue();
									for(Comanda clientComanda5 : clientComandes)
									{
										if(clientComanda5.getNum_comanda() == newComanda.getNum_comanda())
										{
											isExistingComanda = true;
										}
									}
								}	
								
								if(isExistingComanda==false)
								{
									//get all Comandes from chosen client
									HashSet<Comanda> comandesClient = comandes.getOrDefault(newComanda.getDni_client(), null);

									//add the new Comanda to the existing client's Comandes
									if(comandesClient != null)
									{
										comandesClient.add(newComanda);
										System.out.println("Comanda '"+newComanda.getNum_comanda()+"' afegida al client '"+client5.getDni()+"'");
									}
									
									//create a new list(HashMap) to the chosen client and append it the new Comanda
									else
									{
										comandesClient = new HashSet<Comanda>();
										comandesClient.add(newComanda);
										comandes.put(client5.getDni(), comandesClient);
										System.out.println("Comanda '"+newComanda.getNum_comanda()+"' afegida al client '"+client5.getDni()+"'");
									}									
								}
								else
								{
									System.out.println("Comanda with num_comanda '"+newComanda.getNum_comanda()+"' already exists");
								}
								
							}
							else
							{
								System.out.println("Client not found ("+client_id5+")");
							}							
								
						}
						catch(Exception e)
						{
							System.out.println("'client_id' must be an integer number ("+e+")");
						}						
					}
					else
					{
						System.out.println("The DB has not been loaded (choose option 2)");
					}					
					System.out.println("");
					
					return menu.menu(menu);					
				
					
				case 6:
					
					if(isDataLoadedOnMemory())
					{
						clientDB = new ClientDB();
						//print available clients
						System.out.println("--- Available CLIENTS ---");
						for(Map.Entry<Integer,Client> client6: clients.entrySet())
						{
							System.out.println(client6.getKey()+"."
									+client6.getValue().getDni()
									+" - "+client6.getValue().getNom());
						}
						
						//request the number asociated to a client of previous list 
						String client_id_string = enterData("Escull un client pel número davant del punt:");
						
						try{
							int client_id6 = Integer.parseInt(client_id_string);
							
							if(clients.containsKey(client_id6))
							{
								Client client6 = clients.getOrDefault(client_id6, null);
								if(client6 != null)
								{
									System.out.println("Client: "+client6.getDni());
									
									//SQL return all comandes from the specified client						
									for(Comanda comanda : comandes.getOrDefault(client6.getDni(), null))
									{
										if(comanda != null)
										{
											System.out.println("Comanda: "+comanda.toString());
										}
									}										
								}
							}
							else
							{
								System.out.println("Client '"+client_id6+"' not found");
							}
						}
						catch(Exception e)
						{
							System.out.println("'client_id' must be an integer number ("+e+")");
						}
					}
					else
					{
						System.out.println("The DB has not been loaded (choose option 2)");
					}	
					System.out.println("");
					
					return menu.menu(menu);						
					

				case 7:
					
					if(isDataLoadedOnMemory())
					{
						//print all clients data
						for(Map.Entry<Integer, Client> client7 : clients.entrySet()) 
						{
							System.out.println("Client: "+client7.getValue().getDni());

							//print all Comandes of each client if there are
							HashSet<Comanda> comandesTmp = comandes.getOrDefault(client7.getValue().getDni(), null);
							if(comandesTmp!=null)
							{
								for(Comanda comanda : comandesTmp)
								{
									System.out.println("Comanda: "+comanda.toString());
								}							
							}
						}					
					}
					else
					{
						System.out.println("The DB has not been loaded (choose option 2)");
					}	
					System.out.println("");					
					
					return menu.menu(menu);					
					
				case 0:
					System.out.println("Closing...");
					return menu;
					
				default:
					System.out.println("Option not available '"+option+"'");
			}
		}
		else
		{
			System.out.println("Wrong option '"+option+"'");
		}
		System.out.println("");
		
		return menu.menu(menu);
	}
	
	//request data
	public static String enterData(String text)
	{
		System.out.println(text);
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			text = br.readLine();
		}
		catch(Exception e)
		{
			System.out.println("Read ERROR ("+e+")");
		}
		return text;
	}
	
	
	//request data
	public static boolean isInteger(String text)
	{
		boolean isInt = false;
		
		try{
			Integer.parseInt(text);
			isInt = true;
		}
		catch(Exception e)
		{
			//System.out.println("Entered value is not a Integer ("+e+")");
		}
		
		return isInt;
	}	
	

	public Connection getConnection(String driver)
	{
		Connection connection = null;
		
		try {
			Class.forName(driver); //requires try-catch
			//get data from JDBC_Postgres class
			connection = DriverManager.getConnection(
					this.getUrl(),
					this.getUser(),
					this.getPassword());	
		}
		catch(Exception e)
		{
			System.out.println("PostgreSQL Driver -- ERROR ("+e+")");			
		}
		return connection;
	}


	public String getUrl() {
		return url;
	}


	public String getUser() {
		return user;
	}


	public String getPassword() {
		return password;
	}


	public String getClassForName() {
		return classForName;
	}


	public boolean isDataLoadedOnMemory() {
		return dataLoadedOnMemory;
	}


	public void setDataLoadedOnMemory(boolean dataLoadedOnMemory) {
		this.dataLoadedOnMemory = dataLoadedOnMemory;
	}		
	
}
