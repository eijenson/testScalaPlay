function getRandom() {
	return Math.round(Math.random() * 100000)
}
var socket;
window.onload= function(){
	var submit = document.getElementById("addTaskSubmit");
	submit.onclick= function() {taskSend()};
	

	
	
	//タスク情報の送信
	function taskSend(){
		var id = getRandom() -0;
		var name = document.getElementById("addTaskName").value;
		var ticket = document.getElementById("addTaskTicket").value;
		var mode = document.getElementById("addMode").value;
		var sendJson = JSON.stringify(
			{"id": id,"name":name,"ticket":ticket,"mode":mode}
		)
		console.log("taskSend",sendJson)
		socket.send(sendJson)
	}

	
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
	var body = document.getElementsByTagName("body")[0];
	//bodyタブのカスタムデータ属性からurlを取得する
		socket = new WS(body.dataset.wsUrl);
		socket.onopen = function(evt) {
		console.log("start");
	}
	socket.onmessage = function(evt) { onMessage(evt) };

	function onMessage(evt) {
		var json = JSON.parse(evt.data);
		if(json.result == "failure"){
			console.log("[failure] websocket response");
			socket.close();
			return;
		}
		var data = JSON.parse(json.data);
		console.log("[success]",data);
		switch(json.mode){
		case "add":
			addRowTaskTable(data);
			break;
		case "remove":
			removeRowTaskTable(data);
			break;
		}
	}
}

//DBからタスクを削除する
function removeTask(button){
	var sendJson = JSON.stringify(
		{"id": button.parentNode.parentNode.id -0,"name":"","ticket":"","mode":"remove"}
	)
	console.log("removeTask",sendJson)
	socket.send(sendJson)
}
//テーブルからタスク情報を削除
function removeRowTaskTable(id){
	var row = document.getElementById(id);
	row.parentNode.deleteRow(row.sectionRowIndex);
}

/*
 * タスク表示の列を生成します
 * つまり、
 * <tr id="タスクID>
 * 	<td id="name">タスク名</td>
 * 	<td id="ticket">チケット名</td>
 * 	<td>
 *   <input name="taskDeleteButton" type="submit" value="削除" onclick="removeTask(this)">
 *  </td>
 * </tr>
 * を生成します
 */
function addRowTaskTable(task){
	//一行
	var row = document.createElement("tr");
	row.id=task.id;
	//タスク名
	var name = document.createElement("td");
	name.id="name"
	name.innerHTML=task.name;
	//チケット名
	var ticket = document.createElement("td");
	ticket.id="ticket"
	ticket.innerHTML=task.ticket
	//削除ボタン
	var submitTd = document.createElement("td");
	var submit = document.createElement("input");
	submit.name="taskDeleteButton"
	submit.type="submit";
	submit.value="削除";
	submit.setAttribute("onclick","removeTask(this)");

	row.appendChild(name);
	row.appendChild(ticket);
	submitTd.appendChild(submit)
	row.appendChild(submitTd);

	var table = document.getElementById("ticketTable");
	table.appendChild(row);
}

