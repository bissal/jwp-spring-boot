package next.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import next.CannotOperateException;
import next.dao.AnswerDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import next.repository.QuestionRepository;

@Service
public class QnaService {
	@Autowired
	private AnswerDao answerDao;
	
	@Autowired
	private QuestionRepository questionRepository;

//	@Autowired
//	public QnaService(QuestionDao questionDao, AnswerDao answerDao) {
//		this.questionDao = questionDao;
//		this.answerDao = answerDao;
//	}

	public Question findById(long questionId) {
		return questionRepository.findOne(questionId);
	}

	public List<Answer> findAllByQuestionId(long questionId) {
		return answerDao.findAllByQuestionId(questionId);
	}

	public void deleteQuestion(long questionId, User user) throws CannotOperateException {
		Question question = questionRepository.findOne(questionId);
		if (question == null) {
			throw new EmptyResultDataAccessException("존재하지 않는 질문입니다.", 1);
		}

		if (!question.isSameUser(user)) {
			throw new CannotOperateException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
		}

		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		if (answers.isEmpty()) {
			questionRepository.delete(questionId);
			return;
		}

		boolean canDelete = true;
		for (Answer answer : answers) {
			String writer = question.getWriter().getUserId();
			if (!writer.equals(answer.getWriter())) {
				canDelete = false;
				break;
			}
		}

		if (!canDelete) {
			throw new CannotOperateException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
		}

		questionRepository.delete(questionId);
	}

	public void updateQuestion(long questionId, Question newQuestion, User user) throws CannotOperateException {
		Question question = questionRepository.findOne(questionId);
        if (question == null) {
        	throw new EmptyResultDataAccessException("존재하지 않는 질문입니다.", 1);
        }
        
        if (!question.isSameUser(user)) {
            throw new CannotOperateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
        }
        
        question.update(newQuestion);
        questionRepository.save(question);
	}
}
