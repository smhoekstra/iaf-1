package nl.nn.adapterframework.validation;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.IPipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 5.0
 */
public class SchemasProviderImpl implements SchemasProvider {
    private final String id;
    private final String xsd;



    public SchemasProviderImpl(String id, String xsd) {
        this.id = id;
        this.xsd = xsd;
    }
    public String getSchemasId() throws ConfigurationException {
        return id;
    }

    public List<Schema> getSchemas() throws ConfigurationException {
        List<Schema> schemas = new ArrayList<Schema>();
        schemas.add(
                new Schema() {
                    public InputStream getInputStream() throws IOException {
                        return ClassUtils.getResourceURL(xsd).openStream();
                    }

                    public Reader getReader() throws IOException {
                        return null;
                    }

                    public String getSystemId() {
                        return ClassUtils.getResourceURL(xsd).toExternalForm();
                    }
                }
        );
        return schemas;
    }

    /**
     * Not clear what this should do.
     */
    public String getSchemasId(IPipeLineSession session) throws PipeRunException {
        throw new UnsupportedOperationException();
    }

    /**
     * Not clear what this should do.
     */
    public List<Schema> getSchemas(IPipeLineSession session) throws PipeRunException {
        throw new UnsupportedOperationException();
    }
}
