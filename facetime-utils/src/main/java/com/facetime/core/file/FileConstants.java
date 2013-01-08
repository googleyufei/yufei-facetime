package com.facetime.core.file;

public interface FileConstants {

	String DEFAULT_DOMAIN_NAME = "conlect.oatOS.";
	String OK_MARK = "OK";
	String ERROR_MARK = "ERROR";

	String CHARSET_UTF_8 = "UTF-8";
	String CHARSET_GB2312 = "GB2312";
	String CHARSET_ISO8859 = "ISO8859-1";

	// file type
	String FILE_TYPE_ICON = "icon";
	String FILE_TYPE_ONLINEDISK = "onlinedisk";
	String FILE_TYPE_TEMP = "tempfile";
	String FILE_TYPE_SHAREDISK = "sharedisk";
	String FILE_TYPE_CONFERENCE_DOC = "conferenceDoc";
	String FILE_TYPE_ZIP_DOWN = "zip_doanload";
	String FILE_TYPE_SHAREFILE_THUMB = "sharefile_thumb";

	/**
	 * enterprise logo
	 */
	String FILE_TYPE_ENTLOGO = "EntLogo";

	String FILE_NEW = "new";
	// file format
	String FILE_FORMAT_SWF = "swf";
	String FILE_FORMAT_PDF = "pdf";
	String FILE_FORMAT_HTML = "html";
	String FILE_FORMAT_DOC = "doc";
	String FILE_FORMAT_DOCX = "docx";
	String FILE_FORMAT_XLS = "xls";
	String FILE_FORMAT_XLSX = "xlsx";

	String FILE_FORMAT_JPG = "jpg";
	String FILE_FORMAT_PNG = "png";
	String FILE_FORMAT_MP3 = "mp3";

	// oat writer
	String FILE_FORMAT_OATWRI = "oatw";
	// oat spreadsheet
	String FILE_FORMAT_OATSPR = "oats";
	// oat presentation
	String FILE_FORMAT_OATPRE = "oatp";

	// accept image types
	String[] IMAGE_FORMATS = new String[] { "JPG", "JPEG", "PNG", "BMP", "WBMP", "GIF", "ICO" };

	// supported formats by open office converter
	String[] CONVERT_TO_PDF_SUPPORTED_FORMAT = new String[] { "ODT", "SXW", "RTF", "DOC", "DOCX", "WPD", "TXT", "HTML",
			"ODS", "SXC", "XLS", "XLSX", "CSV", "TSV", "ODP", "SXI", "PPT", "PPTX", "ODG", "SQL", "XML", "LOG", "WPS" };

	String[] CONVERT_TO_HTML_SUPPORTED_FORMAT = new String[] { "HTML", "DOC", "DOCX", "XLS", "XLSX", "ODT", "TXT",
			"WPS", "oatw", "ODS", "CSV" };

	String[] EDIT_AS_HTML_SUPPORTED_FORMAT = new String[] { "HTML", "DOC", "DOCX", "XLS", "XLSX", "ODT", "TXT", "WPS",
			"oatw" };

	String[] VIEW_AS_HTML_SUPPORTED_FORMAT = new String[] { "HTML", "XLS", "XLSX", "ODT", "TXT", "WPS", "oatw", "ODS",
			"CSV", "LOG", "XML", "SQL" };

	String[] VIEW_AS_TEXT_SUPPORTED_FORMAT = new String[] { "TXT", "XML", "LOG", "XML", "SQL", "CSV" };

	String[] JWPLAYER_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "webm", "flv", "ogv", "aac", "m4a",
			"f4a", "ogg", "oga", "mp3" };

	String[] JWPLAYER_IE_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "flv", "aac", "m4a", "f4a",
			"mp3" };

	String[] JWPLAYER_SAFARI_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "flv", "aac", "m4a", "f4a",
			"mp3" };

	String[] JWPLAYER_IOS_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "aac", "m4a", "f4a", "mp3" };

	String[] JWPLAYER_ANDROID_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "flv", "aac", "m4a", "f4a",
			"ogg", "oga", "mp3" };

	// file type for show
	String[] FILETYPE_DOC = new String[] { "DOC", "DOCX", "DOT", "ODT", "SXW", "WPD", "WPS" };
	String[] FILETYPE_PDF = new String[] { "PDF" };
	String[] FILETYPE_PPT = new String[] { "PPT", "PPTX", "PPS", "ODP", "SXI" };
	String[] FILETYPE_SPREADSHEET = new String[] { "XLS", "XLSX", "ODS", "SXC", "CSV", "TSV" };

	String[] FILETYPE_MUSIC = new String[] { "aac", "m4a", "f4a", "ogg", "oga", "mp3" };
	String[] FILETYPE_VIDEO = new String[] { "mp4", "m4v", "f4v", "mov", "webm", "flv", "ogv" };
	String[] FILETYPE_PS = new String[] { "PSD" };
	String[] FILETYPE_TXT = new String[] { "txt", "log", "HTML", "XHTML", "SQL", "XML" };

	String[] FILETYPE_GIF = new String[] { "GIF" };
	String[] FILETYPE_JPG = new String[] { "JPG", "JPEG" };
	String[] FILETYPE_PNG = new String[] { "PNG" };

	String[] FILETYPE_RAR = new String[] { "RAR" };

	String[] FILETYPE_ZIP = new String[] { "ZIP" };
	String[] DEFAULT_FOLDER_NAMES = new String[] { "我的文档", "我的照片", "我的视频", "我的音乐" };
}
