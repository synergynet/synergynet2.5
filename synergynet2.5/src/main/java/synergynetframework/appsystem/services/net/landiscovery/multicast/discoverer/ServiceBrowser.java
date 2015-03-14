/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast.discoverer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.landiscovery.multicast.EncoderDecoder;
import synergynetframework.appsystem.services.net.landiscovery.multicast.ServiceDiscoveryParams;

public final class ServiceBrowser implements Runnable, ServiceDiscoverySystem {
	protected boolean shouldRun = true;
	protected MulticastSocket socket;
	//	protected DatagramPacket queuedPacket;
	protected Queue<DatagramPacket> packetQueue = new ConcurrentLinkedQueue<DatagramPacket>();
	protected DatagramPacket receivedPacket;
	protected Thread myThread;
	protected Timer queryTimer;
	private ServiceDiscoveryParams params;
	
	protected Map<String,ServiceDescriptor> knownServices = new ConcurrentHashMap<String,ServiceDescriptor>();
	protected Map<String,Long> lastSeen = new ConcurrentHashMap<String,Long>();
	private Timer serviceRemovalTimer;

	public ServiceBrowser(ServiceDiscoveryParams params) throws IOException {
		this.params = params;		
		socket = new MulticastSocket(params.getMulticastPort());
		socket.joinGroup(params.getMulticastGroup());
		socket.setSoTimeout(ServiceDiscoveryParams.BROWSER_SOCKET_TIMEOUT);
	}


	public void start() {
		startLookup();		
	}

	public void stop() {
		stopListener();
		stopLookup();

	}

	private void startLookup() {
		if (queryTimer==null) {
			queryTimer = new Timer("QueryTimer");
			queryTimer.scheduleAtFixedRate(new QueryTimerTask(),0L,ServiceDiscoveryParams.BROWSER_QUERY_INTERVAL);
		}
		if(serviceRemovalTimer == null) {
			serviceRemovalTimer = new Timer("ServiceRemovalTimer");
			serviceRemovalTimer.scheduleAtFixedRate(new ServiceRemovalTimerTask(), 0L, ServiceDiscoveryParams.BROWSER_QUERY_INTERVAL);
		}
	}

//	public void startSingleLookup() {
//		if (myTimer==null) {
//			myTimer = new Timer("QueryTimer");
//			myTimer.schedule(new QueryTimerTask(), 0L);
//			myTimer=null;
//		}
//	}

	public void stopLookup() {
		if (queryTimer != null) {
			queryTimer.cancel();
			queryTimer=null;
		}
		if (serviceRemovalTimer != null) {
			serviceRemovalTimer.cancel();
			serviceRemovalTimer=null;
		}
	}

	protected void notifyServiceArrival(ServiceDescriptor descriptor) {
		for (ServiceDiscoveryListener l : listeners) {
			l.serviceAvailable(descriptor);			
		}
	}
	
	protected void notifyServiceRemoval(ServiceDescriptor descriptor) {
		for (ServiceDiscoveryListener l : listeners) {
			l.serviceRemoved(descriptor);			
		}
	}

	public void startListener() {
		if (myThread == null) {
			shouldRun = true;
			myThread = new Thread(this,"ServiceBrowser");
			myThread.start();
		}
	}

	public void stopListener() {
		if (myThread != null) {
			shouldRun = false;
			myThread.interrupt();
			myThread = null;
		}
	}

