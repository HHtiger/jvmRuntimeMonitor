import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 17-4-27.
 */
public class TestNTP {

    public static void main(String[] args) throws IOException {
        try {
            NTPUDPClient timeClient = new NTPUDPClient();
            String timeServerUrl = "202.120.2.101";
            InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);
            for(int i=0;i<10;i++){
                TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
                TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
                System.out.println(timeStamp.getTime());
                TimeUnit.SECONDS.sleep(1);
            }
//            Date date = timeStamp.getDate();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            System.out.println(dateFormat.format(date));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
