package analz;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class Rule {
    private LinkedList<String> leftTags;
    private LinkedList<String> rightTags;

    public Rule() {
        this.leftTags = new LinkedList<String>();
        this.rightTags = new LinkedList<String>();
    }
    public Rule(LinkedList<String> left, LinkedList<String> right) {
        this.leftTags = left;
        this.rightTags = right;
    }

    public void setLeftTags(LinkedList<String> left) {
        this.leftTags = left;
    }

    public void setRightTags(LinkedList<String> right) {
        this.rightTags = right;
    }

    public LinkedList<String> getLeftTags() {
        return this.leftTags;
    }

    public LinkedList<String> getRightTags() {
        return this.rightTags;
    }

    public boolean equals(Rule rule) {
        return (this.leftTags.equals(rule.getLeftTags()) &&
                (this.rightTags.equals(rule.getRightTags())));
    }

    public boolean softEquals(Rule rule) {
        LinkedList<String> thisAllTags = new LinkedList<String>(this.leftTags);
        LinkedList<String> ruleAllTags = new LinkedList<String>(rule.getLeftTags());

        thisAllTags.addAll(this.rightTags);
        ruleAllTags.addAll(rule.getRightTags());

        return (thisAllTags.equals(ruleAllTags));
    }

    public boolean isSingular() {
        return (this.leftTags.size() == 1 && this.rightTags.size() == 1);
    }

    static public ArrayList<Integer> findRule(Rule rule, List<Rule> list) {
        Iterator<Rule> it = list.iterator();
        ArrayList<Integer> posList = new ArrayList<Integer>();
        int pos = 0;

        while (it.hasNext()) {
            Rule listRule = it.next();
            if (listRule.equals(rule))
                posList.add(pos);
            pos++;
        }

        return posList;
    }

    static public ArrayList<Integer> findRuleLeft(List<String> left, List<Rule> list) {
        Iterator<Rule> it = list.iterator();
        ArrayList<Integer> posList = new ArrayList<Integer>();
        int pos = 0;

        while (it.hasNext()) {
            Rule rule = it.next();
            if (rule.getLeftTags().equals(left))
                posList.add(pos);
            pos++;
        }

        return posList;
    }

    static public ArrayList<Integer> findRuleRight(List<String> right, List<Rule> list) {
        Iterator<Rule> it = list.iterator();
        ArrayList<Integer> posList = new ArrayList<Integer>();
        int pos = 0;

        while (it.hasNext()) {
            Rule rule = it.next();
            if (rule.getRightTags().equals(right))
                posList.add(pos);
            pos++;
        }

        return posList;
    }
}
