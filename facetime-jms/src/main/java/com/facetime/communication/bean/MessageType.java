package com.facetime.communication.bean;

/**
 * 消息类型<br>
 * 字符串长度不超过10
 */
public class MessageType {

	/**
	 * chat message
	 */
	public static final String ChatMessage = "Chat";
	/**
	 * 新邮件到达消息
	 */
	public static final String NewMailNotify = "NewMail";
	/**
	 * 邮件接收完成消息
	 */
	public static final String MailNotify = "MailN";
	/**
	 * invite buddy
	 */
	public static final String InviteBuddy = "AddBud";
	/**
	 * 加好友，同意
	 */
	public static final String Agree = "Agree";
	/**
	 * 加好友，拒绝
	 */
	public static final String Refuse = "Refuse";
	/**
	 * 邀请好友回复， 代表被邀请方接受邀请，邀请方处理
	 */
	public static final String Reply = "Reply";
	/**
	 * 强行下线
	 */
	public static final String ForceOffline = "ForOffl";
	/**
	 * 即时文件传输
	 */
	public static final String InstantFile = "InsFile";
	/**
	 * 私聊时共享文件
	 */
	public static final String ShareFileInPrivacy = "SFP";
	/**
	 * 群聊时共享文件
	 */
	public static final String ShareFileInGroup = "SFG";
	/**
	 * 群聊共享即时文件，文件为客户端上传
	 */
	public static final String ShareInstantFile = "SIF";
	/**
	 * 群聊天记录
	 */
	public static final String GroupChat = "GroupChat";
	/**
	 * 删除好友时， 通知对方
	 */
	public static final String InformDeleteBuddy = "DelBud";
	/**
	 * 邀请好友加入聊天组
	 */
	public static final String InviteBuddyToChatGroup = "IBToCG";
	/**
	 * 好友回复是否加入聊天组
	 */
	public static final String ReplyForInvitedToChatGroup = "RICG";
	/**
	 * 某聊天组成员通知组内所有成员获取最新的成员列表
	 */
	public static final String InformChatGroupToGetMemberList = "ICGGM";
	/**
	 * 某聊天组成员通知组内所有成员获取最新的成员列表
	 */
	public static final String InformJoinChatGroup = "InfJCG";
	/**
	 * 聊天组成员退出通知
	 */
	public static final String InformRemoveChatGroupMember = "InfRMem";
	/**
	 * 通知编辑状态
	 */
	public static final String InformEditStatus = "InfEditSta";
	/**
	 * 视频邀请
	 */
	public static final String VideoInvite = "VidInv";
	/**
	 * 视频开始
	 */
	public static final String VideoStart = "VidSta";
	/**
	 * 拒绝视频
	 */
	public static final String VideoRefuse = "VidRef";
	/**
	 * 视频结束
	 */
	public static final String VideoEnd = "VidEnd";
	/**
	 * 音频邀请
	 */
	public static final String AudioInvite = "AudInv";
	/**
	 * 音频开始
	 */
	public static final String AudioStart = "AudSta";
	/**
	 * 拒绝音频
	 */
	public static final String AudioRefuse = "AudRef";
	/**
	 * 音频结束
	 */
	public static final String AudioEnd = "AudEnd";

	/**
	 * 用户登入
	 */
	public static final String UserJoin = "UserJoin";
	/**
	 * 用户改变状态，其新状态值由
	 * {@link com.conlect.oatos.dto.autobean.IMessgeDTO#getMessageBody()}获取
	 */
	public static final String UserStatusChange = "UserSCh";
	/**
	 * 用户离开
	 */
	public static final String UserLeave = "UserLea";
	/**
	 * 用户信息更新
	 */
	public static final String UserInfoUpdate = "UserUpd";

	/**
	 * after user agreed import contacts from YAHOO! or GOOGLE, it returns a
	 * verifier.
	 */
	public static final String BuddyImportFromWeb = "BudImpWeb";

	/**
	 * offline file
	 */
	public static final String OfflineFile = "OffFile";

	/**
	 * voice mail
	 */
	public static final String VoiceMail = "VoMail";
	/**
	 * 客户请求服务，客服处理
	 */
	public static final String CustomerRequest = "CusReq";
	/**
	 * 客服响应，客户处理
	 */
	public static final String ServiceResponse = "SerRes";
	/**
	 * 客户退出，客服处理
	 */
	public static final String CustomerLeave = "CusLea";
	/**
	 * 客服退出，客户处理
	 */
	public static final String ServiceLeave = "SerLea";
	/**
	 * 企业用户登录记录
	 */
	public static final String Login = "Login";
	/**
	 * 文件进入记录
	 */
	public static final String FileIn = "FileIn";
	/**
	 * 文件输出记录
	 */
	public static final String FileOut = "FileOut";
	/**
	 * 客户连接通知，客户发出，客服处理
	 */
	public static final String CustomerConnect = "CusCon";
	/**
	 * 客服连接响应，客服发出，客户处理
	 */
	public static final String ServiceConnectRes = "SerCRes";
	/**
	 * 客服移除客户通知，客服发出，客户处理
	 */
	public static final String RemoveCustomer = "ReCus";
	/**
	 * 共享网盘文件更新
	 */
	public static final String ShareFileNew = "SFNew";
	/**
	 * 同事更新
	 */
	public static final String ColleagueNew = "ColNew";
	/**
	 * 视频会议邀请参会
	 */
	public static final String ConferenceInvite = "ConInv";
	/**
	 * 视频会议开始，通知出席
	 */
	public static final String ConferenceStart = "ConSta";
	/**
	 * 视频会议结束
	 */
	public static final String ConferenceEnd = "ConEnd";
	/**
	 * 视频会议更新
	 */
	public static final String ConferenceUpdate = "ConUpd";
	/**
	 * 视频会议文档更新
	 */
	public static final String ConferenceDocNew = "ConDocNew";
	/**
	 * 通知阻止状态更新，message body true 标识 block，false 标识 unblock
	 */
	public static final String InformBlock = "IB";
	/**
	 * 系统消息
	 */
	public static final String SystemMsg = "SystemMsg";
}
