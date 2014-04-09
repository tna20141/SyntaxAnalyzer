package analz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class CYKParser extends Parser {

    class CYKCell extends Rule {
        private int start;
        private int mid;
        private int end;
        private boolean term;

        public CYKCell(String tag, String childTag1, String childTag2, int start, int mid, int end) {
            super();
            getLeftTags().add(tag);
            getRightTags().add(childTag1);
            if (childTag2 != null)
                getRightTags().add(childTag2);
            this.start = start;
            this.mid = mid;
            this.end = end;
            this.term = false;
        }

        public CYKCell(String tag, String childTag1, String childTag2, int start, int mid, int end, boolean term) {
            this(tag, childTag1, childTag2, start, mid, end);
            this.term = term;
        }

        public boolean isSingular() {
            return (getRightTags().size() == 1);
        }

        public boolean isTerminal() {
            return this.term;
        }
    }

    private ArrayList<Rule> singularRules;
    private ArrayList<ArrayList<ArrayList<Rule>>> table;
    private int[] singularRulesMap;

    public CYKParser() {
        super();

        Grammar grammar = Grammar.getInstance();

        this.singularRules = new ArrayList<Rule>();

        Integer argCount = 0;
        ArrayList<Rule> grammarRules = grammar.getRules();
        for (Rule rule : grammarRules) {
            LinkedList<String> left = (LinkedList<String>)rule.getLeftTags().clone();
            LinkedList<String> right = (LinkedList<String>)rule.getRightTags().clone();

            if (right.size() == 1) {
                this.singularRules.add(new Rule(left, right));
                continue;
            }

            while (right.size() > 2) {
                String argTag = "Arg" + argCount.toString();
                LinkedList<String> rightLeft = new LinkedList<String>();
                rightLeft.add(right.removeFirst());
                rightLeft.add(argTag);
                this.rules.add(new Rule(left, rightLeft));
                left = new LinkedList<String>();
                left.add(argTag);
                argCount++;
            }
            this.rules.add(new Rule(left, right));
        }

        this.singularRulesMap = new int[this.singularRules.size()];
        clearMap();
    }

    public LinkedList<SyntaxNode> parse(Sentence sentence) {
        ArrayList<Rule> termRules = sentence.getTermRules();
        int numTermRules = termRules.size();

        this.table = new ArrayList<ArrayList<ArrayList<Rule>>>(numTermRules);
        for (int i = 0; i < numTermRules; i++) {
            ArrayList<ArrayList<Rule>> arr = new ArrayList<ArrayList<Rule>>(numTermRules);
            for (int j = 0; j < numTermRules; j++)
                arr.add(new ArrayList<Rule>());
            this.table.add(arr);
        }

        for (int i = 0; i < numTermRules; i++) {
            Rule rule = termRules.get(i);
            this.table.get(i).get(i).add(new CYKCell(
                    rule.getLeftTags().getFirst(), rule.getRightTags().getFirst(), null, i, -1, i+1, true));
            scanSingularRules(i, i);
        }

        __parse();

        return buildTrees();
    }

    private void __parse() {
        int size = this.table.size();

        for (int width = 2; width <= size; width++)
            for (int start = 0; start <= size-width; start++) {
                int end = start+width;
                for (int mid = start+1; mid < end; mid++) {
                    for (Rule leftCell : this.table.get(start).get(mid-1))
                        for (Rule rightCell : this.table.get(mid).get(end-1)) {
                            LinkedList<String> right = new LinkedList<String>();
                            right.add(leftCell.getLeftTags().getFirst());
                            right.add(rightCell.getLeftTags().getFirst());

                            ArrayList<Integer> pos = Rule.findRuleRight(right, this.rules);
                            nextRule: for (int i : pos) {
                                Rule rule = this.rules.get(i);
                                ArrayList<Rule> cellList = this.table.get(start).get(end-1);
                                ArrayList<Integer> pos1 = Rule.findRule(rule, cellList);

                                for (int j : pos1)
                                    if (((CYKCell)(cellList.get(j))).mid == mid)
                                        continue nextRule;

                                this.table.get(start).get(end-1).add(new CYKCell(
                                        rule.getLeftTags().getFirst(), rule.getRightTags().getFirst(),
                                        rule.getRightTags().get(1), start, mid, end
                                ));
                            }
                        }
                }
                scanSingularRules(start, end-1);
            }
    }

    private void clearMap() {
        for (int i = 0; i < this.singularRulesMap.length; i++)
            this.singularRulesMap[i] = 0;
    }

    private void scanSingularRules(int i, int j) {
        ArrayList<Rule> cells = this.table.get(i).get(j);
        clearMap();
        for (int idx = 0; idx < cells.size(); idx++) {
            Rule cell = cells.get(idx);
            LinkedList<String> singularLeft = new LinkedList<String>();
            singularLeft.add(cell.getLeftTags().getFirst());
            ArrayList<Integer> pos = Rule.findRuleRight(singularLeft, this.singularRules);
            for (int k : pos) {
                Rule more = this.singularRules.get(k);
                if (this.singularRulesMap[k] == 1)
                    continue;
                this.table.get(i).get(j).add(new CYKCell(
                        more.getLeftTags().getFirst(), more.getRightTags().getFirst(), null, i, -1, j+1));
                this.singularRulesMap[k] = 1;
            }
        }
    }

    private LinkedList<SyntaxNode> buildTrees() {
        LinkedList<SyntaxNode> res = new LinkedList<SyntaxNode>();

        ArrayList<Rule> cellList = this.table.get(0).get(this.table.size()-1);
        LinkedList<String> left = new LinkedList<String>();
        left.add(Grammar.getInstance().getTagList().getStartTag());
        ArrayList<Integer> pos = Rule.findRuleLeft(left, cellList);

        for (int i : pos) {
            LinkedList<SyntaxNode> trees = __buildTrees((CYKCell)cellList.get(i));
            for (SyntaxNode tree : trees)
                res.add(tree);
        }

        if (res.size() > 0)
            return res;
        return null;
    }

    private LinkedList<SyntaxNode> __buildTrees(CYKCell cell) {
        LinkedList<SyntaxNode> res = new LinkedList<SyntaxNode>();

        if (cell.isSingular()) {
            if (cell.isTerminal()) {
                SyntaxNode leaf = new SyntaxNode(cell.getRightTags().getFirst());
                SyntaxNode preLeaf = new SyntaxNode(cell.getLeftTags().getFirst());
                preLeaf.addChild(leaf);
                res.add(preLeaf);

                return res;
            }
            ArrayList<Rule> cellList = this.table.get(cell.start).get(cell.end-1);
            LinkedList<String> left = new LinkedList<String>();
            left.add(cell.getRightTags().getFirst());
            ArrayList<Integer> pos = Rule.findRuleLeft(left, cellList);

            for (int i : pos) {
                LinkedList<SyntaxNode> subTrees = __buildTrees((CYKCell) cellList.get(i));
                for (SyntaxNode subTree : subTrees) {
                    SyntaxNode tree = new SyntaxNode(cell.getLeftTags().getFirst());
                    tree.addChild(subTree);
                    res.add(tree);
                }
            }
            return res;
        }

        ArrayList<Rule> cellList1 = this.table.get(cell.start).get(cell.mid-1);
        ArrayList<Rule> cellList2 = this.table.get(cell.mid).get(cell.end-1);
        LinkedList<String> left1 = new LinkedList<String>();
        LinkedList<String> left2 = new LinkedList<String>();
        left1.add(cell.getRightTags().getFirst());
        left2.add(cell.getRightTags().get(1));
        ArrayList<Integer> pos1 = Rule.findRuleLeft(left1, cellList1);
        ArrayList<Integer> pos2 = Rule.findRuleLeft(left2, cellList2);
        LinkedList<SyntaxNode> subTrees1 = new LinkedList<SyntaxNode>();
        LinkedList<SyntaxNode> subTrees2 = new LinkedList<SyntaxNode>();

        for (int i : pos1) {
            LinkedList<SyntaxNode> subSubTrees1 = __buildTrees((CYKCell) cellList1.get(i));
            for (SyntaxNode subSubTree1 : subSubTrees1)
                subTrees1.add(subSubTree1);
        }

        for (int j : pos2) {
            LinkedList<SyntaxNode> subSubTrees2 = __buildTrees((CYKCell) cellList2.get(j));
            for (SyntaxNode subSubTree2 : subSubTrees2)
                subTrees2.add(subSubTree2);
        }

        for (SyntaxNode subTree1 : subTrees1)
            for (SyntaxNode subTree2 : subTrees2) {
                SyntaxNode tree = new SyntaxNode(cell.getLeftTags().getFirst());
                if (isArgNode(subTree1)) {
                    for (SyntaxNode child1 : subTree1.getChildren())
                        tree.addChild(child1);
                } else
                    tree.addChild(subTree1);

                if (isArgNode(subTree2)) {
                    for (SyntaxNode child2 : subTree2.getChildren())
                        tree.addChild(child2);
                } else
                    tree.addChild(subTree2);

                res.add(tree);
            }

        return res;
    }

    private boolean isArgNode(SyntaxNode node) {
        return (node.getValue().indexOf("Arg") == 0);
    }
}
