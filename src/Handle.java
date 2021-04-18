
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Handle extends Thread {
    private final Socket socket;
    private String ldir = new File("").getAbsolutePath();
    private final String absolutedir = new File("").getAbsolutePath();
    private final int port;
    private final String index;

    public Handle(Socket socket,int port,String index) {
        this.socket = socket;
        this.port = port;
        this.index = index;
    }

    public void run(){
        try(InputStream input = socket.getInputStream(); OutputStream output = socket.getOutputStream()){
            File file = new File(index);
            String inputurl = getURL(input);
                ldir = ldir + "\\" + inputurl;
                System.out.println(socket.getInetAddress() + " :" + inputurl);
            String type = "html";
            String text;
            if (new File(ldir).exists() && localFiles() != null && getType(inputurl).equalsIgnoreCase("")) {
                    MainHTML html = new MainHTML(inputurl,ldir,absolutedir,localFiles());
                    if(file.exists()){
                        byte[] b = readFile(file.getName());
                        output.write((SetUp(b.length,type(type))).getBytes());
                        output.write(b);
                    }else {
                        text = html.getHTML();
                        output.write((SetUp(text.length(),type(type))+ text).getBytes());
                    }
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
            String type = tx.substring(0,tx.length()-(tx.length()-4));
            if(type.trim().equalsIgnoreCase("GET")) {
                //if(tx.split("\\?")[1].equalsIgnoreCase("")){

                //}
                return removeLastChar(tx.replaceAll("GET /", "").replaceAll("HTTP/1.1", ""));
            }else{
                String temp = "";
                while(scanner.hasNextLine()){
                    temp = temp + scanner.nextLine() + " [] \n";
                }
                return "";
            }
        }else{
            return "";
        }
    }

    public String getIN(InputStream in){
        Scanner scanner = new Scanner(in);
        if(scanner.hasNextLine()) {
            String text = scanner.nextLine();
            String type = text.substring(0);
            System.out.println(type);
            return text;
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
            String[] temp = in.split("\\.");
            return temp[temp.length-1];
        }catch (ArrayIndexOutOfBoundsException e) {
            return  "";
        }
    }

    private String type(String type){
        String temp;
        switch (type.toLowerCase().trim()){
            case "html":
            case "css":
                temp = "text/" + type;
                break;
            case "png":
            case "jpeg":
                temp = "image/" + type;
                break;
            case "txt":
                temp = "text/html";
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
    public void loadFile(String name,byte[] bt) throws IOException {
        File file = new File(name);
        if(!file.exists()){
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bt);
            fileOutputStream.close();
        }else{
            System.out.println("File is already exist!");
        }
    }
}
