package org.hyperskill.app.android.code.presentation.highlight.prettify;

import org.hyperskill.app.android.code.presentation.highlight.prettify.parser.Job;
import org.hyperskill.app.android.code.presentation.highlight.prettify.parser.Prettify;
import org.hyperskill.app.android.code.presentation.highlight.syntaxhighlight.Parser;
import org.hyperskill.app.android.code.presentation.highlight.syntaxhighlight.PrettifyParseResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The prettify parser for syntax highlight.
 * @author Chan Wai Shing <cws1989@gmail.com>
 */
public class PrettifyParser implements Parser {

    /**
     * The prettify parser.
     */
    protected Prettify prettify;

    /**
     * Constructor.
     */
    public PrettifyParser() {
        prettify = new Prettify();
    }

    @Override
    public List<PrettifyParseResult> parse(String fileExtension, String content) {
        Job job = new Job(0, content);
        prettify.langHandlerForExtension(fileExtension, content).decorate(job);
        List<Object> decorations = job.getDecorations();


        List<PrettifyParseResult> returnList = new ArrayList<>();

        // apply style according to the style list
        for (int i = 0, iEnd = decorations.size(); i < iEnd; i += 2) {
            int endPos = i + 2 < iEnd ? (Integer) decorations.get(i + 2) : content.length();
            int startPos = (Integer) decorations.get(i);
            returnList.add(new PrettifyParseResult(startPos, endPos - startPos, Collections.singletonList((String) decorations.get(i + 1))));
        }

        return returnList;
    }
}
