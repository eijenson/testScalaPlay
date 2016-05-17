if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

function getRandom() {
	return Math.round(Math.random() * 100000)
}

window.onload= function(){
var id = document.getElementsByName("id")[0];
var val = getRandom();
id.value = val;
}