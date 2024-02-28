package nl.youngcapital.backend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountTests {

    @Test
    public void doeEenTestje() {
        int a = 5;
        int b = 5;
        Hotel h = new Hotel();
        h.setCountry("Germany");

        Assertions.assertEquals(h.getCountry() , "Germany");
    }
}
