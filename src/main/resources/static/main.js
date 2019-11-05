
var chatId = null;
var stompClient = null;
var chatView = $("#chat-content");
var chatCreationView = $("#chat-creation");
var chatIdTextField = $("#chat-id");
var createChatButton = $("#create");
var sendButton = $("#send");
var messageTextField = $("#message");

createChatButton.click( function () {
  chatId = chatIdTextField.val();

  chatCreationView.slideUp()

  var socket = new SockJS('/registry'); // http://localhost:8080/registry
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {

    stompClient.subscribe("/topic/chat-" + chatId , function (event) {

      console.log('Receive event: ', event)

      var message = JSON.parse(event.body).content;

      chatView.append('<div>' + message + '</div>')
    });
  });

});

sendButton.click(function(){
  var message = { content: messageTextField.val() };

  console.log("Sending message", message);

  stompClient.send("/topic/chat-" + chatId, {}, JSON.stringify(message));

  messageTextField.val("");

});