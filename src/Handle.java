
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Handle extends Thread {
    private final Socket socket;
    private String ldir = new File("").getAbsolutePath();
    private final String absolutedir = new File("").getAbsolutePath();
    private final int port;

    public Handle(Socket socket,int port) {
        this.socket = socket;
        this.port = port;
    }

    public void run(){
        try(InputStream input = socket.getInputStream(); OutputStream output = socket.getOutputStream()){
            String inputurl = getURL(input);
                ldir = ldir + "\\" + inputurl;
                System.out.println(socket.getInetAddress() + " search:" + inputurl);
            String type = "html";
            String text;
            if (new File(ldir).exists() && localFiles() != null && getType(inputurl).equalsIgnoreCase("")) {
                    MainHTML html = new MainHTML(inputurl,ldir,absolutedir,localFiles());
                    text = html.getHTML();
                    output.write((SetUp(text.length(),type(type))+ text).getBytes());
                }else if(!getType(inputurl).equalsIgnoreCase("") && new File(inputurl).exists()){
                    byte[] b = readFile(inputurl);
                    output.write(SetUp(b.length,type(getType(inputurl))).getBytes());
                    output.write(b);
                }else {
                    text = "NOT_FOUND";
                    output.write((SetUp(text.length(),type(type))+ text).getBytes());
                }
             output.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String SetUp(int length,String type){
        return "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: " + type +"\r\n" +
                "Content-length: "+ length +" \r\n" +
                "Connection: close\r\n\r\n";
    }

    public String getURL(InputStream in){
        Scanner scanner = new Scanner(in);
        if(scanner.hasNextLine()) {
            String tx = scanner.nextLine();
            return removeLastChar(tx.replaceAll("GET /","").replaceAll("HTTP/1.1",""));
        }else{
            return "";
        }
    }

    public File[] localFiles(){
        File file = new File(ldir);
        return file.listFiles();
    }

    public String getType(String in){
        try {
            return in.split("\\.")[1];
        }catch (ArrayIndexOutOfBoundsException e) {
            return  "";
        }
    }

    private String type(String type){
        String temp;
        switch (type.toLowerCase().trim()){
            case "html":
                temp = "text/" + type;
                break;
            case "png":
                temp = "image/" + type;
                break;
            case "jpeg":
                temp = "image/" + type;
                break;
            case "css":
                temp = "text/" + type;
                break;
            case "exe":
                temp = "application/octet-stream";
                break;
            case "jar":
                temp = "application/octet-stream";
                break;
            case "zip":
                temp = "application/octet-stream";
                break;
            case "rar":
                temp = "application/octet-stream";
                break;
            default:
                temp = "application/octet-stream";
                break;
        }
        return temp;
    }

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }
    public static String removeFirstChars(String str, int chars) {
        return str.substring(chars);
    }

    public static byte[] readFile(String file) throws IOException {

        File f = new File(file);

        // work only for 2GB file, because array index can only up to Integer.MAX

        byte[] buffer = new byte[(int)f.length()];

        FileInputStream is = new FileInputStream(file);

        is.read(buffer);

        is.close();

        return  buffer;

    }
}
