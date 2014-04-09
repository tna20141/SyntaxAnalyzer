package analz;

import java.util.*;
import java.util.regex.*;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class TagList {
    private HashMap<String, String> tags;
    private String startTag;

    public TagList() {
        this.tags = new HashMap<String, String>();
    }

    public void setStartTag(String tag) {
        this.startTag = tag;
    }

    public String getStartTag() {
        return this.startTag;
    }

    public void add(String detailedTag, String tag) {
        this.tags.put(detailedTag, tag);
    }

    public String check(String rawTag) {
        if (Pattern.matches("\\p{Punct}", rawTag))
            return "Punc";
        return this.tags.get(rawTag);
    }
}
