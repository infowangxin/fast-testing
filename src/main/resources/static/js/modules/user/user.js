$().ready(function(){
    var vm = new Vue({
        el: '#app',
        data: {
            tableData: [],
            total: 50,
            page_size: 5,
            current_page: 1
        },
        methods: {
            addUser:function() {
                layer.open({
                    type: 2,
                    title: '新增',
                    maxmin: true,
                    shadeClose: false, // 点击遮罩关闭层
                    area: ['800px', '700px'],
                    content: context + '/user/add',
                    end: function () {
                        vm.getUserList();
                    }
                });
            },
            handleEdit: function(row) {
                layer.open({
                    type: 2,
                    title: '编辑',
                    maxmin: true,
                    shadeClose: false, // 点击遮罩关闭层
                    area: ['800px', '700px'],
                    content: context + '/user/update?id='+row.id,
                    end: function () {
                        vm.getUserList();
                    }
                });
            },
            editPassword:function(row) {
                layer.confirm('该用户密码将重置为123456，是否确认？', {
                    btn: ['确认','取消'] //按钮
                }, function(){
                    $.ajax({
                        url: context + '/user/editPassword?id='+row.id,
                        type: 'GET',
                        success: function (res) {
                            if (res.code === 200){
                                if (res.data.code === 200){
                                    layer.msg("重置密码操作成功");
                                    vm.getUserList();
                                } else if(res.data.code === 500){
                                    layer.msg("重置密码操作失败");
                                }
                            }
                        }
                    });
                }, function(){

                });
            },
            handleDelete:function(row) {
                layer.confirm("您确定要删除吗？", function (index) {
                    $.ajax({
                        url: context + '/user/deleteUser?id=' + row.id,
                        type: 'GET',
                        success: function (res) {
                            if (res.code === 200){
                               if (res.data.code === 200){
                                   layer.msg("操作成功");
                                   vm.getUserList();
                               } else if(res.data.code === 500){
                                   layer.msg("操作失败");
                               }
                            }
                        }
                    });
                });
            },
            handleSizeChange: function (val) {
                vm.page_size = val;
                this.getUserList();
            },
            handleCurrentChange: function (val) {
                vm.current_page = val;
                this.getUserList();
            },

            getUserList: function () {
                $.ajax({
                    url: context + '/user/getUserInfo?page=' + this.current_page + '&page_size=' + this.page_size,
                    type: 'GET',
                    success: function (res) {
                        vm.tableData = res.data.sysUserList;
                        vm.total = res.data.total;
                        vm.page_size = res.data.page_size;
                        vm.current_page = res.data.page;
                    }
                });
            }

        },
        mounted: function () {
            this.getUserList();
        }
    });
});