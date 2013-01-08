package com.facetime.communication.utils;

public class CommConstants {

	public static final String ERROR_MARK = "error";
	public static final String OK_MARK = "OK";
	public static final String SEND_EMAIL = "SEND_EMAIL";
	public static final String SEND_MESSAGE = "SEND_MESSAGE";
	public static final String NONE_MARK = "none";

	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_GB2312 = "GB2312";
	public static final String CHARSET_ISO8859 = "ISO8859-1";

	// file type
	public static final String FILE_TYPE_ICON = "icon";
	public static final String FILE_TYPE_ONLINEDISK = "onlinedisk";
	public static final String FILE_TYPE_TEMP = "tempfile";
	public static final String FILE_TYPE_SHAREDISK = "sharedisk";
	public static final String FILE_TYPE_CONFERENCE_DOC = "conferenceDoc";
	public static final String FILE_TYPE_ZIP_DOWN = "zip_doanload";
	public static final String FILE_TYPE_SHAREFILE_THUMB = "sharefile_thumb";

	/**
	 * enterprise logo
	 */
	public static final String FILE_TYPE_ENTLOGO = "EntLogo";

	public static final String FILE_NEW = "new";
	// file format
	public static final String FILE_FORMAT_SWF = "swf";
	public static final String FILE_FORMAT_PDF = "pdf";
	public static final String FILE_FORMAT_HTML = "html";
	public static final String FILE_FORMAT_DOC = "doc";
	public static final String FILE_FORMAT_DOCX = "docx";
	public static final String FILE_FORMAT_XLS = "xls";
	public static final String FILE_FORMAT_XLSX = "xlsx";

	public static final String FILE_FORMAT_JPG = "jpg";
	public static final String FILE_FORMAT_PNG = "png";
	public static final String FILE_FORMAT_MP3 = "mp3";

	// oat writer
	public static final String FILE_FORMAT_OATWRI = "oatw";
	// oat spreadsheet
	public static final String FILE_FORMAT_OATSPR = "oats";
	// oat presentation
	public static final String FILE_FORMAT_OATPRE = "oatp";

	// accept image types
	public static final String[] IMAGE_FORMATS = new String[] { "JPG", "JPEG", "PNG", "BMP", "WBMP", "GIF", "ICO" };

	// supported formats by open office converter
	public static final String[] CONVERT_TO_PDF_SUPPORTED_FORMAT = new String[] { "ODT", "SXW", "RTF", "DOC", "DOCX",
			"WPD", "TXT", "HTML", "ODS", "SXC", "XLS", "XLSX", "CSV", "TSV", "ODP", "SXI", "PPT", "PPTX", "ODG", "SQL",
			"XML", "LOG", "WPS" };

	public static final String[] CONVERT_TO_HTML_SUPPORTED_FORMAT = new String[] { "HTML", "DOC", "DOCX", "XLS",
			"XLSX", "ODT", "TXT", "WPS", "oatw", "ODS", "CSV" };

	public static final String[] EDIT_AS_HTML_SUPPORTED_FORMAT = new String[] { "HTML", "DOC", "DOCX", "XLS", "XLSX",
			"ODT", "TXT", "WPS", "oatw" };

	public static final String[] VIEW_AS_HTML_SUPPORTED_FORMAT = new String[] { "HTML", "XLS", "XLSX", "ODT", "TXT",
			"WPS", "oatw", "ODS", "CSV", "LOG", "XML", "SQL" };

	public static final String[] VIEW_AS_TEXT_SUPPORTED_FORMAT = new String[] { "TXT", "XML" };

	public static final String[] JWPLAYER_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "webm", "flv",
			"ogv", "aac", "m4a", "f4a", "ogg", "oga", "mp3" };

	public static final String[] JWPLAYER_IE_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "flv",
			"aac", "m4a", "f4a", "mp3" };

	public static final String[] JWPLAYER_SAFARI_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "flv",
			"aac", "m4a", "f4a", "mp3" };

	public static final String[] JWPLAYER_IOS_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "aac",
			"m4a", "f4a", "mp3" };

	public static final String[] JWPLAYER_ANDROID_SUPPORTED_FORMAT = new String[] { "mp4", "m4v", "f4v", "mov", "flv",
			"aac", "m4a", "f4a", "ogg", "oga", "mp3" };

	// 默认好友分组
	public static final String DefaultFriendGroupName = "My buddies";
	// 黑名单组名
	public static final String BlackListGroupName = "Blacklist";
	// 默认网盘文件夹
	public static final String DocumentFolderName = "My documents";
	public static final String PictureFolderName = "My pictures";
	public static final String ReceivedFileFolderName = "Received files";
	public static final String SendFileFolderName = "Send files";
	public static final String EmailAttachFolderName = "Email Attach";

	public static final int PDF_SPLIT_PAGES = 10;
	// 激活query string key
	public static final String URL_PARAM_ACTIVATION_TYPE = "upat";
	public static final String URL_PARAM_USER_ID = "upui";
	public static final String URL_PARAM_ACTIVATION_KEY = "upak";
	public static final String URL_PARAM_ACCOUNT = "upa";
	public static final String URL_PARAM_TIME = "upt";

	// disk space
	public static final long ENTERPRISE_DISK_SIZE = 5 * 1024 * 1024L; // 5G
	public static final long PERSONAL_DISK_SIZE = 1 * 1024 * 1024L; // 1G

	// 返回判断
	public static final String Exist = "Exist";
	public static final String NotExist = "NotExist";
	public static final String MaxCustomer = "MaxCustomer";

	public static final String Administrator = "Admin";

	public static final String DefaultRoleName = "user";

	/**
	 * max conference member
	 */
	public static final int MAX_CONFERENCE_MEMBER = 8;

	// file type for show
	public static final String[] FILETYPE_DOC = new String[] { "DOC", "DOCX", "DOT", "ODT", "SXW", "WPD", "WPS" };
	public static final String[] FILETYPE_PDF = new String[] { "PDF" };
	public static final String[] FILETYPE_PPT = new String[] { "PPT", "PPTX", "PPS", "ODP", "SXI" };
	public static final String[] FILETYPE_SPREADSHEET = new String[] { "XLS", "XLSX", "ODS", "SXC", "CSV", "TSV" };

	public static final String[] FILETYPE_MUSIC = new String[] { "aac", "m4a", "f4a", "ogg", "oga", "mp3" };
	public static final String[] FILETYPE_VIDEO = new String[] { "mp4", "m4v", "f4v", "mov", "webm", "flv", "ogv" };
	public static final String[] FILETYPE_PS = new String[] { "PSD" };
	public static final String[] FILETYPE_TXT = new String[] { "txt", "log", "HTML", "XHTML", "SQL", "XML" };

	public static final String[] FILETYPE_GIF = new String[] { "GIF" };
	public static final String[] FILETYPE_JPG = new String[] { "JPG", "JPEG" };
	public static final String[] FILETYPE_PNG = new String[] { "PNG" };

	public static final String[] FILETYPE_RAR = new String[] { "RAR" };

	public static final String[] FILETYPE_ZIP = new String[] { "ZIP" };

	/**
	 * 默认未分组的邮件联系人组ID
	 */
	public static final long DEFAULT_CONTACT_GROUP_ID = 0;
	public static final String DEFAULT_DOMAIN_NAME = "conlect.oatOS.";
}
