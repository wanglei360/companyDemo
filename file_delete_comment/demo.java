import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

class Demo{
  private static ArrayList<String> paths = new ArrayList<>();
    private static String path;
    private static String outputPath;

    public static void main(String[] args) {
        initPath(3);
        getFilePath(path);
        for (String inputPath : paths) {
            writeFile(handle(loadFile(inputPath)), outputPath);
        }
    }

    /**
     * @param i 0=保安
     *          1=领事
     *          2=保洁
     *          3=工程
     */
    private static void initPath(int i) {
        if (i == 0) {
            path = "/Users/wanglei/Documents/CompanyProject/newProject/NewManager/NewManager1.0.0/app/src/main/java/com/hogocloud/newmanager/modules";
            outputPath = "/Users/wanglei/Downloads/NewManager.txt";
        }else if (i == 1) {
            path = "/Users/wanglei/Documents/CompanyProject/newProject/newLink/hogoLink1.0.0/app/src/main/java/com/hogocloud/consular/modules";
            outputPath = "/Users/wanglei/Downloads/newLink.txt";
        }else if (i == 2) {
            path = "/Users/wanglei/Documents/CompanyProject/newProject/keepClean/keepClean1.0.0/app/src/main/java/com/hogocloud/keepclean/modules";
            outputPath = "/Users/wanglei/Downloads/keepClean.txt";
        }else if (i == 3) {
            path = "/Users/wanglei/Documents/CompanyProject/newProject/engineering/engineering1.0.0/app/src/main/java/com/hogocloud/engineering/modules";
            outputPath = "/Users/wanglei/Downloads/engineering.txt";
        }
    }

    private static String loadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String temp;
        String result = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                result = result + temp + "\r\n";
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    //删掉注释的方法
    private static String handle(String data) {
        StringBuilder sb = new StringBuilder();
        String[] split = data.split("\n");
        for (String s : split) {
            if (!s.contains("/*") && !s.contains("*/") && !s.contains("//") && !s.contains("*"))
                sb.append(s);
        }
        return sb.toString();
    }

    //把数据写进文件
    private static void writeFile(String data, String path) {
        try {
            if(!new File(path).exists()){
              new File(path).createNewFile();
            }
            FileWriter fw = new FileWriter(path, true);
            fw.write(data);
            fw.close();
        } catch (Exception ignored) {
        }
    }

    // 获取指定目录下的所有包含Fragment和Activity的文件路径
    private static void getFilePath(String sonPath) {
        File file = new File(sonPath);
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String s : list) {
                getFilePath(file.getAbsolutePath() + "/" + s);
            }
        } else {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.contains("Fragment") || absolutePath.contains("Activity") || absolutePath.contains("Repository")) {
                paths.add(absolutePath);
            }
        }
    }
}
