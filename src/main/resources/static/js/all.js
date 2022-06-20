$('#search').click(function () {
    console.log("测试");
    //创建form
    var form=$("<form></form>");
    //设置属性
    form.attr("action","/search");
    form.attr("method","get");

    ////遍历提交参数
    var input=$("<input type='text' name='searchName'/>");
    input.attr("value",$("#searchName").val());
    form.append(input);

    ///////////////下面不管
    form.appendTo("body");
    form.hide();
    form.submit();
});