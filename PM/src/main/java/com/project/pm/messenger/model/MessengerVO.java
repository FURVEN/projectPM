package com.project.pm.messenger.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MessengerVO {
	
	private String pk_msgno;
	private String fk_senderno ;
	private String fk_recipientno ;
	private String origin_msgno ;
	private String subject ;
	private String content ;
	private String view_status ;
	private String having_attach ;
	private String writedate ;
	private String group_msgno;

}
