$.validator.setDefaults({
    submitHandler : function() {
        updateUser();
    }
});

var app = new Vue({
    el:"#app",
    data:{

    },
    methods:{
        getMenuLevel:function() {
            $.ajax({
                cache : true,
                type : "GET",
                url : context + '/menu/getMenuLevel',
                error : function(request) {
                    parent.layer.alert("Connection error");
                },
                success : function(data) {
                    if (data.code === 200) {
                        $("#menuLevel").html("");
                        for (var i = 0; i < data.data.menuLevel.length; i++) {
                            var level = "";
                            level += "<option value='"+data.data.menuLevel[i]+"'>"+data.data.menuLevel[i]+"</option>";
                            $("#menuLevel").append(level);
                            level = "";
                        }
                    }
                    $("#menuLevel option[value='"+$("#menuLevels").val()+"']").attr("selected","selected");
                    app.pdMenuLevel($("#menuLevel").val());
                }
            });
        },
        getValue:function() {
            $("#menuLevel").change(function(){
                var menuLevel =  $(this).children('option:selected').val();
                app.pdMenuLevel(menuLevel);
            });
        },
        pdMenuLevel:function(menuLevel) {
            if (menuLevel !== "1" && menuLevel !== null) {
                $.ajax({
                    cache: true,
                    type: "GET",
                    url: context + '/menu/getPreviousMenu?menuLevel=' + menuLevel,
                    error: function (request) {
                        parent.layer.alert("Connection error");
                    },
                    success: function (data) {
                        if (data.code === 200) {
                            $("#menuNames").show();
                            $("#menuNames").html("");
                            $("#menuNames").append("<label class='col-sm-3 control-label'>一级菜单：</label>");
                            var level = "";
                            level += "<div class='layui-input-inline'>";
                            level += "<select id='menuLevel' name='modules' lay-verify='required' lay-search='' style='width: 235px;height: 33.9px;border: 1px solid #ccc;border-radius: 4px;margin-left: 6%;'>";
                            for (var i = 0; i < data.data.menuNames.length; i++){
                                level += "<option value='"+data.data.menuNames[i].id+"'>"+data.data.menuNames[i].menuName+"</option>";
                            }
                            level += "</select></div></div>";
                            $("#menuNames").append(level);
                            $("#menuLevel option[value='"+$("#parentId").val()+"']").attr("selected","selected");
                            $("#menuHrefs").show();
                            $("#menuIcons").hide();
                        }
                    }
                });
            } else {
                $("#menuNames").hide();
                $("#menuHrefs").hide();
                $("#menuIcons").show();
            }
        },
        validateRule:function() {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#signupForm").validate({
                rules : {
                    menuName : {
                        required : true
                    }, menuCode : {
                        required : true
                    }, menuHref : {
                        required : true
                    }, menuLevel : {
                        required : true
                    }, menuWeight : {
                        required : true
                    }, menuIcon : {
                        required : true
                    }
                },
                messages : {
                    menuName : {
                        required : icon + "请输入菜单名称"
                    }, menuCode : {
                        required : icon + "请输入菜单别名"
                    }, menuHref : {
                        required : icon + "请输入菜单链接"
                    }, menuLevel : {
                        required : icon + "请输入菜单层级"
                    }, menuWeight : {
                        required : icon + "请输入排序"
                    }, menuIcon : {
                        required : icon + "请输入菜单图标"
                    }
                }
            })
        }
    },
    mounted:function() {
        this.getMenuLevel();
        this.getValue();
        if ($("#isShow").val() === "true"){
            $(":radio[name='isShow'][value='1']").prop("checked", "checked");
        } else {
            $(":radio[name='isShow'][value='0']").prop("checked", "checked");
        }
        this.validateRule();
    }
});

function updateUser(){
    var menuVO = {
        "id":$("#id").val(),
        "menuName":$("#menuName").val(),
        "menuCode":$("#menuCode").val(),
        "menuHref":$("#menuHref").val(),
        "menuLevel":$("#menuLevel").val(),
        "menuIcon":$("#menuIcon").val(),
        "menuNames":'',
        "menuWeight":$("#menuWeight").val(),
        "isShow":$('input:radio:checked').val()===undefined?"":$('input:radio:checked').val()
    };
    if (menuVO.menuLevel !== "1"){
        menuVO.menuNames = $('#menuNames option:selected').text();
    }
    $.ajax({
        cache : true,
        type : "POST",
        url : context + '/menu/updateMenu',
        data :JSON.stringify(menuVO),
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