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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

		Document jspdoc = Jsoup.parse(source);
		Elements jspelem = jspdoc.getElementsByAttributeValue("name", "citation_title");
		paperTitle = jspelem.attr("content");
		jspelem = jspdoc.getElementsByAttributeValue("name", "citation_author");
		for(int _idx = 0; _idx < jspelem.size(); _idx++) {
			paperAuthors += jspelem.get(_idx).attr("content") + " - ";
		}
		if(paperAuthors.length() > 0)
			paperAuthors = paperAuthors.substring(0, paperAuthors.length()-4);

		jspelem = jspdoc.getElementsByTag("blockquote");
		paperAbstract = jspelem.text();

		jspelem = jspdoc.getElementsByAttributeValue("name", "citation_date");
		paperDate = jspelem.attr("content");
	}

	public boolean isValid() {
		return isValidArxivPage;
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

	public String getArxivAbsUrl() {
		return abstractURL;
	}

	public boolean getArxivPageValidity() {
		return isValidArxivPage;
	}
}