import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.ReplaySubject;

// Some inspirations from an old Server class made in a previous course

// Server side
public class Server {
	// Variable
	ServerSocket serverSocket;
	
	/* Tried using Observable but could not make it work.
	 * I Googled and found ReplaySubject as an example.
	 * Would not mind feedback as I'm not sure exactly how this works
	 * except for that it tries again.
	 */
	private ReplaySubject<Socket> allClients;
	
	// Constructor with port number
	public Server(int port) {
		allClients = ReplaySubject.create();
		
		// Set port for server which clients need to know to connect
		setPort(port);
	}
	
	// Observer for new clients connecting, 
	// Every client is represented as an Observer
	private Observer<Socket> clientConnected = new Observer<Socket>() {

		@Override
		public void onSubscribe(@NonNull Disposable d) {}

		@Override
		public void onNext(@NonNull Socket socket) {
			System.out.println("Client has connected! (" + 
		socket.getRemoteSocketAddress() + ")");
			
			// Add next client
			allClients.onNext(socket);
			
			  // Get all shapes from the stream
			  Observable.<Object>create(e -> {
				    while (serverSocket != null && !serverSocket.isClosed()
				    		&& !socket.isClosed()) {
				    	try {
				    		e.onNext(new ObjectInputStream
				    			(socket.getInputStream())
								.readObject());
				    	} catch(Exception error) {
				    		System.err.println("Error trying e.onNext() in Server class");
				    		e.onError(error);
				    		
				    		// Close socket if there is an error
				    		socket.close();
				    	}
				    }
				    e.onComplete();
			  })
			    .subscribeOn(Schedulers.io())
				.flatMap(o->Observable.just(o))
				.subscribe(objectObserver);
		}

		@Override
		public void onError(@NonNull Throwable e) {
			System.err.println("Error with connecting the client");
			System.err.println(e.getMessage());
		}

		@Override
		public void onComplete() {}
		
	};
	
	// Observer for objects/shapes via the clients
	private Observer<Object> objectObserver =  new Observer<Object>() {
		@Override
		public void onSubscribe(@NonNull Disposable d) {}

		@Override
		public void onNext(@NonNull Object object) {
			allClients.filter(s -> s != null && !s.isClosed()) // Filter so only active ones are left
			.map(Socket::getOutputStream)
			.map(ObjectOutputStream::new)
			.subscribe(s -> s.writeObject(object));
		}

		@Override
		public void onError(@NonNull Throwable e) {
			System.err.println(e.getMessage());
			System.err.println("Error in ObjectObserver, Server class");
		}

		@Override
		public void onComplete() {}
	};
	
	public boolean setPort(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started on port " + serverSocket.getLocalPort());
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.err.println("Could not create new ServerSocket()");
		}
		
		return serverSocket != null && !serverSocket.isClosed();
	}
	
	public boolean startServer() {
		Observable.<Socket> create(e->{
			while(serverSocket != null && !serverSocket.isClosed() 
					&& !e.isDisposed()) {
				e.onNext(serverSocket.accept());
			}
			e.onComplete();
		})
		.subscribeOn(Schedulers.io()) // Using thread pool
		.window(1) // This returns an Observable that emits windows of items collected
		.subscribe(e->e.subscribe(clientConnected));
		
		return serverSocket != null && !serverSocket.isClosed();
	}
}
