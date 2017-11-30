import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spliter {
    // 用来进行括号匹配的栈
    private List<String> stacklist = new ArrayList<>();
    private String jsonstr;
    private String remainstr;
    public Spliter(String jsonstr){
        this.jsonstr = jsonstr;
        this.remainstr = jsonstr;
        initjson();
    }
    public Spliter(File file){
        this.jsonstr = readData(file);
        this.remainstr = jsonstr;
        initjson();
    }
    private static String readData(File file) {
        BufferedReader bf;
        StringBuilder sb = new StringBuilder();
        try {
            bf = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bf.readLine())!=null){
                sb.append(line);
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public boolean isParseOver(){
        return remainstr.length()==0;
    }
    public String split(){
        if (isParseOver()){
            return "$";
        }
        char povit = remainstr.charAt(0);
        switch (povit){
            case '\"':
                return parseStr();
            case ':':
                remainstr = remainstr.substring(1).trim();
                return ":";
            case ',':
                remainstr = remainstr.substring(1).trim();
                return ",";
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            default:
                return parsenum();

        }
    }
    public HashMap start(){
        HashMap<String, String> retmap = new HashMap<>();
        String key;
        String val;
        while (true){
            //首先解析出来的是一个字符串
            key = split();
            //然后是一个：
            split();
            //然后是一个val
            val = split();
            retmap.put(key,val);
            //然后是一个逗号，或者是结束了
            if (remainstr.length() == 0){
                break;
            }else {
                //去掉逗号
                split();
            }
        }
        return retmap;
    }
    private String parsenum() {
        StringBuilder sb = new StringBuilder();
        int index = remainstr.length();
        for (int i = 0; i < remainstr.length(); i++) {
            if (Character.isDigit(remainstr.charAt(i))){
                sb.append(remainstr.charAt(i));
            }else {
                index = i;
                break;
            }
        }
        remainstr = remainstr.substring(index);
        return sb.toString().trim();
    }

    /**
     * 这三个函数都是分割出需要的东西，然后更行remainStr的值
     * @return
     */

    private String parseArray() {
        stacklist.add("[");
        int index = -1;
        for (int i = 1; i < remainstr.length(); i++) {
            if (remainstr.charAt(i)=='['){
                stacklist.add("[");
            }else if (remainstr.charAt(i)==']'){
                stacklist.remove(stacklist.size()-1);
                if (stacklist.isEmpty()){
                    index = i;
                    break;
                }
            }
        }
        String ret = remainstr.substring(0,index+1);
        remainstr = remainstr.substring(index+1).trim();
        return ret;
    }

    private String parseObject() {
        stacklist.add("{");
        int index = -1;
        for (int i = 1; i < remainstr.length(); i++) {
            if (remainstr.charAt(i)=='{'){
                stacklist.add("{");
            }else if (remainstr.charAt(i)=='}'){
                stacklist.remove(stacklist.size()-1);
                if (stacklist.isEmpty()){
                    index = i;
                    break;
                }
            }
        }
        String ret = remainstr.substring(0,index+1);
        remainstr = remainstr.substring(index+1).trim();
        return ret;
    }

    private String parseStr() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        boolean flag = false;
        int index = -1;
        for (int i = 1; i <remainstr.length() ; i++) {
            if (flag){
                sb.append(remainstr.charAt(i));
                flag = false;
            }else {
                if (remainstr.charAt(i)=='\\'){
                    flag = true;
                }else {
                    if (remainstr.charAt(i)=='"'){
                        index = i;
                        break;
                    }
                    sb.append(remainstr.charAt(i));
                }
            }
        }
        sb.append('"');
        remainstr = remainstr.substring(index+1).trim();
        return sb.toString();
    }

    private void initjson() {
       remainstr = remainstr.substring(1,remainstr.length()-1).trim();
    }
}
