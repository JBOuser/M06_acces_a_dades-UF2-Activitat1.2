package activitat_1_2;

public class Client {

	private String dni;
	private String nom;
	private boolean premium;
	
	public Client(){};
	
	public Client(String dni, String nom, boolean premium) {
		super();
		this.dni = dni;
		this.nom = nom;
		this.premium = premium;
	}	
	
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public boolean isPremium() {
		return premium;
	}
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	
	
	//when nwe Politic is created request user to enter its data
	public void setNewClient()
	{
		
		this.setDni(Menu.enterData("Introdueix el dni:"));
		this.setNom(Menu.enterData("Introdueix el nom:"));
		
		boolean goOn = false;
		while(!goOn){
			
			String premiumBoolean = Menu.enterData("Introdueix 'true' o 'false' ('true':premium, 'false':no premium)");
			if(premiumBoolean.matches("true|false"))
			{
				this.setPremium(Boolean.parseBoolean(premiumBoolean));
				goOn = true;
			}
		}
	}

	@Override
	public String toString() {
		return "Client [dni=" + dni + ", nom=" + nom + ", premium=" + premium + "]";
	}	
	
}
