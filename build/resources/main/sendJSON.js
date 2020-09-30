function submitForm() {

    let number = document.querySelector('#number');
    let brand = document.querySelector('#brand');
    let color = document.querySelector('#color');
    let year_of_issue = document.querySelector('#year_of_issue');
    let result = document.querySelector('.result');
    let xhr = new XMLHttpRequest();
       let url = 'http://localhost:8001/add';
    xhr.open("POST", url, true);
    xhr.setRequestHeader('Content-type', 'application/json;charset=UTF-8');
//    xhr.setResponseType("blob");
    var data = JSON.stringify({ "number": number.value, "brand": brand.value, "color":color.value, "year_of_issue":year_of_issue.value });
//    alert(data);

    xhr.onreadystatechange = function () {
        if (xhr.status === 200) {
            alert(this.responseText)
        } else {
            alert("Bad request :(")
        }
    }
//     return data;
     xhr.send(data);
}