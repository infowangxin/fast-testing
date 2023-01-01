$.validator.setDefaults({
    submitHandler: function () {
        addNews();
    }
});

var app = new Vue({
    el: '#app',
    data: {
        value1: ''
    },
    methods: {
        validateRule: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#newsForm").validate({
                rules: {
                    id: {
                        required: true
                    }, title: {
                        required: true
                    }, description: {
                        required: true
                    }, address: {
                        required: true
                    }
                },
                messages: {
                    title: {
                        required: icon + "请输入新闻标题"
                    }, description: {
                        required: icon + "请输入新闻内容"
                    }, address: {
                        required: icon + "请输入新闻发生地址"
                    }
                }
            })
        }
    },
    mounted: function () {
        this.validateRule();
    }
});

function addNews() {
    let news = {
        'title': $("#title").val(),
        'description': $("#description").val(),
        'address': $("#address").val(),
        'newsTime': $("#newsTimeDatePicker").val()
    };
    $.ajax({
        cache: true,
        type: "POST",
        url: context + '/news/addNews',
        data: JSON.stringify(news),
        dataType: 'json',
        contentType: 'application/json',
        error: function (request) {
            parent.layer.alert("Connection error");
        },
        success: function (data) {
            if (data.code === 200) {
                if (data.data.status === true) {
                    parent.layer.msg("操作成功");
                } else {
                    parent.layer.msg("操作失败");
                }
                let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);
            }
        }
    });
}
