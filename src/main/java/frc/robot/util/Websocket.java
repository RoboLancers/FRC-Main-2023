package frc.robot.util;

import java.net.URI;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class Websocket extends WebSocketClient {

  public String recievedMessage = "";

  public Websocket(URI serverUri, Draft draft) {
    super(serverUri, draft);
  }

  public Websocket(URI klimelighturlstring) {
    super(klimelighturlstring);
  }

  public Websocket(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  public String getMessage() {
    return this.recievedMessage;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {

    System.out.println("opened connection");
    // if you plan to refuse connection based on ip or httpfields overload:
    // onWebsocketHandshakeReceivedAsClient
  }

  @Override
  public void onMessage(String message) {

    this.recievedMessage = message;

  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    // The close codes are documented in class org.java_websocket.framing.CloseFrame
    System.out.println(
        "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
            + reason);
  }

  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();
    // if the error is fatal then onClose will be called additionally
  }

}
