import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("system nasluchuje");

       
        while (true) {
            Socket s1 = server.accept();
            System.out.println("polaczony");


            BufferedReader browserReader = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            PrintWriter browserWriter = new PrintWriter(s1.getOutputStream(), true); //true-autoflush

            String userpath=browserReader.readLine();


            if (userpath.contains("KILLPROXY")){
                browserWriter.write("<html><h1>PROXY HAS BEEN KILLED</h1></html>");
                browserWriter.flush();
                browserWriter.close();
                break;
            }

            String mywebsite = userpath.substring(5).split(" ")[0];
       // GET /onet.pl HTTP/1.1
            try {
                URL url = new URL("https://www." + mywebsite);


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                
                BufferedReader websiteReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = websiteReader.readLine()) != null) {
                    content.append(inputLine);
                }
                websiteReader.close();
                //System.out.println(content.toString());
            //    String fullResponseBuilder = =con.getResponseCode()+ " " + con.getResponseMessage() +"\n" ; 
               

              
                browserWriter.write(content.toString());
                browserWriter.flush();
                browserWriter.close();
                s1.close();
                //Thread.sleep(300000);


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}