package activitat_1_2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Comanda {

	private int num_comanda;
	private float preu_total;
	private LocalDate data;
	private String dni_client;
	
	public Comanda(){};
	
	public Comanda(int num_comanda, float preu_total, LocalDate data, String dni_client) {
		super();
		this.num_comanda = num_comanda;
		this.preu_total = preu_total;
		this.data = data;
		this.dni_client = dni_client;
	}

	public int getNum_comanda() {
		return num_comanda;
	}

	public void setNum_comanda(int num_comanda) {
		this.num_comanda = num_comanda;
	}

	public float getPreu_total() {
		return preu_total;
	}

	public void setPreu_total(float preu_total) {
		this.preu_total = preu_total;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getDni_client() {
		return dni_client;
	}

	public void setDni_client(String dni_client) {
		this.dni_client = dni_client;
	}
	
	
	//when nwe Politic is created request user to enter its data
	public void setNewComanda(Client client)
	{
		boolean goOn = false;
		while(!goOn)
		{
			try{
				this.setNum_comanda( Integer.parseInt(Menu.enterData("Introdueix el num_producte:")));
				goOn = true;
			}
			catch(Exception e)
			{
				System.out.println("'num_producte' must be an Integer (p.e. 36396996)");
			}
		}
		
		goOn = false;
		while(!goOn)
		{
			try{
				this.setPreu_total( Float.parseFloat(Menu.enterData("Introdueix el preu_total:")));
				goOn = true;
			}
			catch(Exception e)
			{
				System.out.println("'preu_total' must be a decimal number (p.e. 3.14)");
			}
		}
		
		goOn = false;
		while(!goOn)
		{
			try{
				String enteredDate = Menu.enterData("Introdueix la data (p.e. 03/03/2001):");
				this.setData(LocalDate.parse(enteredDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				//this.setData(LocalDate.now()); //get and set current LocalDate
				goOn = true;
			}
			catch(Exception e)
			{
				System.out.println("'data' must be a decimal number (p.e. 03/03/2001)");
			}
		}	
		
		this.setDni_client(client.getDni()); //
	}

	@Override
	public String toString() {
		return "Comanda [num_comanda=" + num_comanda + ", preu_total=" + preu_total + ", data=" + data + ", dni_client="
				+ dni_client + "]";
	}	
}
