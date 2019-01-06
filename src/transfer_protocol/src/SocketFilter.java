package transfer_protocol.src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SocketFilter extends DatagramSocket {
    private static final double PROBABILITY_PACKET_FAILURE = 0.05;
    private static final double PROBABILITY_PACKET_REJECT = 0.005;
    private static final double PROBABILITY_PACKET_DUPLICATE = 0.05;
    private boolean packetFailure;
    private boolean refusePacket;
    private boolean duplicatePacket;


    public SocketFilter() throws SocketException {
        super();
        this.packetFailure = false;
        this.refusePacket = false;
        this.duplicatePacket = false;
    }

    public SocketFilter(int port) throws SocketException {
        super(port);
        this.packetFailure = false;
        this.refusePacket = false;
        this.duplicatePacket = false;
    }


    @Override
    public void send(DatagramPacket p) throws IOException {
        int sendNumber = 1;
        DatagramPacket datagramPacket = p;
        if (packetFailure && decideToSendPacketFailure(PROBABILITY_PACKET_FAILURE)) {
            datagramPacket = new DatagramPacket(generatePacketFailure(p.getData()), p.getLength(), p.getAddress(), p.getPort());
            System.out.println("generated packet failure");
        }

        if (refusePacket && decideToSendPacketFailure(PROBABILITY_PACKET_REJECT)) {
            System.out.println("packet rejected");
            sendNumber = 0;
        }
        if (duplicatePacket && decideToSendPacketFailure(PROBABILITY_PACKET_DUPLICATE)) {
            System.out.println("packet duplicated");
            sendNumber = 2;
        }
        for (int i = 0; i < sendNumber; i++) {
            super.send(datagramPacket);
        }
    }

    /**
     * decides if a bit failure should be generated using a given probability
     **/
    public boolean decideToSendPacketFailure(double probability) {
        double random = Math.random();
        System.out.println("random: " + random);
        return  random < probability;
    }

    /**
     * generates a bit failure
     **/
    public byte[] generatePacketFailure(byte[] originalPacket) {
        //TODO Bitfehler an einer zufaelligen Stelle erzeugen
        final int failureBytePosition = 2;
        final int flipPosition = 5;
        byte[] failurePacket = originalPacket.clone();
        byte failureByte = originalPacket[failureBytePosition];
        failureByte = (byte) (failureByte ^ (1 << flipPosition));
        failurePacket[Math.abs(failureByte)] = failureByte;
        return failurePacket;
    }

}
