/*
 * The MIT License
 *
 * Copyright 2016 Christian Spiewok <christian.spiewok@apps4people.ch>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbake.parser;

import ch.apps4people.cdemodel.CDEXmlContent;
import ch.apps4people.cdemodel.ContentXmlModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedMapConverter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

/**
 *
 * @author Christian Spiewok <christian.spiewok@apps4people.ch>
 */
public class XMLParser implements ParserEngine {

    @Override
    public Map<String, Object> parse(Configuration config, File file, String contentPath) {

        XStream xstream = new XStream();
        NamedMapConverter converter = new NamedMapConverter(xstream.getMapper(), "entry", "key", String.class, null, String.class, true, false, xstream.getConverterLookup()) {
            @Override
            public boolean canConvert(Class type) {
                if (type.equals(Map.class)) {
                    return true;
                } else {
                    return super.canConvert(type);
                }
            }
        };

        xstream.registerConverter(converter);

        xstream.processAnnotations(ContentXmlModel.class);
        ContentXmlModel model = (ContentXmlModel) xstream.fromXML(file);

        Map<String, Object> renderedContent = this.getRenderedContent(model.getContent());
        return renderedContent;
    }



    private Map<String, Object> getRenderedContent(List<CDEXmlContent> unrenderedContent) {

        String[] mdExts = new String[]{"HARDWRAPS", "AUTOLINKS", "FENCED_CODE_BLOCKS", "DEFINITIONS"};

        int extensions = Extensions.NONE;
        if (mdExts.length > 0) {
            for (int index = 0; index < mdExts.length; index++) {
                String ext = mdExts[index];
                if (ext.startsWith("-")) {
                    ext = ext.substring(1);
                    extensions = removeExtension(extensions, extensionFor(ext));
                } else {
                    if (ext.startsWith("+")) {
                        ext = ext.substring(1);
                    }
                    extensions = addExtension(extensions, extensionFor(ext));
                }
            }
        }

        long maxParsingTime = PegDownProcessor.DEFAULT_MAX_PARSING_TIME;

        PegDownProcessor pegdownProcessor = new PegDownProcessor(extensions, maxParsingTime);

        Map<String, Object> renderedContent = new HashMap<>();
        
        for (CDEXmlContent content : unrenderedContent) {

            if(!StringUtils.isBlank(content.getMarkup()) && content.getMarkup().equals("ml")) {
                renderedContent.put(content.getKey(), pegdownProcessor.markdownToHtml(content.getContent()));            
            } else {
                renderedContent.put(content.getKey(), content.getContent());
            }
            

        }

        return renderedContent;
    }



    private int extensionFor(String name) {
        int extension = Extensions.NONE;
        if (name.equals("HARDWRAPS")) {
            extension = Extensions.HARDWRAPS;
        } else if (name.equals("AUTOLINKS")) {
            extension = Extensions.AUTOLINKS;
        } else if (name.equals("FENCED_CODE_BLOCKS")) {
            extension = Extensions.FENCED_CODE_BLOCKS;
        } else if (name.equals("DEFINITIONS")) {
            extension = Extensions.DEFINITIONS;
        } else if (name.equals("ABBREVIATIONS")) {
            extension = Extensions.ABBREVIATIONS;
        } else if (name.equals("QUOTES")) {
            extension = Extensions.QUOTES;
        } else if (name.equals("SMARTS")) {
            extension = Extensions.SMARTS;
        } else if (name.equals("SMARTYPANTS")) {
            extension = Extensions.SMARTYPANTS;
        } else if (name.equals("SUPPRESS_ALL_HTML")) {
            extension = Extensions.SUPPRESS_ALL_HTML;
        } else if (name.equals("SUPPRESS_HTML_BLOCKS")) {
            extension = Extensions.SUPPRESS_HTML_BLOCKS;
        } else if (name.equals("SUPPRESS_INLINE_HTML")) {
            extension = Extensions.SUPPRESS_INLINE_HTML;
        } else if (name.equals("TABLES")) {
            extension = Extensions.TABLES;
        } else if (name.equals("WIKILINKS")) {
            extension = Extensions.WIKILINKS;
            //} else if (name.equals("ANCHORLINKS")) { // not available in pegdown-1.4.2
            //    extension = Extensions.ANCHORLINKS;
        } else if (name.equals("STRIKETHROUGH")) {
            extension = Extensions.STRIKETHROUGH;
        } else if (name.equals("ALL")) {
            extension = Extensions.ALL;
        }
        return extension;
    }



    private int addExtension(int previousExtensions, int additionalExtension) {
        return previousExtensions | additionalExtension;
    }



    private int removeExtension(int previousExtensions, int unwantedExtension) {
        return previousExtensions & (~unwantedExtension);
    }

}
