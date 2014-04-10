package analz;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class Outputer {

    public static void write(String file, LinkedList<String> results, boolean append) throws Exception {
        File file = new File(file);
        FileOutputStream fs = new FileOutputStream(file, append);
        OutputStreamWriter ow = new OutputStreamWriter(fs);
        BufferedWriter writer = new BufferedWriter(ow);

        for (String result : results) {
            bw.newLine();
            bw.write(result);
        }
        bw.newLine();

        bw.close();
    }

    public static void write(String file, LinkedList<SyntaxNode> results, boolean append) throws Exception {
        LinkedList<String> results = toString(trees);
        write(file, results, append);
    }

    public static LinkedList<String> toString(LinkedList<SyntaxNode> trees) {
        LinkedList<String> results = new LinkedList<String>();
        for (SyntaxNode tree : trees)
            results.add(toString(tree));
        return results;
    }

    private static String toString(SyntaxNode tree) {
        return null;
    }

}
