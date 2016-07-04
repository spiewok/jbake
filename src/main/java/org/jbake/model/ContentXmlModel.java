/*
 * /\|^|^_\~+||^[-()|^|_[- 
 *         
 * Copyright 2016 apps4people
 * All Rights Reserved
 *
 * ****************************************************************************
 * * Unauthorized copying of this file, via any medium is strictly prohibited *
 * * Proprietary and confidential                                             *
 * ****************************************************************************
 * 
 * Written by Christian Spiewok <christian.spiewok@apps4people.ch>, 2016
 * 
 */
package org.jbake.model;

import ch.apps4people.cdemodel.CDEXmlContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian Spiewok <christian.spiewok@apps4people.ch>
 */
@XStreamAlias("a4p-content")
public class ContentXmlModel {
    
    
    private List<CDEXmlContent> content;
    
    public ContentXmlModel() {
        this.content = new ArrayList<>();
    }
    
    /**
     * @return the content
     */
    public List<CDEXmlContent> getContent() {
        return content;
    }



    /**
     * @param content the content to set
     */
    public void setContent(List<CDEXmlContent> content) {
        this.content = content;
    }
}

