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
    private static Grammar instance = null;

    public static Grammar getInstance() {
        if (Grammar.instance == null)
            Grammar.instance = new Grammar();
        return Grammar.instance;
    }

    public void init(String tagListFile, String rulesFile) throws Exception {
        this.tagList = new TagList();
        this.rules = new ArrayList<Rule>();

        readTags(tagListFile);
        readRules(rulesFile);
    }

    public TagList getTagList() {
        return this.tagList;
    }

    public ArrayList<Rule> getRules() {
        return this.rules;
    }

    private void readTags(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;


        while((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+", 0);
            this.tagList.add(parts[0], parts[1]);
        }

        br.close();
    }

    private void readRules(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;

        line = br.readLine();
        this.tagList.setStartTag(line.trim());

        while((line = br.readLine()) != null) {
            String[] parts = line.split("=");
            String[] leftParts = parts[0].trim().split("\\s+", 0);
            String[] rightParts = parts[1].trim().split("\\s+", 0);
            Rule rule = new Rule(new LinkedList<String>(Arrays.asList(leftParts)),
                    new LinkedList<String>(Arrays.asList(rightParts)));
            this.rules.add(rule);
        }

        br.close();
    }
}
