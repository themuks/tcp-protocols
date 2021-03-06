package model;

import java.io.IOException;

public class TimeProtocolClient extends TcpClient{

    private final int DEFAULT_PORT = 37;

    @Override
    public void start() throws IOException {
        super.start(DEFAULT_PORT);
    }

    public long getTime() throws IOException {
        byte[] buffer = new byte[128];
        in.read(buffer);
        return Long.parseLong(new String(buffer).trim()) + 2208988800L;
    }
}
