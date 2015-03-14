package synergynetframework.mtinput.luminja;

import java.util.concurrent.Callable;

import de.evoluce.multitouch.adapter.java.BlobJ;
import de.evoluce.multitouch.adapter.java.JavaAdapter;

public class GetBlobsTask implements Callable<BlobJ[]> {

	private JavaAdapter ja;

	public GetBlobsTask(JavaAdapter ja) {
		this.ja = ja;
	}

	@Override
	public BlobJ[] call() throws Exception {
		return ja.getBlobsOfNextFrame().mBlobs;
	}

}
