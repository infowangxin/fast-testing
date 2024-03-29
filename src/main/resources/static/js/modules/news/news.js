$().ready(function () {
    var vm = new Vue({
        el: '#app',
        data: {
            tableData: [],
            total: 50,
            page_size: 5,
            current_page: 1
        },
        methods: {
            // 日期格式化方法
            formatDate(row, column) {
                // 获取单元格数据
                let data = row[column.property]
                if (data == null) {
                    return null
                }
                let dt = new Date(data)
                return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' ' + dt.getHours() + ':' + dt.getMinutes() + ':' + dt.getSeconds()
            },
            addNews: function () {
                layer.open({
                    type: 2,
                    title: '新增',
                    maxmin: true,
                    shadeClose: false, // 点击遮罩关闭层
                    area: ['800px', '700px'],
                    content: context + '/news/addNews',
                    end: function () {
                        vm.getNewsList();
                    }
                });
            },
            handleEdit: function (row) {
                layer.open({
                    type: 2,
                    title: '编辑',
                    maxmin: true,
                    shadeClose: false, // 点击遮罩关闭层
                    area: ['800px', '700px'],
                    content: context + '/news/updateNews/' + row.id,
                    end: function () {
                        vm.getNewsList();
                    }
                });
            },
            handleDelete: function (row) {
                layer.confirm("您确定要删除吗？", function (index) {
                    $.ajax({
                        url: context + '/news/deleteNews/' + row.id,
                        type: 'GET',
                        success: function (res) {
                            if (res.code === 200) {
                                if (res.data.status === true) {
                                    layer.msg("操作成功");
                                    vm.getNewsList();
                                } else {
                                    layer.msg("操作失败");
                                }
                            }
                        }
                    });
                });
            },
            handleSizeChange: function (val) {
                vm.page_size = val;
                this.getNewsList();
            },
            handleCurrentChange: function (val) {
                vm.current_page = val;
                this.getNewsList();
            },

            getNewsList: function () {
                $.ajax({
                    url: context + '/news/getNewsList',
                    type: 'POST',
                    dateType: 'json',
                    headers: {'Content-Type': 'application/json;charset=utf8'},
                    data: JSON.stringify({"page": this.current_page, "pageSize": this.page_size}),
                    success: function (res) {
                        vm.tableData = res.data.records;
                        vm.total = res.data.total;
                        vm.page_size = res.data.size;
                        vm.current_page = res.data.current;
                    }
                });
            }

        },
        mounted: function () {
            this.getNewsList();
        }
    });
});