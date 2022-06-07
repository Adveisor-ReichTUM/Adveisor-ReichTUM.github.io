<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <title>WebSocket</title>
    <link rel="stylesheet" href="/css/main.css" />
</head>
<body style="background-position: center; background-repeat: no-repeat; background-size: cover;">
<noscript>
    <h2>this is header 2</h2>
</noscript>

<div id="username-page">
    <div class="username-page-container">
        <h1 class="title">Title</h1>
        <form id="usernameForm" name="usernameForm">
            <div class="form-group">
                <input type="text" id="name" placeholder="Name"
                       autocomplete="off" class="form-control" />
            </div>
            <div class="form-group">
                <button type="submit" class="accent username-submit">user name submit</button>
            </div>
        </form>
    </div>
</div>

<div id="chat-page" class="hidden">
    <div class="chat-container">
        <div class="chat-header">
            <h2>ChatBox</h2>
        </div>
        <div class="connecting">Connecting</div>
        <ul id="messageArea">

        </ul>
        <form id="messageForm" name="messageForm" nameForm="messageForm">
            <div class="form-group">
                <div class="input-group clearfix">
                    <input type="text" id="message" placeholder="write a message"
                           autocomplete="off" class="form-control" />
                    <button type="submit" class="primary">send</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script
        src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="/js/main.js"></script>
</body>
</html>