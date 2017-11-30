import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class Frame {
    //用来保存每个层次的frame结构
    //在构造这个json的时候，我需要一个解析类
    Spliter spliter;
    //通过key值找到下一个对象，也就是下一个Frame
    HashMap<String, String> dict = new LinkedHashMap<>();
    HashMap<String, Frame> traces = new LinkedHashMap<>();

    public Frame(String content){
        spliter = new Spliter(content);
        constructFrame();
    }
    public HashMap getDict(){
        return dict;
    }
    private void constructFrame() {
        String key;
        String val;
        while (true){
            //首先解析出来的是一个字符串
            key = spliter.split();
            //然后是一个：
            spliter.split();
            //然后是一个val
            val = spliter.split();
            if (val.startsWith("{"))
            {
                //说明这是一个对象
                Frame frame = new Frame(val);
                traces.put(key,frame);
            }
            dict.put(key,val);
            //然后是一个逗号，或者是结束了
            if (spliter.isParseOver()){
                break;
            }else {
                //去掉逗号
                spliter.split();
            }
        }
    }

    public void prinfFrame(){
        System.out.println("======================================================================================");
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
        System.out.println("======================================================================================");
    }
}
