/*
 * $Source: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//launcher/src/java/org/apache/commons/launcher/StreamConnector.java,v $
 * $Revision: 1.3 $
 * $Date: 2003/11/30 15:15:17 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation - http://www.apache.org/"
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * http://www.apache.org/
 *
 */

package org.apache.commons.launcher;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

 /**
 * A class for connecting an OutputStream to an InputStream.
 *
 * @author Patrick Luby
 */
public class StreamConnector extends Thread {

    //------------------------------------------------------------------ Fields

    /**
     * Input stream to read from.
     */
    private InputStream is = null;

    /**
     * Output stream to write to.
     */
    private OutputStream os = null;

    //------------------------------------------------------------ Constructors

    /**
     * Specify the streams that this object will connect in the {@link #run()}
     * method.
     *
     * @param is the InputStream to read from.
     * @param os the OutputStream to write to.
     */
    public StreamConnector(InputStream is, OutputStream os) {

        this.is = is;
        this.os = os;

    }

    //----------------------------------------------------------------- Methods

    /**
     * Connect the InputStream and OutputStream objects specified in the
     * {@link #StreamConnector(InputStream, OutputStream)} constructor.
     */
    public void run() {

        // If the InputStream is null, don't do anything
        if (is == null)
            return;

        // Connect the streams until the InputStream is unreadable
        try {
            int bytesRead = 0;
            byte[] buf = new byte[4096];
            while ((bytesRead = is.read(buf)) != -1) {
                if (os != null && bytesRead > 0) {
                    os.write(buf, 0, bytesRead);
                    os.flush();
                }
                yield();
            }
        } catch (IOException e) {}

    }

}