package analz;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Parser {
    protected ArrayList<Rule> rules;

    public Parser() {
        this.rules = new ArrayList<Rule>();
    }

    public abstract LinkedList<SyntaxNode> parse(Sentence sentence);
}
