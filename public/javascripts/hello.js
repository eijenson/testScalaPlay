function getRandom() {
	return Math.round(Math.random() * 100000)
}

window.onload= function(){
	var submit = document.getElementById("addTaskSubmit");
	submit.onclick= function() {taskSend()};
	
	//タスク情報の送信
	function taskSend(){
	
		var id = getRandom() -0;
		var name = document.getElementById("addTaskName").value;
		var ticket = document.getElementById("addTaskTicket").value;
	
		var sendJson = JSON.stringify(
			{"id": id,"name":name,"ticket":ticket}
		)
		console.log(sendJson)
		socket.send(sendJson)
	}
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
	var body = document.getElementsByTagName("body")[0];
	//bodyタブのカスタムデータ属性からurlを取得する
	var socket = new WS(body.dataset.wsUrl);
		socket.onopen = function(evt) {
		console.log("start");
	}
	socket.onmessage = function(evt) { onMessage(evt) };

	function onMessage(evt) {
		var json = JSON.parse(evt.data);
		var data = JSON.parse(json.data);
		console.log(data);
		addRowTaskTable(data);
		//socket.close();
	}
}

/*
 * タスク表示の列を生成します
 * つまり、
 * <tr>
 * 	<td id="name">タスク名</td>
 * 	<td id="ticket">チケット名</td>
 * 	<td>
 *    <form action="/taskDelete" method="POST">
 *      <input type="hidden" value="タスクID" name="id">
 *      <input type="submit" value="削除">
 *    </form>
 *   </td>
 * </tr>
 * を生成します
 */
function addRowTaskTable(task){
	//一行
	var row = document.createElement("tr");
	//タスク名
	var name = document.createElement("td");
	name.id="name"
	name.innerHTML=task.name;
	//チケット名
	var ticket = document.createElement("td");
	ticket.id="ticket"
	ticket.innerHTML=task.ticket

	//form
	var formData = document.createElement("td");
	var form = document.createElement("form");
	form.action="/taskDelete";
	form.method="POST";
	//タスクID
	var input = document.createElement("input");
	input.type="hidden"
	input.value=task.id;
	input.name="id";
	//削除ボタン
	var submit = document.createElement("input");
	submit.type="submit";
	submit.value="削除";

	form.appendChild(input);
	form.appendChild(submit);
	formData.appendChild(form);

	row.appendChild(name);
	row.appendChild(ticket);	
	row.appendChild(formData);
	
	var table = document.getElementById("ticketTable");
	table.appendChild(row);
}