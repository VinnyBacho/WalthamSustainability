function search(form) {
  var address = form.query.value;
  var request = new XMLHttpRequest();
  var url = "http://129.64.149.162:8081/address?address=" + address;

  request.open("GET", url, true);
  request.onload = function() {};

  request.send();

  var data = JSON.parse(this.response);
  console.log("we made it 1");
  if (request.status >= 200 && request.status < 400) {
    console.log("we made it");
    data.forEach(pieceOfInfo => {
      console.log(pieceOfInfo.gallonsPerDayPerHouse);
    });
  } else {
    console.log("error");
  }
}
