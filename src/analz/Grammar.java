package analz;

import java.util.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class Grammar {
    private TagList tagList;
    private ArrayList<Rule> rules;
    private static int revision = -1;
    private static Grammar instance = null;

    public static Grammar getInstance() {
        if (Grammar.instance == null)
            Grammar.instance = new Grammar();
        return Grammar.instance;
    }

    public void init(String tagListFile, String rulesFile) throws Exception {
        if (Grammar.revision%2 == 0)
            Grammar.revision++;

        this.tagList = new TagList();
        this.rules = new ArrayList<Rule>();

        readTags(tagListFile);
        readRules(rulesFile);

        if (Grammar.revision%2 != 0)
            Grammar.revision++;
    }

    public TagList getTagList() {
        return this.tagList;
    }

    public ArrayList<Rule> getRules() {
        return this.rules;
    }

    public int getRevision() {
        return Grammar.revision;
    }

    public boolean initialized() {
        return (Grammar.revision%2 == 0);
    }

    private void readTags(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;


        while((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+", 0);
            if (parts.length != 2)
                throw new Exception("File nhãn từ loại không hợp lệ");
            this.tagList.add(parts[0], parts[1]);
        }

        br.close();
    }

    private void readRules(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;

        line = br.readLine();
        if (line.isEmpty())
            throw new Exception("Dòng đầu tiên của file luật phải là nhãn gốc");
        this.tagList.setStartTag(line.trim());

        while((line = br.readLine()) != null) {
            String[] parts = line.split("=");
            if (parts.length != 2)
                throw new Exception("File luật không hợp lệ");
            String[] leftParts = parts[0].trim().split("\\s+", 0);
            String[] rightParts = parts[1].trim().split("\\s+", 0);
            if (leftParts.length != 1 || rightParts.length == 0)
                throw new Exception("File luật không hợp lệ");

            Rule rule = new Rule(new LinkedList<String>(Arrays.asList(leftParts)),
                    new LinkedList<String>(Arrays.asList(rightParts)));
            this.rules.add(rule);
        }

        br.close();
    }
}
