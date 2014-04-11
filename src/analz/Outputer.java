package analz;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class Outputer {

    public static void write(String file, LinkedList<String> results, boolean wrap, boolean append) throws Exception {
        File f = new File(file);
        FileOutputStream fs = new FileOutputStream(f, append);
        OutputStreamWriter ow = new OutputStreamWriter(fs);
        BufferedWriter bw = new BufferedWriter(ow);

        String wrapStr1 = "";
        String wrapStr2 = "";

        if (wrap) {
            wrapStr1 = "<s> ";
            wrapStr2 = " </s>";
        }

        for (String result : results) {
            bw.newLine();
            bw.write(wrapStr1);
            bw.write(result);
            bw.write(wrapStr2);
        }
        bw.newLine();

        bw.close();
    }

    public static void write(String file, String result, boolean wrap, boolean append) throws Exception {
        LinkedList<String> list = new LinkedList<String>();
        list.add(result);
        write(file, list, wrap, append);
    }

    public static void write(String file, SyntaxNode tree, boolean append) throws Exception {
        LinkedList<String> list = new LinkedList<String>();
        list.add(tree.toString());
        write(file, list, true, append);
    }

    public static void write(String file, Sentence s, boolean append) throws Exception {
        String raw = s.raw();
        write(file, raw, true, append);
    }

    public static LinkedList<String> toStringList(LinkedList<SyntaxNode> trees) {
        LinkedList<String> results = new LinkedList<String>();

        if (trees != null)
            for (SyntaxNode tree : trees) {
                results.add(tree.toString());
            }
        return results;
    }
}
