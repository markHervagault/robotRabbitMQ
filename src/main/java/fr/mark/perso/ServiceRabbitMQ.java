package fr.mark.perso;



public class ServiceRabbitMQ {
	

	public static void main(String[] args) {
		String manuel = "Programme d'envoi de message test sur RabbitMQ.\n"
				+ "Arguments possible : \n"
				+ "- battery : envoi sur drone.info.1 les d�tails d'un drone fake dont la batterie change continuellement.";
		
		if(args.length==0) {
			System.out.println("Arguments n�cessaires");
			System.out.println(manuel);
			System.exit(0);
		}
		
		if(!args[0].equals("battery")) {
			System.out.println("Arguments non-reconnus");
			System.out.println(manuel);
			System.exit(0);
		}	
		
		Thread robot = new Thread(new RobotRabbitMQ());
		robot.start();

	}

}