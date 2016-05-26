if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

function getRandom() {
	return Math.round(Math.random() * 100000)
}

window.onload= function(){
	var submit = document.getElementById("addTaskSubmit");
	submit.onclick= function() {taskSend()};

function taskSend(){
	
	var name = document.getElementById("addTaskName");
	var ticket = document.getElementById("addTaskTicket");
	
	var sendJson = JSON.stringify(
        {"id": getRandom() -0,"name":name.value,"ticket":ticket.value}
    )
    console.log(sendJson)
	socket.send(sendJson)
}

}
var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
//        var socket = new WS("ws://localhost:9000/socket")
//        socket.onopen = function(evt) {
//          socket.send(JSON.stringify(
//              {"ping": "hogddde"}
//          ))
//        };
    	var socket = new WS("ws://localhost:9000/addTask")
		socket.onopen = function(evt) {
			console.log("start")
		}

        socket.onmessage = function(evt) { onMessage(evt) };

          function onMessage(evt) {
            var data = JSON.parse(evt.data)
            console.log(data);
            //socket.close();
        }
