/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast.responder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.multicast.EncoderDecoder;
import synergynetframework.appsystem.services.net.landiscovery.multicast.ServiceDiscoveryParams;

/**
 * Responds to service queries.
 * @author dcs0ah1
 *
 */
public final class ServiceResponder implements Runnable, ServiceAnnounceSystem {
	protected boolean shouldRun = true;
	protected MulticastSocket socket;
	protected DatagramPacket queuedPacket;
	protected Thread myThread;
	private ServiceDiscoveryParams params;

	public ServiceResponder(ServiceDiscoveryParams params) throws IOException {
		this.params = params;
		socket = new MulticastSocket(params.getMulticastPort());
		socket.joinGroup(params.getMulticastGroup());
		socket.setSoTimeout(ServiceDiscoveryParams.RESPONDER_SOCKET_TIMEOUT);
	}

	public void startResponder() {
		if (myThread == null || !myThread.isAlive()) {
			shouldRun = true;
			myThread = new Thread(this,"ServiceResponder");
			myThread.setDaemon(true);
			myThread.start();
		}
	}

	public void stopResponder() {
		if (myThread != null && myThread.isAlive()) {
			shouldRun = false;
			myThread.interrupt();
		}
	}

	protected void sendQueuedPacket() {
		if (queuedPacket==null) { return; }
		try {
			socket.send(queuedPacket);
			queuedPacket = null;
		}
		catch (IOException ioe) {
			System.err.println("Unexpected exception: "+ioe);
			ioe.printStackTrace();
			/* resume operation */
		}
	}

	public void run() {
		while (shouldRun) {
			byte[] buf = new byte[params.getDatagramLength()];
			DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(receivedPacket); // note a timeout in effect
				if (isQueryPacket(receivedPacket)) {
					String query = getQuery(receivedPacket);					
					if(isRegistered(query)) {
						DatagramPacket replyPacket = getReplyPacket(query);
						queuedPacket = replyPacket;
						sendQueuedPacket();
					}else{
						
					}
				}
			}catch (SocketTimeoutException ste) {
				/* ignored; this exception is by design to
				 * break the blocking from socket.receive */
			}catch (IOException ioe) {
				System.err.println("Unexpected exception: "+ioe);
				ioe.printStackTrace();
			}
		}
	}

	private String getQuery(DatagramPacket receivedPacket) {
		if (receivedPacket==null) {
			return null;
		}

		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char)0);
		if (pos>-1) {
			dataStr = dataStr.substring(0,pos);
		}

		if (dataStr.startsWith(EncoderDecoder.QUERY_HEADER)) {
			return dataStr.substring(EncoderDecoder.QUERY_HEADER.length());
		}
		return null;
	}


	protected boolean isQueryPacket(DatagramPacket receivedPacket) {
		if (receivedPacket==null) {
			return false;
		}

		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char)0);
		if (pos>-1) {
			dataStr = dataStr.substring(0,pos);
		}

		if (dataStr.startsWith(EncoderDecoder.QUERY_HEADER)) {
			return true;
		}

		return false;
	}

	protected DatagramPacket getReplyPacket(String query) {
		
		StringBuffer buf = new StringBuffer();
		try {
			buf.append(EncoderDecoder.REPLY_HEADER);
			ServiceDescriptor sd = respondTo.get(query);
			buf.append(EncoderDecoder.encodeForPacket(sd.getStringRepresentation()));
		}
		catch (NullPointerException npe) {
			System.err.println("Unexpected exception: "+npe);
			npe.printStackTrace();
			return null;
		}
		byte[] bytes = buf.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(bytes,bytes.length);
		packet.setAddress(params.getMulticastGroup());
		packet.setPort(params.getMulticastPort());

		return packet;
	}


	public void addShutdownHandler() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() { stopResponder(); }
		});
	}

	// mapping of type:name pairs to service descriptors
	Map<String,ServiceDescriptor> respondTo = new HashMap<String,ServiceDescriptor>();

	public boolean isRegistered(String query) {
		return respondTo.keySet().contains(query);
	}
	
	public void registerService(ServiceDescriptor sd) {
		String key = getKey(sd);
		if(!respondTo.keySet().contains(key)) respondTo.put(key, sd);		
	}

	private String getKey(ServiceDescriptor sd) {
		return sd.getServiceType() + ":" + sd.getServiceName();
	}

	public void unregisterService(ServiceDescriptor sd) {
		respondTo.remove(getKey(sd));		
	}

	@Override
	public void stop() {
		stopResponder();
		
	}


}
