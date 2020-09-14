

public class FeedbackHandler {

	//Called from the javascript
	public void close() {
		Installer.getInstance().stopTheApplication();
	}
	
}
