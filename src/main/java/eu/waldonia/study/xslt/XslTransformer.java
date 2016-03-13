package eu.waldonia.study.xslt;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.sf.saxon.jaxp.SaxonTransformerFactory;

/**
 * @author sih
 *
 */
@Component
public class XslTransformer {
	
	private static final Logger LOG = LoggerFactory.getLogger(XslTransformer.class);
	
	/**
	 * Uses XSLT 2.0
	 * @param xmlSource The document to transform
	 * @param xslSource The stylesheet
	 * @return A transformed document
	 */
	public String transform(Source xmlSource, Source xslSource) {
		
		String output = null;
		
		try {
			TransformerFactory factory = new SaxonTransformerFactory();
			Transformer transformer = factory.newTransformer(xslSource);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			transformer.transform(xmlSource, new StreamResult(baos));
			
			output = baos.toString(StandardCharsets.UTF_8.name());
		}
		catch (TransformerException | UnsupportedEncodingException e) {
			LOG.error(e.getMessage());
		}
		
		
		return output;
	}

}
