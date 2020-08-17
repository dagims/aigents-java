/*
 * MIT License
 * 
 * Copyright (c) 2018-2020 by Anton Kolonin, Aigents®
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.webstructor.xnd;


import java.util.ArrayList;
import net.webstructor.agent.Body;

public class XNDArxivPlugin {

	private Body body;

	private boolean isValidArxivPage;
	private String abstractURL;
	private String pdfURL;
	private String psURL;
	private String paperTitle;
	private String paperAuthors;
	private String paperAbstract;
	private String paperDate;
	private String paperSummary;

	public XNDArxivPlugin(Body _body) {
		body = _body;
	}

	public boolean processArticle(String url) {
		isValidArxivPage = false;
		abstractURL = url;

		if (url.contains("arxiv.org/abs") || url.contains("arxiv.org/pdf") || url.contains("arxiv.org/ps")) {
			isValidArxivPage = true;
			abstractURL = url.replaceFirst("arxiv.org/(pdf|ps)", "arxiv.org/abs").replace(".pdf", "");
			extractArxivProps(body.filecacher.checkCachedRaw(abstractURL));
		}

		return isValidArxivPage;
	}


	private void extractArxivProps(String source)
	{
		paperTitle = "";
		paperAuthors = "";
		paperAbstract = "";
		paperDate = "";
		ArrayList<String> prop = getMetaContByProp(source, "citation_title", false);
		if(prop.size() != 0)
			paperTitle = prop.get(0);

		prop = getMetaContByProp(source, "citation_author", false);
		if(prop.size() != 0)
			for (String s : prop)
				paperAuthors += (s + " : ");

		prop = getMetaContByProp(source, "og:description", true);
		if(prop.size() != 0)
			paperAbstract = prop.get(0);

		prop = getMetaContByProp(source, "citation_date", false);
		if(prop.size() != 0)
			paperDate = prop.get(0);
	}

	public String getArxivTitle() {
		return paperTitle;
	}

	public String getArxivAuthors() {
		return paperAuthors;
	}

	public String getArxivDate() {
		return paperDate;
	}

	public String getArxivAbstract() {
		return paperAbstract;
	}

	public String getArxivPDFUrl() {
		return pdfURL;
	}

	public boolean getArxivPageValidity() {
		return isValidArxivPage;
	}

	private ArrayList<String> getMetaContByProp(String source, String property, boolean prop) {
        ArrayList<String> mCont = new ArrayList<String>();
        String ptoken = prop ? "property=\"" : "name=\"";
        String ctoken = "content=\"";
        int mbpos = source.indexOf("<meta");
        int mepos = source.indexOf(">", mbpos);
        while(mbpos != -1 && mepos != -1) {
            String fmc = source.substring(mbpos, mepos);
            int mpbpos = fmc.indexOf(ptoken);
            int mpepos = fmc.indexOf("\"", mpbpos+ptoken.length());
            if(mpbpos != -1 && mpepos != -1) {
                if (fmc.substring(mpbpos+ptoken.length(), mpepos).equalsIgnoreCase(property)) {
                    int mcbpos = fmc.indexOf(ctoken);
                    int mcepos = fmc.indexOf("\"", mcbpos+ctoken.length());
                    if(mcbpos != -1 && mcepos != -1)
                        mCont.add(fmc.substring(mcbpos+ctoken.length(), mcepos));
                }
            }
            mbpos = source.indexOf("<meta", mepos+1);
            mepos = source.indexOf(">", mbpos);
        }
        return mCont;
    }

}