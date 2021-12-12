/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example2;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

/**
 *
 * @author bmayr
 */

// Recursive Action for forkJoinFramework from Java7

public class LinkFinderAction extends RecursiveAction {

    private String url;
    private ILinkHandler cr;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, ILinkHandler cr) {
        // ToDo: Implement Constructor
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {
        // ToDo:
        // 1. if crawler has not visited url yet:
        // 2. Create new list of recursiveActions
        // 3. Parse url
        // 4. extract all links from url
        // 5. add new Action for each sublink
        // 6. if size of crawler exceeds 500 -> print elapsed time for statistics
        // -> Do not forget to call ìnvokeAll on the actions!
        if(!cr.visited(url)) {
            try {
                List<RecursiveAction> actions = new ArrayList<>();
                URL urlLink = new URL(url);
                Parser p = new Parser(urlLink.openConnection());
                NodeList list = p.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));

                for(int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    if(!extracted.extractLink().isEmpty() && !cr.visited(extracted.getLink())) {
                        actions.add(new LinkFinderAction(extracted.extractLink(), cr));
                    }
                }
                cr.addVisited(url);

                if(cr.size() > 500) {
                    System.out.println("Time for size of crawler exceeds 500: " + (System.nanoTime() - t0) + " ns");
                }

                invokeAll(actions);
            } catch (Exception e) {
                /*e.printStackTrace();*/
            }
        }
    }
}

