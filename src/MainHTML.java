import java.io.File;

public class MainHTML {
    private String text;

    public MainHTML(String inputurl, String ldir, String absolutedir, File[] file){

        pre();
        for (File value : file) {
            String outurl = removeFirstChars(ldir, absolutedir.length()) + "\\" + value.getName();
            if (inputurl.trim().equalsIgnoreCase("")) {
                outurl = value.getName();
            }
            text = text + "<h1><a href=\"" + outurl + "\">" + value.getName() + "</a>" + "\n";
        }
        post();
    }

    private void pre(){
        text = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />\n" +
                "    <title>Texting</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <main>";
    }

    private void post(){
        text = text + " </ul>\n" +
                "    <style>\n" +
                "      body {\n" +
                "        background: #222222;\n" +
                "      }\n" +
                "      main {\n" +
                "        background: #ffffff;\n" +
                "        padding: 10px;\n" +
                "        margin: 0 10px;\n" +
                "        border-radius: 10px;\n" +
                "      }\n" +
                "      main h1 {\n" +
                "        background: #585aca;\n" +
                "        padding: 10px;\n" +
                "        margin: 5px 10px;\n" +
                "        border-radius: 10px;\n" +
                "        color: #ffffff;\n" +
                "      }\n" +
                "main a {\n" +
                "        color: #ffffff;\n" +
                "      }" +
                "    </style>\n" +
                "  </body>\n" +
                "</html>\n";
    }

    public static String removeFirstChars(String str, int chars) {
        return str.substring(chars);
    }

    public String getHTML(){
        return text;
    }
}