	public void run() {
		while (shouldRun) {
			/* listen (briefly) for a reply packet */
			try {
				byte[] buf = new byte[params.getDatagramLength()];
				receivedPacket = new DatagramPacket(buf, buf.length);
				socket.receive(receivedPacket); // note timeout in effect
				if (isReplyPacket()) {
					ServiceDescriptor descriptor;
					/* notes on behavior of descriptors.indexOf(...)
					 * ServiceDescriptor objects check for 'equals()'
					 * based only on the instanceName field. An update
					 * to a descriptor implies we should replace an
					 * entry if we already have one. (Instead of bothering
					 * with the details to determine new vs. update, just
					 * quickly replace any current descriptor.)
					 */
					descriptor = getReplyDescriptor();
					if (descriptor!=null) {
						if(!lastSeen.containsKey(getKey(descriptor))) {
							notifyServiceArrival(descriptor);
							knownServices.put(getKey(descriptor), descriptor);
						}
						lastSeen.put(getKey(descriptor), System.currentTimeMillis());
						receivedPacket = null;
					}

				}

			}
			catch (SocketTimeoutException ste) {
				/* ignored; this exception is by design to
				 * break the blocking from socket.receive */
			}
			catch (IOException ioe) {
				System.err.println("Unexpected exception: "+ioe);
				ioe.printStackTrace();
				/* resume operation */
			}

			sendQueuedPacket();

		}
	}

	protected synchronized void sendQueuedPacket() {
		if (packetQueue.size() < 1) { return; }
		try {
			for(DatagramPacket p : packetQueue) {
				socket.send(p);
			}
			packetQueue.clear();
		}
		catch (IOException ioe) {
			System.err.println("Unexpected exception: "+ioe);
			ioe.printStackTrace();
			/* resume operation */
		}
	}

	protected boolean isReplyPacket(){ 
		if (receivedPacket==null) {
			return false;
		}

		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char)0);
		if (pos>-1) {
			dataStr = dataStr.substring(0,pos);
		}

		/* REQUIRED TOKEN TO START */
		if (dataStr.startsWith(EncoderDecoder.REPLY_HEADER)) {
			return true;
		}

		return false;
	}

	protected ServiceDescriptor getReplyDescriptor() throws UnknownHostException {
		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char)0);
		if (pos>-1) {
			dataStr = dataStr.substring(0,pos);
		}
		dataStr = EncoderDecoder.decodeFromPacket(dataStr.substring(EncoderDecoder.REPLY_HEADER.length()));
		ServiceDescriptor sd = ServiceDescriptor.getServiceDescriptorFromStringRepresentation(dataStr);
		return sd;		
	}

	private class QueryTimerTask extends TimerTask {
		@Override
		public void run() {		
			for(String query : queries) {
				byte[] bytes = (EncoderDecoder.QUERY_HEADER + query).getBytes();
				DatagramPacket packet = new DatagramPacket(bytes,bytes.length);
				packet.setAddress(params.getMulticastGroup());
				packet.setPort(params.getMulticastPort());
				packetQueue.add(packet);
			}			
		}
	}
	
	private class ServiceRemovalTimerTask extends TimerTask {
		@Override
		public void run() {			
			for(String s : lastSeen.keySet()) {
				long elapsedTime = System.currentTimeMillis() - lastSeen.get(s);
				if(elapsedTime > ServiceDiscoveryParams.BROWSER_QUERY_INTERVAL * 4) {
					lastSeen.remove(s);
					ServiceDescriptor sd = knownServices.get(s);
					knownServices.remove(s);
					notifyServiceRemoval(sd);
				}
			}
			
		}
		
	}

	protected List<ServiceDiscoveryListener> listeners = new ArrayList<ServiceDiscoveryListener>();

	public void registerListener(ServiceDiscoveryListener l) {
		if(!listeners.contains(l)) listeners.add(l);		
	}

	public void removeListener(ServiceDiscoveryListener l) {
		listeners.remove(l);
	}

	//list of queries in type:name format
	Queue<String> queries = new ConcurrentLinkedQueue<String>();

	public void registerServiceForListening(String type, String name) {
		if(!queries.contains(type)) queries.add(getKey(type,name));		
	}

	private String getKey(ServiceDescriptor sd) {
		return getKey(sd.getServiceType(), sd.getServiceName());
	}
	
	private String getKey(String type, String name) {
		return type + ":" + name;
	}

}
