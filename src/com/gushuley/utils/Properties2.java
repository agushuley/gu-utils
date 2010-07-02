package com.gushuley.utils;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;

import com.gushuley.utils.Tools;

public class Properties2 extends HashMap<String, String>{
	private static final long serialVersionUID = -3183194028238205686L;

	public Properties2() { }

	public Properties2(String props) throws IOException {
		read(new StringReader(Tools.coalesce(props, "")));
	}
	
	public Properties2(String ... values) {
		if (values.length % 2 != 0) {
			throw new InvalidParameterException("Values lenght is not even");
		}
		for (int i = 0; i < values.length / 2; i++) {
			this.put(values[i * 2], values[i * 2 + 1]);
		}
	}

	private void read(Reader r) throws IOException {
		BufferedReader s = new BufferedReader(r);
		String l;
		while ((l = s.readLine()) != null) {
			if (!Tools.isEmpty(l)) {
				String[] p = l.split("=", 2);
				put(p[0], p[1].replaceAll("\\n", "\n").replaceAll("\r", ""));
			}
		}
	}

	public String getProperty(String name) {
		return get(name);
	}
	
	@Override
	public String toString() {
		final StringBuffer writer = new StringBuffer();
		for (final Map.Entry<String, String> e : entrySet()) {
			if (!Tools.isEmpty(e.getValue())) {
				writer.append(e.getKey());
				writer.append("=");
				writer.append(e.getValue().replaceAll("\r", "").replaceAll("\n", "\\n"));
				writer.append("\n");
			}
		}
		return writer.toString();
	}
}

