package synergynetframework.mtinput.luminja;

import java.util.concurrent.Callable;

import de.evoluce.multitouch.adapter.java.BlobJ;
import de.evoluce.multitouch.adapter.java.JavaAdapter;

/**
 * The Class GetBlobsTask.
 */
public class GetBlobsTask implements Callable<BlobJ[]> {
	
	/** The ja. */
	private JavaAdapter ja;
	
	/**
	 * Instantiates a new gets the blobs task.
	 *
	 * @param ja
	 *            the ja
	 */
	public GetBlobsTask(JavaAdapter ja) {
		this.ja = ja;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public BlobJ[] call() throws Exception {
		return ja.getBlobsOfNextFrame().mBlobs;
	}
	
}
