package analz;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sentence {

    class Word {
        private String word;
        private String tag;
        private int pos;

        public Word(String word, String tag, int pos) {
            this.word = word;
            this.tag = tag;
            this.pos = pos;
        }
    }

    private ArrayList<Word> words;

    public Sentence(String sentence) throws Exception {
        TagList tagList = Grammar.getInstance().getTagList();
        String[] rawWords = sentence.split("\\s+", 0);
        int pos = 0;

        this.words = new ArrayList<Word>();

        for (String word : rawWords) {
            String[] parts = word.split("/");
            String tag = tagList.check(parts[1]);
            if (tag == null)
                throw new Exception("Nhan tu loai " + parts[1] + " khong ton tai!");
            else if (tag.equals("Punc"))
                continue;
            this.words.add(new Word(parts[0], tag, pos));
            pos++;
        }
    }

    public ArrayList<Rule> getTermRules() {
        Iterator<Word> it = this.words.iterator();
        ArrayList<Rule> termRules = new ArrayList<Rule>();

        while (it.hasNext()) {
            Word word = it.next();
            LinkedList<String> left = new LinkedList<String>();
            LinkedList<String> right = new LinkedList<String>();
            left.add(word.tag);
            right.add(word.word);
            termRules.add(new Rule(left, right));
        }

        return termRules;
    }

    public int numWords() {
        return this.words.size();
    }

    public Word getWord(int pos) {
        return this.words.get(pos);
    }
}
