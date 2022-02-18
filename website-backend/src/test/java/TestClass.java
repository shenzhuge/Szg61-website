import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestClass {
    String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

    @Test
    public void test() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1644058555399L)));
    }

    private void idConvertTest(){
        long temp;
        Random random = new Random();

        temp = random.nextLong() >>> 4; // temp:[0,2^60)

        System.out.println(temp);
        String id = idConvert(temp);
        System.out.println(id);
        System.out.println(idConvert(id));
    }

    private String idConvert(long id) {
        StringBuilder sb = new StringBuilder();
        long mask = 0x0FC0_0000_0000_0000L;

        for (int i = 54; i >= 0; i -= 6) {
            sb.append(characters.charAt((int) ((id & mask) >> i)));
            mask = mask >> 6;
        }

        return sb.toString();
    }

    private long idConvert(String id) {
        char[] c = id.toCharArray();
        long re = characters.indexOf(c[0]);
        for (int i = 1; i < c.length; i++) {
            re = re << 6;
            re += characters.indexOf(c[i]);
        }
        return re;
    }
}
