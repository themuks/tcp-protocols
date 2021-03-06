package model;

import java.io.IOException;

public class RLoginProtocolClient extends TcpClient {

    private boolean isWorking = true;
    private Response response;

    @Override
    public void start() throws IOException {
        super.start();
    }

    @Override
    public void stop() throws IOException {
        super.stop();
        response.setStopped();
        isWorking = false;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void initializeConnection(String name1, String name2, String terminalType, int speed, String password) throws IOException {
        String msg = "\0" + name1 + "\0" + name2 + "\0" + terminalType + "/" + speed + "\0";
        out.write(msg.getBytes());
        byte[] buffer = new byte[256];
        in.read(buffer);
        out.write(password.getBytes());
        response = new Response();
        response.setParent(this);
        response.start();
    }

    public void sendMessage(String msg) throws IOException {
        out.write(msg.getBytes());
    }

    private class Response extends Thread {

        private boolean isStopped = false;
        private TcpClient parent;

        public void setStopped() {
            isStopped = true;
        }

        public void setParent(TcpClient tcpClient) {
            parent = tcpClient;
        }

        @Override
        public void run() {
            while (!isStopped) {
                try {
                    byte[] buffer = new byte[1024];
                    in.read(buffer);
                    if (new String(buffer).trim().equalsIgnoreCase("!stop")) {
                        parent.stop();
                    }
                    System.out.println("Server response: " + new String(buffer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
