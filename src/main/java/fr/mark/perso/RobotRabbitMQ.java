package fr.mark.perso;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class RobotRabbitMQ implements Runnable {
	
	private ConnectionFactory _factory;
    private Connection _connection;
    private Channel _channel;
    private static final long  DRONE_ID = 1l;

	public void run() {
		init();
		try {
			simulateBatteryChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	private void init() {
		_factory = new ConnectionFactory();

        _factory.setHost(Endpoints.RABBITMQ_SERVERADRESS);
        _factory.setUsername(Endpoints.RABBITMQ_USERNAME);
        _factory.setPassword(Endpoints.RABBITMQ_USERPASSWORD);
        _factory.setPort(Endpoints.RABBITMQ_SERVERPORT);



        try {
            _connection = _factory.newConnection();
            if(_connection != null) {
                _channel = _connection.createChannel();
                _channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
	}
	
	private void simulateBatteryChange() throws Exception {
		int battery = 100;
		double latitude = 48.114978;
		double longitude = -1.637892;
		String message1 = "{\"timestamp\":1521799522,\"id_drone\":1,\"id_mission\":0,\"battery_level\":";
		String message2 = ",\"position\":{\"latitude\":";
		String message3 = ",\"altitude\":10.0,\"longitude\":";
		String message4 = "},\"orientation\":{\"roll\":0.343,\"pitch\":8.780,\"yaw\":-9.343},\"velocity\":{\"x\":-0.343,\"y\":1.780,\"z\":-0.343}}";
		while(true) {
			String message = message1 + battery + message2 + latitude + message3 + longitude + message4;
			sendMessage(message);
			battery = (battery - 1) % 101;
			latitude = latitude - 0.000010;
			longitude = longitude - 0.000010;
			Thread.sleep(500);
			
		}
	}
	
	private void sendMessage(String message) throws Exception {
        _channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, "drone.info."+DRONE_ID, null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.info."+DRONE_ID + "':'" + message + "'");
	}
	
}
