var vm = new Vue({
    el: '#app',
    data: {
        tableData: [],
        total: 50,
        page_size: 5,
        current_page: 1
    },
    methods: {
        addMenu: function(){
            layer.open({
                type: 2,
                title: '新增',
                maxmin: true,
                shadeClose: false, // 点击遮罩关闭层
                area: ['800px', '520px'],
                content: context + 'menu/add',
                end: function () {
                    vm.getMenuList();
                }
            });
        },
        handleEdit: function(row) {
            layer.open({
                type: 2,
                title: '编辑',
                maxmin: true,
                shadeClose: false, // 点击遮罩关闭层
                area: ['800px', '520px'],
                content: context + 'menu/update?id='+row.id,
                end: function () {
                    vm.getMenuList();
                }
            });
        },
        handleDelete:function(row) {
            layer.confirm("您确定要删除吗？", function (index) {
                $.ajax({
                    url: context + 'menu/deleteMenu?id=' + row.id,
                    type: 'GET',
                    success: function (res) {
                        if (res.code === 200){
                            if (res.data.code === 200){
                                layer.msg("操作成功");
                                vm.getMenuList();
                            } else {
                                layer.msg("操作失败");
                            }

                        }
                    }
                });
            });
        },
        formatShow: function (row, column) {
            return row.isShow == true ? '启用' : '未启用';
        },
        handleSizeChange: function (val) {
            vm.page_size = val;
            this.getMenuList();
        },
        handleCurrentChange: function (val) {
            vm.current_page = val;
            this.getMenuList();
        },

        getMenuList: function () {
            $.ajax({
                url: context + 'menu/getMenuInfo?page=' + this.current_page + '&page_size=' + this.page_size,
                type: 'GET',
                success: function (res) {
                    console.log(res);
                    vm.tableData = res.data.menuList;
                    vm.total = res.data.total;
                    vm.page_size = res.data.page_size;
                    vm.current_page = res.data.page;
                }
            });
        }

    },
    mounted: function () {
        this.getMenuList();
    }
});
