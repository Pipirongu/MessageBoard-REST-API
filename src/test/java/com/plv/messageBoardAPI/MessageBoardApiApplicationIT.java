package com.plv.messageBoardAPI;

import com.plv.messageBoardAPI.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

/*
 	Integration tests
 	Clear DB for each test
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = MessageBoardApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageBoardApiApplicationIT {

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@LocalServerPort
	private int port;

	/*
		Verify that a new message can be created
	 */
	@Test
	void createNewMessage() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		ResponseEntity<String> response = this.createMessage(testData1);

		assert(response.getStatusCode().equals(HttpStatus.OK));
	}

	/*
		Verify that a duplicate message by ID won't be allowed to be created
	 */
	@Test
	void createDuplicateMessage() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		ResponseEntity<String> response1 = this.createMessage(testData1);
		assert(response1.getStatusCode().equals(HttpStatus.OK));

		Message testData2 = new Message(1L, "admin2", "testMessage2");
		ResponseEntity<String> response2 = this.createMessage(testData2);
		assert(response2.getStatusCode().equals(HttpStatus.CONFLICT));
	}

	/*
    	Verify that it's possible to edit an existing message
 	*/
	@Test
	void updateExistingMessage() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		ResponseEntity<String> response1 = this.createMessage(testData1);
		assert(response1.getStatusCode().equals(HttpStatus.OK));

		String testMsg = "testMessage2";
		HttpEntity<String> entity = new HttpEntity<String>(testMsg, headers);
		ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/messageBoard/message/1/admin1"), HttpMethod.PUT, entity, String.class);
		assert(response2.getStatusCode().equals(HttpStatus.OK));
	}

	/*
		Test updating existing message with a mismatched owner
	*/
	@Test
	void updateExistingMessageWithMismatchedOwner() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		ResponseEntity<String> response1 = this.createMessage(testData1);
		assert(response1.getStatusCode().equals(HttpStatus.OK));

		String testMsg = "testMessage2";
		HttpEntity<String> entity = new HttpEntity<String>(testMsg, headers);
		ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/messageBoard/message/1/admin2"), HttpMethod.PUT, entity, String.class);
		assert(response2.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}

	/*
		Test updating a non existing message
	*/
	@Test
	void updateNonExistingMessage() {
		String testMsg = "testMessage2";
		HttpEntity<String> entity = new HttpEntity<String>(testMsg, headers);
		ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/messageBoard/message/1/admin2"), HttpMethod.PUT, entity, String.class);
		assert(response2.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}

	/*
    	Verify that it's possible to delete an existing message
 	*/
	@Test
	void deleteExistingMessage() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		ResponseEntity<String> response1 = this.createMessage(testData1);
		assert(response1.getStatusCode().equals(HttpStatus.OK));

		ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/messageBoard/message/1/admin1"), HttpMethod.DELETE, null, String.class);
		assert(response2.getStatusCode().equals(HttpStatus.OK));
	}

	/*
		Test deleting existing message with a mismatched owner
	*/
	@Test
	void deleteExistingMessageWithMismatchedOwner() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		ResponseEntity<String> response1 = this.createMessage(testData1);
		assert(response1.getStatusCode().equals(HttpStatus.OK));

		ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/messageBoard/message/1/admin2"), HttpMethod.DELETE, null, String.class);
		assert(response2.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}

	/*
		Test deleting a non existing message
	*/
	@Test
	void deleteNonExistingMessage() {
		ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/messageBoard/message/1/admin2"), HttpMethod.DELETE, null, String.class);
		assert(response2.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}

	/*
    Test updating a non existing message
*/
	@Test
	void fetchAllMessages() {
		Message testData1 = new Message(1L, "admin1", "testMessage1");
		Message testData2 = new Message(2L, "admin2", "testMessage2");
		ResponseEntity<String> response1 = this.createMessage(testData1);
		ResponseEntity<String> response2 = this.createMessage(testData2);
		assert(response1.getStatusCode().equals(HttpStatus.OK));
		assert(response2.getStatusCode().equals(HttpStatus.OK));


		ResponseEntity<Message[]> response3 = restTemplate.exchange(createURLWithPort("/api/messageBoard/messages"), HttpMethod.GET, null, Message[].class);
		Message[] messages = response3.getBody();

		assertThat(messages[0]).isEqualToComparingFieldByField(testData1);
		assertThat(messages[1]).isEqualToComparingFieldByField(testData2);

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private ResponseEntity<String> createMessage(Message message){
		HttpEntity<Message> entity = new HttpEntity<Message>(message, headers);
		return restTemplate.exchange(createURLWithPort("/api/messageBoard/message"), HttpMethod.POST, entity, String.class);
	}

}
