package netgloo;

import netgloo.search.updateData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
    updateData ud=new updateData();
    ud.DBhelper();
  }

}
