package com.hospital.hospital;


import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.hospital.mybatis.MyBatisDAO;
import com.hospital.vo.Patient_1VO;

public class EchoHandler extends TextWebSocketHandler{

	@Autowired
	private SqlSession sqlSession;
	
	Map<String, WebSocketSession> usersAll = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersA = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersP = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersD_a = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersD_b = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersD_c = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersN_a = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersN_b = new ConcurrentHashMap<String, WebSocketSession>();
	Map<String, WebSocketSession> usersN_c = new ConcurrentHashMap<String, WebSocketSession>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		putMemberId(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String msg = message.getPayload();
		
		if(msg != null) {
			String[] strs = msg.split(",");
			log(Arrays.toString(strs));
			
			if(strs != null) {
				String dpart = strs[0].trim();
				String target = strs[1].trim(); 
				String ptIdx = strs[2].trim();
				String ptName = strs[3].trim();
				String content = strs[4].trim();
				String dDay = strs[5].trim();
				
				TextMessage tmpMsg = new TextMessage("<a style='color: black; text-decoration: none;' "
						+ "href='/viewPatientDetail?patientIdx="+ptIdx+"&dDay="+dDay+"'><b>["+content+"]</b> "+ptIdx 
						+ " " + ptName + "  From <strong>" + dpart + "</strong></a>");
				
				TextMessage tmpMsg2 = new TextMessage("<a style='color: black; text-decoration: none;' "
						+ "href='#'><b>[????????????]</b> "
						+ " " + content + "  From <strong>" + dpart + "</strong></a>");
				
				TextMessage tmpMsg3 = new TextMessage("<a style='color: black; text-decoration: none;' "
						+ "href='#'><b>[????????????]</b> "
						+ "  From <strong>" + dpart + "</strong></a>");
				
				
				WebSocketSession targetSession = null;
				
				if (target.equals("All")) {
			        for(String recieverId : usersAll.keySet()) {
			        	targetSession = usersAll.get(recieverId);
			        	targetSession.sendMessage(tmpMsg2);
			        } 
				} else if (content.equals("????????????")) {
					System.out.println("??????????????????1");
					switch(ptName) {
						case "A" :
					        for(String recieverId : usersA.keySet()) {
					        	targetSession = usersA.get(recieverId);
					        	targetSession.sendMessage(tmpMsg3);
					        }
							break;
						case "P" :
							for(String recieverId : usersP.keySet()) {
								targetSession = usersP.get(recieverId);
								targetSession.sendMessage(tmpMsg3);
							}
							break;
						case "DA" :
								for(String recieverId : usersD_a.keySet()) {
									targetSession = usersD_a.get(recieverId);
									targetSession.sendMessage(tmpMsg3);
								}
								break;  
						case "DB":
							for(String recieverId : usersD_b.keySet()) {
								targetSession = usersD_b.get(recieverId);
								targetSession.sendMessage(tmpMsg3);
							}
							break;  
						case "DC":
							for(String recieverId : usersD_c.keySet()) {
								targetSession = usersD_c.get(recieverId);
								targetSession.sendMessage(tmpMsg3);
							}
							break;  
						case "NA":
							for(String recieverId : usersN_a.keySet()) {
								targetSession = usersN_a.get(recieverId);
								targetSession.sendMessage(tmpMsg3);
							}
							break;  
						case "NB":
							for(String recieverId : usersN_b.keySet()) {
								targetSession = usersN_b.get(recieverId);
								targetSession.sendMessage(tmpMsg3);
							}
							break;  
						default:
							System.out.println("????????? ??? ");
							for(String recieverId : usersN_c.keySet()) {
								targetSession = usersN_c.get(recieverId);
								targetSession.sendMessage(tmpMsg3);
							}
							break;  
					}
				} else {
				
					MyBatisDAO mapper = sqlSession.getMapper(MyBatisDAO.class);
					Patient_1VO patientVO = mapper.selectPatient(Integer.parseInt(ptIdx));
					String doctorT = patientVO.getDoctorT();
					String nurseT = patientVO.getNurseT();
					
					switch(target) {
						case "A" :
					        for(String recieverId : usersA.keySet()) {
					        	targetSession = usersA.get(recieverId);
					        	targetSession.sendMessage(tmpMsg);
					        }
							break;
							
						case "P" :
							for(String recieverId : usersP.keySet()) {
								targetSession = usersP.get(recieverId);
								targetSession.sendMessage(tmpMsg);
							}
							break;
							
						case "D" :
							switch(doctorT) {
								case "A":
									for(String recieverId : usersD_a.keySet()) {
										targetSession = usersD_a.get(recieverId);
										targetSession.sendMessage(tmpMsg);
									}
									break;  
								case "B":
									for(String recieverId : usersD_b.keySet()) {
										targetSession = usersD_b.get(recieverId);
										targetSession.sendMessage(tmpMsg);
									}
									break;  
								default:
									for(String recieverId : usersD_c.keySet()) {
										targetSession = usersD_c.get(recieverId);
										targetSession.sendMessage(tmpMsg);
									}
									break;  
							}
							break;
							
						default :		
							System.out.println("??????1");
							switch(nurseT) {
								case "A":
									for(String recieverId : usersN_a.keySet()) {
										targetSession = usersN_a.get(recieverId);
										targetSession.sendMessage(tmpMsg);
									}
									break;  
								case "B":
									for(String recieverId : usersN_b.keySet()) {
										targetSession = usersN_b.get(recieverId);
										targetSession.sendMessage(tmpMsg);
									}
									break;  
								default:
									for(String recieverId : usersN_c.keySet()) {
										targetSession = usersN_c.get(recieverId);
										targetSession.sendMessage(tmpMsg);
									}
									break;  
							}
							break;
					}
				}
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Map<String,Object> map = session.getAttributes();
		String userDpart = (String) map.get("dpart");
		String userId = (String) map.get("employeeIdx");
		if(userId!=null) {	
			usersAll.remove(userId);	
			switch(userDpart) {
				case "?????????" :
					usersA.remove(userId);   
					log(userId + " ??? ?????? ??????");
					break;
				case "?????????" :
					usersP.remove(userId);
					log(userId + " ??? ?????? ??????");
					break;
				case "??????" :
					String doctorT = (String) map.get("doctorT");
					switch(doctorT) {
						case "A":
							usersD_a.remove(userId);
							log(userId + " ??? ?????? ??????");
							break;  
						case "B":
							usersD_b.remove(userId);
							log(userId + " ??? ?????? ??????");
							break;  
						default:
							usersD_c.remove(userId);
							log(userId + " ??? ?????? ??????");
							break;  
					}
					break;
				default :		
					String nurseT = (String) map.get("nurseT");
					switch(nurseT) {
						case "A":
							usersN_a.remove(userId);
							log(userId + " ??? ?????? ??????");
							break;  
						case "B":
							usersN_b.remove(userId);
							log(userId + " ??? ?????? ??????");
							break;  
						default:
							usersN_c.remove(userId);
							log(userId + " ??? ?????? ??????");
							break;  
					}
					break;
			}
		
		}	
		
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log(session.getId() + " ????????? ??????: " + exception.getMessage());

	}
	private void log(String logmsg) {
		System.out.println(new Date() + " : " + logmsg);
	}
	
	private String getMemberId(WebSocketSession session) {
		
		Map<String,Object> map = session.getAttributes();
		String userId = (String) map.get("employeeIdx");
		return userId == null ? null : userId;
	}
	private String getMemberDpart(WebSocketSession session) {
		
		Map<String,Object> map = session.getAttributes();
		String userDpart = (String) map.get("dpart");
		return userDpart == null ? null : userDpart;
	}
	
	private void putMemberId(WebSocketSession session) {
	
		Map<String,Object> map = session.getAttributes();
		String userDpart = (String) map.get("dpart");
		String userId = (String) map.get("employeeIdx");
		
		if(userId!=null) {	
			usersAll.put(userId, session); 
			switch(userDpart) {
				case "?????????":
					log(userId + " ??? ?????? ??????");
					usersA.put(userId, session);  
					break;
				case "?????????":
					log(userId + " ??? ?????? ??????");
					usersP.put(userId, session);   
					break;
				case "??????":
					String doctorT = (String) map.get("doctorT");
					switch(doctorT) {
						case "A":
							log(userId + " ??? ?????? ??????");
							usersD_a.put(userId, session);   
							break;  
						case "B":
							log(userId + " ??? ?????? ??????");
							usersD_b.put(userId, session);   
							break;  
						default:
							log(userId + " ??? ?????? ??????");
							usersD_c.put(userId, session);   
							break;  
					}
					break;
				default :		
					String nurseT = (String) map.get("nurseT");
					switch(nurseT) {
						case "A":
							log(userId + " ??? ?????? ??????");
							usersN_a.put(userId, session);   
							break;  
						case "B":
							log(userId + " ??? ?????? ??????");
							usersN_b.put(userId, session);   
							break;  
						default:
							log(userId + " ??? ?????? ??????");
							usersN_c.put(userId, session);   
							break;  
					}
					break;
			}
		}
	}
	
}