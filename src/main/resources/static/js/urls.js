function getShortUrl() {
    var str = $("#url").val();
    if (str === "") {
        alert("请先输入网址");
        return;
    }
    if (str === undefined || str.trim().length === 0) {
        alert("请输入正确网址");
        return;
    }
    $.ajax({
        cache: true,
        type: "POST",
        url: "/getShortUrl",
        data: {"url": str},
        async: false,
        error: function () {
            alert("服务器异常");
        },
        success: function (data) {
            $('#sUrlDiv').removeClass("hidden")
            if (data.code === 0) {
                $('#sUrl').html(data.data);
            }
        }
    });
}