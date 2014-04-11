package analz;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class EarleyParser extends Parser {

    class EarleyRule extends Rule {
        private int start;
        private boolean term;
        private EarleyRule from;
        private EarleyRule by;

        public EarleyRule(LinkedList<String> left, LinkedList<String> right, boolean term) {
            super(left, right);
            this.start = 0;
            this.term = term;
            this.from = null;
            this.by = null;

        }

        public EarleyRule(LinkedList<String> left, LinkedList<String> right) {
            this(left, right, false);
        }

        public EarleyRule(Rule rule, boolean term) {
            super(rule);
            if (rule.getClass() == EarleyRule.class) {
                this.start = ((EarleyRule)rule).start;
                this.term = ((EarleyRule)rule).term;
            } else {
                this.start = 0;
                this.term = term;
            }
            this.from = null;
            this.by = null;
        }

        public EarleyRule(Rule rule) {
            this(rule, false);
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getStart() {
            return this.start;
        }

        public String getFirstRight() {
            return this.getRightTags().getFirst();
        }

        public String getFirstLeft() {
            return this.getLeftTags().getFirst();
        }

        public String getLastLeft() {
            return this.getLeftTags().getLast();
        }

        public EarleyRule from() {
            return this.from;
        }

        public void from(EarleyRule rule) {
            this.from = rule;
        }

        public EarleyRule by() {
            return this.by;
        }

        public void by(EarleyRule rule) {
            this.by = rule;
        }

        public void shift() {
            if (completed())
                return;
            this.getLeftTags().add(this.getRightTags().removeFirst());
        }

        public boolean completed() {
            return (this.getRightTags().isEmpty());
        }

        public boolean isTerm() {
            return this.term;
        }

        public boolean isRoot() {
            return this.equals(EarleyParser.this.root);
        }

        public boolean equals(EarleyRule rule) {
            LinkedList<String> thisAllTags = new LinkedList<String>(getLeftTags());
            LinkedList<String> ruleAllTags = new LinkedList<String>(rule.getLeftTags());

            thisAllTags.addAll(getRightTags());
            ruleAllTags.addAll(rule.getRightTags());

            return (thisAllTags.equals(ruleAllTags));
        }

        public String toString() {
            String res = "";
            res += this.start + " | ";
            for (String str : getLeftTags())
                res += str + " ";
            res += "= ";
            for (String str : getRightTags())
                res += str + " ";

            return res;
        }
    }

    private static EarleyParser instance = null;

    private ArrayList<ArrayList<EarleyRule>> table;
    private EarleyRule root;
    private HashMap<String, Integer> tagMap;

    public EarleyParser() {
        super();

        Grammar grammar = Grammar.getInstance();

        for (Rule rule : grammar.getRules()) {
            this.rules.add(new EarleyRule(rule));
        }

        LinkedList<String> left = new LinkedList<String>();
        LinkedList<String> right = new LinkedList<String>();
        left.add("ROOT");
        right.add(grammar.getTagList().getStartTag());
        this.root = new EarleyRule(left, right);

        this.tagMap = new HashMap<String, Integer>();
    }

    public static EarleyParser getInstance() {
        if (EarleyParser.instance == null)
            EarleyParser.instance = new EarleyParser();
        return EarleyParser.instance;
    }

    @Override
    public LinkedList<SyntaxNode> parse(Sentence sentence) {
        ArrayList<Rule> rules = sentence.getTermRules();

        for (Rule rule : rules) {
            this.rules.add(new EarleyRule(rule, true));
        }

        int numWords = sentence.numWords();

        this.table = new ArrayList<ArrayList<EarleyRule>>(numWords+1);
        for (int i = 0; i < numWords+1; i++)
            this.table.add(new ArrayList<EarleyRule>());

        this.table.get(0).add(this.root);

        __parse(sentence);

        for (int i = 0; i < this.table.size(); i++) {
            System.out.print(i + " || ");
            for (int j = 0; j < this.table.get(i).size(); j++) {
                EarleyRule rule = this.table.get(i).get(j);
                System.out.print(rule.getStart() + " ");
                for (String str : rule.getLeftTags())
                    System.out.print(str + " ");
                System.out.print(".");
                for (String str : rule.getRightTags())
                    System.out.print(str + " ");
                System.out.print("| ");
            }
            System.out.println("");
        }

        while (((EarleyRule)this.rules.get(this.rules.size()-1)).isTerm())
            this.rules.remove(this.rules.size()-1);

        return buildTrees();
    }

    private void __parse(Sentence sentence) {

        int pos;

        for (pos = 0; pos < this.table.size(); pos++) {
            ArrayList<EarleyRule> column = this.table.get(pos);
            int idx = 0;

            clearTagMap();
            while (idx < column.size()) {
                EarleyRule rule = column.get(idx);
                if (rule.completed())
                    complete(pos, rule);
                else if (pos < this.table.size()-1) {
                    if (rule.isTerm())
                        scan(sentence, rule);
                    else
                        predict(pos, rule);
                }
                idx++;
            }
        }
    }

    private void complete(int pos, EarleyRule rule) {
        ArrayList<EarleyRule> completedColumn = this.table.get(rule.getStart());
        ArrayList<EarleyRule> thisColumn = this.table.get(pos);
        for (EarleyRule prevRule : completedColumn) {
            if (!prevRule.completed() && !prevRule.isTerm() && prevRule.getFirstRight().equals(rule.getFirstLeft())) {
                EarleyRule transfer = new EarleyRule(prevRule);
                transfer.shift();
                transfer.from(prevRule);
                transfer.by(rule);
                thisColumn.add(transfer);
            }
        }
    }

    private void scan(Sentence sentence, EarleyRule rule) {
        int pos = rule.getStart();
        if (sentence.getWord(pos).getWord().equals(rule.getFirstRight()) &&
                sentence.getWord(pos).getTag().equals(rule.getFirstLeft())) {
            EarleyRule newRule = new EarleyRule(rule);
            newRule.shift();
            this.table.get(pos+1).add(newRule);
        }
    }

    private void predict(int pos, EarleyRule rule) {
        ArrayList<EarleyRule> column = this.table.get(pos);
        String tag = rule.getFirstRight();

        if (this.tagMap.get(tag) == null) {
            LinkedList<String> left = new LinkedList<String>();
            left.add(tag);
            ArrayList<Integer> idxs = Rule.findRuleLeft(left, this.rules);

            for (int i : idxs) {
                EarleyRule newRule = new EarleyRule(this.rules.get(i));
                newRule.setStart(pos);
                column.add(newRule);
            }

            this.tagMap.put(tag, 1);
        }
    }

    private LinkedList<SyntaxNode> buildTrees() {
        LinkedList<SyntaxNode> res = new LinkedList<SyntaxNode>();

        for (EarleyRule rule : this.table.get(this.table.size()-1))
            if (rule.isRoot()) {
                LinkedList<SyntaxNode> ret = __buildTrees(rule);
                res.add(ret.getFirst().getChildren().getFirst());
            }

        this.table = null;

        return res;
    }

    private LinkedList<SyntaxNode> __buildTrees(EarleyRule rule) {
        LinkedList<SyntaxNode> res = new LinkedList<SyntaxNode>();

        if (rule.isTerm() && rule.completed()) {
            SyntaxNode leaf = new SyntaxNode(rule.getLastLeft());
            SyntaxNode preLeaf = new SyntaxNode(rule.getFirstLeft());
            preLeaf.addChild(leaf);
            res.add(preLeaf);
            return res;
        }

        if (rule.getLeftTags().size() == 1)
            return res;

        LinkedList<SyntaxNode> subTrees = __buildTrees(rule.from());
        SyntaxNode subTreeRight = __buildTrees(rule.by()).getFirst();

        if (rule.completed()) {
            SyntaxNode subRoot = new SyntaxNode(rule.getLeftTags().getFirst());
            for (SyntaxNode subTree : subTrees)
                subRoot.addChild(subTree);
            subRoot.addChild(subTreeRight);
            res.add(subRoot);
        } else {
            res = subTrees;
            res.add(subTreeRight);
        }

        return res;
    }

    private void clearTagMap() {
        this.tagMap = new HashMap<String, Integer>();
    }
}
