function writeForm() {
    location.href = "/form";
}

function logout() {
    $.ajax({
        url: "/logout",
        success: function (data) {
            if (data.code == 200) {
                location.href = "/";
            }
        }
    });
}

function apply(formId) {
    var operator = getUser();
    if (operator == "") {
        location.href = "/";
    }
    $.ajax({
        url: "/apply",
        data: {
            "formId": formId,
            "operator": operator
        },
        success: function (data) {
            if (data.code == 200) {
                location.href = "/home";
            }
        }
    });
}

function giveup(formId) {
    var operator = getUser();
    if (operator == "") {
        location.href = "/";
    }
    $.ajax({
        url: "/giveup",
        data: {
            "formId": formId,
            "operator": operator
        },
        success: function (data) {
            if (data.code == 200) {
                location.href = "/home";
            }
        }
    });
}

function checkState(formId) {
    $.ajax({
        url: "/historyState",
        data: {
            "formId": formId
        },
        success: function (data) {
            if (data.code == 200) {
                var processList = data.info;
                var html = "";
                $.each(processList, function (i, item) {
                    html += "<span>" + item.name + "(操作人：" + item.operator + ")" + "</span><br/><br/>";
                });
                $(".modal-body").html(html);
            }
        }
    });
}

function getUser() {
    var name = "userInfo=";
    var user = "";
    var ca = document.cookie.split(';');
    $.each(ca, function (i, item) {
        if (item.indexOf(name) != -1) {
            user = item.substring(name.length + 1, item.length);
        }
    });
    return user;
}

function submit() {
    var operator = getUser();
    if(operator == ""){
        location.href = "/";
    }
    $.ajax({
        url : "/writeForm",
        data : {
            "title" : $("#title").val(),
            "content" : $("#content").val(),
            "operator" : operator
        },
        success : function(data) {
            if(data.code == 200){
                location.href="/home";
            }
        }
    });
}

function approve(formId) {
    var operator = getUser();
    if (operator == "") {
        location.href = "/";
    }
    $.ajax({
        url: "/approve",
        data: {
            "formId": formId,
            "operator": operator
        },
        success: function (data) {
            if (data.code == 200) {
                location.href = "/homeApprover";
            }
        }
    });
}

function login() {
    $.ajax({
        url: "/login",
        type: "POST",
        data: {
            "username": $("#username").val()
        },
        success: function (data) {
            if (data.code == 200) {
                if (data.info.type == 1) {
                    location.href = "/home";
                } else {
                    location.href = "/homeApprover";
                }
            } else {
                alert(data.msg);
            }
        }
    });
}