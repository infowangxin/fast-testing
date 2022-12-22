$.validator.setDefaults({
    submitHandler : function() {
        updateUser();

    }
});

var app = new Vue({
    el: '#app',
    data:{
        birthday:''
    },
    methods:{
        getAllRoleName:function(roleName,birthday) {
            $.ajax({
                cache : true,
                type : "GET",
                url : context + 'user/getAllRoleName',
                error : function(request) {
                    parent.layer.alert("Connection error");
                },
                success : function(data) {
                    if (data.code === 200) {
                        $("#userRole").html("");
                        var level = "";
                        level += "<div class='layui-input-inline'>";
                        level += "<select id='userRole' name='modules' lay-verify='required' lay-search=''style='width: 235px;height: 33.9px;border: 1px solid #ccc;border-radius: 4px;'>";
                        for (var i = 0; i < data.data.allRoleName.length; i++){
                            level += "<option value='"+data.data.allRoleName[i]+"'>"+data.data.allRoleName[i]+"</option>";
                        }
                        level += "</select></div>";
                        $("#userRole").append(level);
                        $("#userRole option[value='"+roleName+"']").attr("selected","selected");
                        app.birthday = birthday;
                    }
                }
            });
        },
        validateRule:function() {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#signupForm").validate({
                rules : {
                    id : {
                        required : true
                    }, name : {
                        required : true
                    }, password : {
                        required : true
                    }
                },
                messages : {
                    name : {
                        required : icon + "请输入用户名"
                    }, password : {
                        required : icon + "请输入密码"
                    }
                }
            })
        }
    },
    mounted:function () {
        if ($("#sex").val() !== "") {
            $("[name='sex'][value="+$("#sex").val()+"]").prop("checked", "checked");
        }
        this.getAllRoleName($("#roleName").val(),$("#birthday").val());
        this.validateRule();
    }
});

function updateUser(){
    var sysUser = {
        'id':$("#id").val(),
        'name':$("#name").val(),
        'nickName':$("#nickName").val(),
        'sex':$('input:radio:checked').val()===undefined?"":$('input:radio:checked').val(),
        'userRole':$('#userRole option:selected').text(),
        'mobile':$("#mobile").val(),
        'email':$("#email").val(),
        'birthday':$("#birth").val(),
        'hobby':$("#hobby").val(),
        'liveAddress':$("#liveAddress").val()
    };
    if (isMobileEmail(sysUser.mobile, sysUser.email)){
        $.ajax({
            cache : true,
            type : "POST",
            url : context + 'user/updateUser',
            data :JSON.stringify(sysUser),
            dataType : 'json',
            contentType:'application/json',
            error : function(request) {
                parent.layer.alert("Connection error");
            },
            success : function(data) {
                if (data.code === 200) {
                    if (data.data.code === 200){
                        parent.layer.msg("操作成功");
                    } else if(data.data.code === 200){
                        parent.layer.msg("操作失败");
                    }
                    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                    parent.layer.close(index);
                }
            }
        });
    }
}

function isMobileEmail(mobile,email) {
    var flag = true;
    if (mobile !== ""){
        var phoneReg = /^[1][3,4,5,7,8][0-9]{9}$/;
        if (!phoneReg.test(mobile)) {
            layer.msg("手机号格式不正确，请重新输入！");
            document.getElementById("mobile").value = "";
            flag =  false;
        }
    }
    if(email !== "") {
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        //调用正则验证test()函数
        isok= reg.test(email);
        if(!isok) {
            layer.msg("邮箱格式不正确，请重新输入！");
            document.getElementById("email").value = "";
            flag = false;
        }
    }
    return flag;
}
