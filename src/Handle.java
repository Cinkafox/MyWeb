
import GetWorker.Get;
import GetWorker.ValueGet;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class Handle extends Thread {
    private final Socket socket;
    private String ldir = new File("").getAbsolutePath();
    private String absolutedir = ldir;
    private final int port;
    private final String index;


    public Handle(Socket socket,int port,String index) {
        this.socket = socket;
        this.port = port;
        this.index = index;
    }

    public void run(){
        try(InputStream input = socket.getInputStream(); OutputStream output = socket.getOutputStream()){
            st(output,input);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void st(OutputStream output, InputStream input) throws IOException{

        String inputurl = getURL(input)[1];
        ldir = ldir + "/" + inputurl;

        String type = "html";
        String text;
        if (new File(ldir).exists() && new File(ldir).isDirectory()) {
            MainHTML html = new MainHTML(inputurl,ldir,absolutedir,localFiles());
            File file = new File(ldir + "index.html.");
            if(file.exists()){
                byte[] b = readFile(file.getName());
                output.write((SetUp(b.length,type(type))).getBytes());
                output.write(b);
            }else {
                text = html.getHTML();
                output.write((SetUp(text.length(),type(type))+ text).getBytes());
            }
        }else if(!getType(inputurl).equalsIgnoreCase("") && new File(ldir).exists()){
            byte[] b = readFile(ldir);
            output.write(SetUp(b.length,type(getType(inputurl))).getBytes());
            output.write(b);
        }else {
            text = "NOT_FOUND";
            output.write((SetUp(text.length(),type(type))+ text).getBytes());
        }
        output.flush();
        input.close();
    }

    public String SetUp(int length,String type){
        return "HTTP/1.1 200 OK\r\n" +
                "Server: MyWeb\r\n" +
                "Content-Type: " + type +"\r\n" +
                "Content-length: "+ length +" \r\n" +
                "Connection: close\r\n\r\n";
    }

    public String[] getURL(InputStream in){
        Scanner scanner = new Scanner(in);
        if(scanner.hasNextLine()) {
            String[] GetPost = GetPost(scanner.nextLine());
            if(GetPost[0].equalsIgnoreCase("GET")){
                return new String[]{"1",GetPost[1]};
            }else{
                while(scanner.hasNextLine()){
                    System.out.println(scanner.nextLine());
                }
                return new String[]{"1","POST"};
            }
        }else{
            return new String[]{"1",""};
        }
    }

    public String[] GetPost(String in){
        String GetPost = spleeting(in,0);
        String out = removeFirstChars(spleeting(in,1),1);
        return new String[] {GetPost.trim(),out};
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
        return str.substring(chars).trim();
    }

    public String bFirstChar(String str,int chars){
        return removeLastChars(str,str.length()-(str.length()-chars)).trim();
    }

    public String acceptText(String in,String mon){
        if(spleeting(mon,0).trim().equalsIgnoreCase(in)) {
            return spleeting(mon, 1);
        }else {
            return "";
        }
    }

    public String spleeting(String in,int index){
        try {
            return in.split(" ")[index];
        }catch (ArrayIndexOutOfBoundsException e){
            return "";
        }
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
