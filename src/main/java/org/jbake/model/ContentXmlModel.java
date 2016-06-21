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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christian Spiewok <christian.spiewok@apps4people.ch>
 */
@XStreamAlias("a4p-content")
public class ContentXmlModel {
    
    private Map<String, Object> content;
    
    public ContentXmlModel() {
        this.content = new HashMap<>();
    }



    /**
     * @return the content
     */
    public Map<String, Object> getContent() {
        return content;
    }



    /**
     * @param content the content to set
     */
    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
