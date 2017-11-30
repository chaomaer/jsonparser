import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Parser {
    //在解析json的时候,其实就是一颗树
    HashMap<String, String> dict;
    HashMap<String, Frame> traces = new LinkedHashMap<>();
    private Frame gloenv;
    public Parser(Frame gloenv){
        this.gloenv = gloenv;
        dict = gloenv.getDict();
        startparser();
    }

    private void startparser() {
        //寻找gloenv中的每个key,val,如果是对象的话，再为他生成一个Frame
        Set<String> keys = dict.keySet();
        String val;
        for (String key : keys) {
            val = dict.get(key);
            if (val.startsWith("{")){
                traces.put(key,new Frame(val));
            }
        }
    }

    public void prinfFrame(){
        //*遍历每一个Frame，找到所有的路径
        //首先打印没有子Frame的节点
        for (String s : dict.keySet()) {
            System.out.println("key==>"+s);
            System.out.println("val==>"+dict.get(s));
            System.out.println();
        }
        //* 遍历含有Frame的东西
        for (String s : traces.keySet()) {
            Frame childFrame = traces.get(s);
            childFrame.prinfFrame();
        }
    }
}
