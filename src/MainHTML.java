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
            text = text + "<p><a href=\"" + outurl + "\"> - " + value.getName() + "</a></p>" + "\n";
        }
        post();
    }

    private void pre(){
        text = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js\"></script>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />\n" +
                "    <title>Texting</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "<h1>Local Files</h1>\n" +
                "<hr>\n";
    }

    private void post(){
        text = text + " " +
                        "<hr>\n" +
                "<form name=\"uploader\" enctype=\"multipart/form-data\" method=\"POST\">\n" +
                "        Отправить этот файл: <input name=\"userfile\" type=\"file\" />\n" +
                "        <button type=\"submit\" name=\"submit\">Загрузить</button>\n" +
                "    </form>\n" +
                "<script type=\"text/javascript\">\n" +
                "    $(\"form[name='uploader']\").submit(function(e) {\n" +
                "        var formData = new FormData($(this)[0]);\n" +
                "\n" +
                "        $.ajax({\n" +
                "            url: 'file.php',\n" +
                "            type: \"POST\",\n" +
                "            data: formData,\n" +
                "            async: false,\n" +
                "            success: function (msg) {\n" +
                "                alert(msg);\n" +
                "            },\n" +
                "            error: function(msg) {\n" +
                "                alert('Ошибка!');\n" +
                "            },\n" +
                "            cache: false,\n" +
                "            contentType: false,\n" +
                "            processData: false\n" +
                "        });\n" +
                "        e.preventDefault();\n" +
                "    });\n" +
                "    </script>" +
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
