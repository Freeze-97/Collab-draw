import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.UnknownHostException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/* 
 * Client side 
 * This is a re-modified Client class from an earlier course 
 */

public class Client {
	public static final String DEFAULT_ADDRESS = "localhost";
	public static final int DEFAULT_PORT = 10000;
	
	Socket socket;
	
	public Client(String address, int port) {
		connect(address, port);
	}
	
	public Client() {
		connect(DEFAULT_ADDRESS, DEFAULT_PORT);
	}
	
	protected boolean connect(String address, int port) {
			if(!(socket != null && !socket.isClosed())) {
				try {
					socket = new Socket(address, port);
					if(socket.isConnected()) {
						System.out.println("Connected to address: " 
					+ socket.getInetAddress() + " and port: " 
								+ socket.getPort());
					}
				} catch (UnknownHostException e) {
					System.err.println(e.getMessage());
					System.err.println("UnknownHostException when trying new Socket() "
							+ "in Client class");
				} catch (IOException e) {
					System.err.println(e.getMessage());
					System.err.println("IOException when trying new Socket() "
							+ "in Client class");
				} 
			}
			
			return socket != null && !socket.isClosed();
	}
	
	protected boolean disconnect() {
		try  {
			// If the socket is not closed, close it
			if(socket != null) {
				socket.close();
				System.out.println("Disconnected from host!");
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			System.err.println("IOException when trying socket.close()"
					+ "in Client class");
		}
		return socket == null || !socket.isConnected();
	}
	
	// This function sends the data using an Observable
	protected void send(Object object) {
		Observable.just(socket)
		.flatMap(s -> Observable.just(s) // flatMap returns new Observable
		.subscribeOn(Schedulers.io()) // Thread pool
		.map(Socket::getOutputStream) // Get the output
		.map(ObjectOutputStream::new))
		.subscribe(e -> e.writeObject(object), // Writes the object here
				error -> { 
					System.err.println("Error with sending data");
				
				disconnect(); // If there is an error, disconnect
				});
	}
	
	// This function will receive data using an Observable
	protected Observable<Object> receive() {
		Observable<Object> stream =
				Observable.just(socket)
				.flatMap(s -> Observable.just(s)
				.subscribeOn(Schedulers.io()) // Thread pool
				.repeat() // This will repeat infinitely since there is no limit for how many objects we can receive
				.map(Socket::getInputStream) // Get the input
				.map(ObjectInputStream::new)
				.map(ObjectInputStream::readObject));
		
		return stream;
	}
}
	
