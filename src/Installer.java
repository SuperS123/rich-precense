

import java.io.InputStream;

import javax.swing.JOptionPane;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class Installer extends Application {

	public static void main(String[] args) {
		System.out.println("Starting...");
		RPC.getInstance().start();
		RPC.getInstance().update("do not disturb", "or else");
		Application.launch(Installer.class);
	}

	private static Installer instance;
	private final WebView webView = new WebView();
	private static FeedbackHandler feedbackHandler = new FeedbackHandler();
	private int currentIndex;

	@Override
	public void start(Stage stage) throws Exception {
		instance = this;

		final VBox layout = new VBox();

		layout.getChildren().add(webView);
		stage.setScene(new Scene(layout));
		stage.setTitle("DiscordRPC Runner");
		stage.getIcons().setAll(new Image(getIcon()));
		stage.setResizable(false);
		stage.setWidth(1063);
		stage.setHeight(620);
		webView.setContextMenuEnabled(false);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				RPC.getInstance().start();
				RPC.getInstance().update("Do Not Disturb", "or block");
				webView.getEngine().load(ClassLoader.getSystemResource("index.html").toExternalForm());
				webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {

					if(newValue == Worker.State.SUCCEEDED) {
						((JSObject)webView.getEngine().executeScript("window")).setMember("feedback", feedbackHandler);
						if(webView.getEngine().getLocation().toLowerCase().contains("index.html")) {
							registerWorkers();
						}
					}

				});
			}
		});

		stage.show();


	}

	public static Installer getInstance() {
		return instance;
	}
	
	
	public static InputStream getIcon() {
		return ClassLoader.getSystemResourceAsStream("assets/icon.png");
	}

	private void registerWorkers() {
		moveForward();
		new Timeline(new KeyFrame[] { new KeyFrame(Duration.minutes(2), e -> {

			if(this.currentIndex > 100) {
				die(new Exception("Installer timed out."));
			}

		}, new KeyValue[0])}).play();

	}

	public void moveForward() {
		this.currentIndex += 34;
		if(currentIndex > 100) {

			new Timeline(
					new KeyFrame[] {
							new KeyFrame(
									Duration.seconds(4.0), 
									e -> Platform.runLater(
											() -> this.webView.getEngine().executeScript("javascript:finished()")
											), 
									new KeyValue[0]
									) 
					}
					)
			.play();

		}
	}

	public void die(Throwable err) {
		err.printStackTrace();
		JOptionPane.showMessageDialog(null, err.getMessage(), "An error occurred", JOptionPane.ERROR_MESSAGE);
		stopTheApplication();
	}

	public void stopTheApplication() {
		RPC.getInstance().shutdown();
		System.exit(0);
	}


}

