/**
 * Copyright (c) 2013, xembly.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the xembly.org nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.xembly;

import com.rexsl.test.XhtmlMatchers;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilderFactory;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Test case for {@link PiDirective}.
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.9
 */
public final class PiDirectiveTest {

    /**
     * PiDirective can add processing instructions to current DOM.
     * @throws Exception If some problem inside
     */
    @Test
    public void addsProcessingInstructionsToDom() throws Exception {
        final Collection<Directive> dirs = new Directives(
            "PI 'ab' 'boom \u20ac'; ADD 'test'; PI 'foo' 'some data \u20ac';"
        );
        final Document dom = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder().newDocument();
        dom.appendChild(dom.createElement("root"));
        new Xembler(dirs).apply(dom);
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(dom),
            XhtmlMatchers.hasXPaths(
                "/root/processing-instruction('ab')",
                "/root/test/processing-instruction('foo')"
            )
        );
    }

    /**
     * PiDirective can add processing instructions to DOM.
     * @throws Exception If some problem inside
     */
    @Test
    public void addsProcessingInstructionsDirectlyToDom() throws Exception {
        final Document dom = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder().newDocument();
        final Element root = dom.createElement("xxx");
        dom.appendChild(root);
        new PiDirective("x", "y").exec(dom, Arrays.<Node>asList());
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(dom),
            XhtmlMatchers.hasXPath("/processing-instruction('x')")
        );
    }

    /**
     * PiDirective can add processing instructions to root.
     * @throws Exception If some problem inside
     */
    @Test
    public void addsProcessingInstructionsToDomRoot() throws Exception {
        final Collection<Directive> dirs = new Directives(
            "XPATH '/'; PI 'alpha' 'beta \u20ac';"
        );
        final Document dom = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder().newDocument();
        dom.appendChild(dom.createElement("x4"));
        new Xembler(dirs).apply(dom);
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(dom),
            XhtmlMatchers.hasXPath(
                "/processing-instruction('alpha')[.='beta \u20ac']"
            )
        );
    }

}