/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example2;

/**
 *
 * @author bmayr
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;
    /**
     * Used fot statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, ILinkHandler handler) {
        //ToDo: Implement Constructor
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        // ToDo: Implement
        // 1. if url not already visited, visit url with linkHandler
        // 2. get url and Parse Website
        // 3. extract all URLs and add url to list of urls which should be visited
        //    only if link is not empty and url has not been visited before
        // 4. If size of link handler equals 500 -> print time elapsed for statistics               
        if(!linkHandler.visited(url)) {
            try {
                URL urlLink = new URL(url);
                Parser p = new Parser(urlLink.openConnection()) ;
                NodeList list = p.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                List<String> urls = new ArrayList<>();

                for(int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    if(!extracted.getLink().isEmpty() && !linkHandler.visited(extracted.getLink())) {
                        urls.add(extracted.getLink());
                    }
                }
                linkHandler.addVisited(url);

                if(linkHandler.size() == 500) {
                    System.out.println("Time for size of link handler equals 500: " + (System.nanoTime() - t0) + " ns");
                }

                for(String s : urls) {
                    linkHandler.queueLink(s);
                }
            } catch (Exception e) {
                /*e.printStackTrace();*/
            }
        }
    }
}

