package all.about.apartment.message.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import all.about.apartment.message.domain.MessageVO;

@Repository
public class MessageDAOImpl implements MessageDAO {

	@Inject
	private SqlSession session;
	private static String namespace = "all.about.apartment.mappers.MessageMapper";
	
	@Override
	public void sendMessage(MessageVO vo) throws Exception {
		session.insert(namespace+".sendMessage", vo);
	}

	@Override
	public List<MessageVO> recieveMessage(String reciever) throws Exception {
		return session.selectList(namespace+".recieveMessage", reciever);
	}

}