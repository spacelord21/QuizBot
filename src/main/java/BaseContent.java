import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseContent {

    public static void main(String[] args) {
        String[] hello = new String[100];
        String[] newWords = {"1", "2","3", "4"};
        hello = newWords;
        System.out.println(hello);
        System.out.println("__________");
        System.out.println(hello.length);
    }

}
