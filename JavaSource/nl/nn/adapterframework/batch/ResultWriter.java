/*
 * $Log: ResultWriter.java,v $
 * Revision 1.1  2007-08-03 08:37:51  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.batch;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.nn.adapterframework.core.PipeLineSession;

import org.apache.commons.lang.StringUtils;


/**
 * Baseclass for resulthandlers that write the transformed record to a writer.
 * 
 * <p><b>Configuration:</b>
 * <table border="1">
 * <tr><th>attributes</th><th>description</th><th>default</th></tr>
 * <tr><td>classname</td><td>nl.nn.adapterframework.batch.ResultWriter</td><td>&nbsp;</td></tr>
 * <tr><td>{@link #setDefaultResultHandler(boolean) default}</td><td>If true, this resulthandler is the default for all RecordHandlingFlow that do not have a handler specified</td><td>&nbsp;</td></tr>
 * </table>
 * </p>
 * 
 * @author  Gerrit van Brakel
 * @since   4.7
 * @version Id
 */
public abstract class ResultWriter extends AbstractResultHandler {
	public static final String version = "$RCSfile: ResultWriter.java,v $  $Revision: 1.1 $ $Date: 2007-08-03 08:37:51 $";
	
	private Map openWriters = Collections.synchronizedMap(new HashMap());
	
	protected abstract Writer createWriter(PipeLineSession session, String streamId) throws Exception;

	public void openResult(PipeLineSession session, String streamId) throws Exception {
		getBufferedWriter(session, streamId, true);
	}

	
	public void handleResult(PipeLineSession session, String streamId, String recordKey, Object result) throws Exception {
		if (result instanceof String) {
			write(session, streamId, (String)result);
		}
		else if (result instanceof String[]) {
			write(session, streamId, (String[])result);
		}
	}
	
	private void write(PipeLineSession session, String streamId, String line) throws Exception {
		BufferedWriter bw = getBufferedWriter(session, streamId, false);
		bw.write(line);
		bw.newLine();
	}

	private void write(PipeLineSession session, String streamId, String[] lines) throws Exception {
		BufferedWriter bw = getBufferedWriter(session, streamId, false);
		for (int i = 0; i < lines.length; i++) {
			bw.write(lines[i]);
			bw.newLine();
		}
	}
	
	public Object finalizeResult(PipeLineSession session, String streamId, boolean error) throws IOException {
		BufferedWriter bw = (BufferedWriter)openWriters.remove(streamId);
		if (bw != null) {
			bw.close();
		}
		return null;
	}
	
	public void writePrefix(PipeLineSession session, String streamId, boolean mustPrefix, boolean hasPreviousRecord) throws Exception {
		String[] prefix = prefix(mustPrefix, hasPreviousRecord);
		if (prefix != null) {
			write(session, streamId, prefix);
		}
	}

	public void writeSuffix(PipeLineSession session, String streamId) throws Exception {
		BufferedWriter bw = getBufferedWriter(session, streamId, false);
		if (bw != null && ! StringUtils.isEmpty(getSuffix())) {
			write(session, streamId, getSuffix());
		}
	}

	private BufferedWriter getBufferedWriter(PipeLineSession session, String streamId, boolean openIfNotOpen) throws Exception {
		BufferedWriter bw;
		bw = (BufferedWriter)openWriters.get(streamId);
		if (bw != null) {
			return bw;
		}
		
		if (!openIfNotOpen) {
			return null;
		}
		Writer writer = createWriter(session,streamId);
		if (writer==null) {
			throw new IOException("cannot get writer for stream ["+streamId+"]");
		}
		if (writer instanceof BufferedWriter) {
			bw=(BufferedWriter)bw;
		} else {
			bw=new BufferedWriter(writer);
		}
		openWriters.put(streamId,bw);
		return bw;		
	}
	
}