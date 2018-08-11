package com.github.dapeng.common.mail.entity;

import java.net.URL;

public class MailAttach {
	/** 附件名称*/
    private String name;

    /** 附件描述*/
    private String description;

    /** 附件文件路径，绝对路径*/
    private String path;

    /** 附件url*/
    private URL url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}
