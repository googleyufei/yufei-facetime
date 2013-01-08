package com.facetime.core.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.facetime.core.resource.impl.AbstractResource;

public class FilepathResource extends AbstractResource {

    File file;

	public FilepathResource(File file) {
		super(file.getPath());
        this.file = file;
	}
	
	public FilepathResource(String path) {
		super(path);
		this.file = new File(path);
	}

	public URL toURL() {
        try {
            return file.toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

	@Override
	protected Resource newResource(String path) {
		Resource resource = new FilepathResource(path);
		return resource;
	}

	@Override
	public int hashCode() {
		return 227 ^ path().hashCode();
	}

	@Override
	public String toString() {
		return "file:" + path();
	}

	public long lastModified() {
		return file.lastModified();
	}
}
